package Model.Data.API.Run;

import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends RunDBAPI {

    public static final int CONF_KEY_EXPIRY_MS = 86405000;

    Map<String, ConfKeyExpiryThread> emailConfExpiryThreads;
    Map<String, Object> emailConfExpiryLocks;

    public LoginDBAPI() {
        super();
        this.emailConfExpiryThreads = new HashMap<>();
        this.emailConfExpiryLocks = new HashMap<>();
        retrieveExistingConfKeyExpiryThreadsAndLocks();
    }

    class ConfKeyExpiryThread implements Runnable
    {
        private Object dayLock;
        private boolean replacementPending;
        private String replacementConfKey;
        private String email;

        public ConfKeyExpiryThread(String email, Object dayLock) {
            this.dayLock = dayLock;
            this.replacementPending = false;
            this.email = email;
        }
        public void run()
        {
            try {
                synchronized (this.dayLock) {
                    this.dayLock.wait(CONF_KEY_EXPIRY_MS);
                    removeEmailConfirmationKey(this.email);
                    System.out.println(String.format("Confirmation key for %s has been removed", this.email));
                    if (this.replacementPending) {
                        storeEmailConfirmationKey(this.email, this.replacementConfKey);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public void setNewConfirmationKey(String newConfirmationKey) {
            this.replacementPending = true;
            this.replacementConfKey = newConfirmationKey;
        }
    }

    public List<Map<String, Object>> login(String userName, String passWord) throws InvalidLoginException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));
        conditions.add(new Equals(ColumnInfo.getPASSWORD(), passWord));
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions));
        if (userInfo.size() != 1) {
            throw new InvalidLoginException();
        }
        return userInfo;
    }

    public List<Map<String, Object>> createUser(String userName, String passWord, String email) throws UserErrorException {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERNAME(), userName));
        parameters.add(new Parameter(ColumnInfo.getPASSWORD(), passWord));
        parameters.add(new Parameter(ColumnInfo.getEMAIL(), email));

        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));

        List<Map<String, Object>> userInfo;
        if (tableEntryExists(TableNames.getUserInfo(), ColumnInfo.getUSERNAME(), userName)) {
            throw new AccountExistsException();
        } else if (tableEntryExists(TableNames.getUserInfo(), ColumnInfo.getEMAIL(), email)) {
            throw new EmailExistsException();
        }
        userAction(SQLQueryBuilder.insert(TableNames.getUserInfo(), parameters));
        userInfo = userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions));
        return userInfo;
    }

    public void storeEmailConfirmationKey(String email, String key) {
        if (tableEntryExists(TableNames.getEmailConfirmation(), ColumnInfo.getEMAIL(), email)) {
            expireOldKeyAndSetNewWhenDone(email, key);
            // ^ if a confirmation key already exists, tell the thread expiring it to hurry up,
            // then replace with the new value when it's done
        } else { //otherwise, insert the key as usual
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(ColumnInfo.getEMAIL(), email));
            parameters.add(new Parameter(ColumnInfo.CONF_KEY, key));

            String query = SQLQueryBuilder.insert(TableNames.getEmailConfirmation(), parameters);
            userAction(query);
            startConfKeyExpiryThreadAndStoreLock(email);
        }
    }

    public void removeEmailConfirmationKey(String email) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        String query = SQLQueryBuilder.remove(TableNames.getEmailConfirmation(), conditions);
        userAction(query);
        if (this.emailConfExpiryThreads.containsKey(email)) {
            this.emailConfExpiryThreads.remove(email);
            this.emailConfExpiryLocks.remove(email);
        }
    }

    public void verifyConfirmationKey(String key, String email) throws UserErrorException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        conditions.add(new Equals(ColumnInfo.CONF_KEY, key));
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(TableNames.getEmailConfirmation(), conditions));
        if (userInfo.size() != 1) {
            throw new InvalidConfirmationKeyException();
        }
    }

    public boolean tableEntryExists(String tableName, String columnName, String value) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(columnName, value));
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(tableName, conditions));
        return userInfo.size() > 0;
    }

    public boolean tableEntryExists(String tableName, Map<String, String> colToVal) {
        List<Condition> conditions = new ArrayList<>();
        for (String col : colToVal.keySet()) {
            conditions.add(new Equals(col, colToVal.get(col)));
        }
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(tableName, conditions));
        return userInfo.size() > 0;
    }

    public void changeUserInfo(String username, String column, String newValue) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(column, newValue));
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), username));
        userAction(SQLQueryBuilder.modify(TableNames.getUserInfo(), parameters, conditions));
    }

    private void expireOldKeyAndSetNewWhenDone(String email, String newKey) {
        ConfKeyExpiryThread confKeyExpiryThread = this.emailConfExpiryThreads.get(email);
        Object lock = this.emailConfExpiryLocks.get(email);
        confKeyExpiryThread.setNewConfirmationKey(newKey);
        synchronized (lock) {
            lock.notify();
        }
    }

    private Object startConfKeyExpiryThreadAndStoreLock(String email) {
        Object dayLock = new Object();
        ConfKeyExpiryThread confKeyExpiryThread = new ConfKeyExpiryThread(email, dayLock);
        Thread threadRunner = new Thread(confKeyExpiryThread);
        threadRunner.start();

        this.emailConfExpiryThreads.put(email, confKeyExpiryThread);
        this.emailConfExpiryLocks.put(email, dayLock);
        return confKeyExpiryThread;
    }

    private void retrieveExistingConfKeyExpiryThreadsAndLocks() {
        List<Map<String, Object>> keys = loadTable(TableNames.getEmailConfirmation());
        for (Map<String, Object> key : keys) {
            String email = key.get(ColumnInfo.getEMAIL()).toString();
            startConfKeyExpiryThreadAndStoreLock(email);
        }
    }

}
