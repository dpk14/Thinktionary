package Model.Data.API.Run;

import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends RunDBAPI {

    public static final int CONF_KEY_EXPIRY_MS = 5000;

    Map<String, Object> emailExpiryLocks;

    public LoginDBAPI() {
        super();
        this.emailExpiryLocks = new HashMap<>();
        retrieveExistingConfKeyExpiryThreads();
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
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));
        conditions.add(new Equals(ColumnInfo.getPASSWORD(), passWord));

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERNAME(), userName));
        parameters.add(new Parameter(ColumnInfo.getPASSWORD(), passWord));

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

    public void storeEmailConfirmationKey(String email, int key) {
        if (tableEntryExists(TableNames.getEmailConfirmation(), ColumnInfo.getEMAIL(), email)) {
            removeEmailConfirmationKey(email);
            this.emailExpiryLocks.get(email).notify(); // if a confirmation key already exists, remove lock and let it expire, replacing with new
        }
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getEMAIL(), email));
        parameters.add(new Parameter(ColumnInfo.CONF_KEY, key));

        userAction(SQLQueryBuilder.insert(TableNames.getEmailConfirmation(), parameters));
        startConfKeyExpiryThreadAndStoreLock(email);
    }

    public void removeEmailConfirmationKey(String email) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        userAction(SQLQueryBuilder.remove(TableNames.getEmailConfirmation(), conditions));
        if (this.emailExpiryLocks.containsKey(email)) {
            this.emailExpiryLocks.remove(email);
        }
    }

    public void verifyConfirmationKey(String key, String email) throws UserErrorException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        conditions.add(new Equals(ColumnInfo.CONF_KEY, Integer.parseInt(key)));
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(TableNames.getEmailConfirmation(), conditions));
        System.out.println(userInfo);
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

    public List<Map<String, Object>> loadUserInfoTable() {
        List<Map<String, Object>> table = loadTable(TableNames.getUserInfo());
        try {
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object startConfKeyExpiryThreadAndStoreLock(String email) {
        class ConfKeyExpiryThread implements Runnable
        {
            Object dayLock;
            public ConfKeyExpiryThread(Object dayLock) {
                this.dayLock = dayLock;
            }
            public void run()
            {
                try {
                    this.dayLock.wait(CONF_KEY_EXPIRY_MS);
                    removeEmailConfirmationKey(email);
                } catch (InterruptedException e) {
                    System.out.println(String.format("Confirmation key for %s has been removed", email));
                }
            }
        }
        Object dayLock = new Object();
        Thread confKeyExpiryThread = new Thread(new ConfKeyExpiryThread(dayLock));
        confKeyExpiryThread.start();
        emailExpiryLocks.put(email, dayLock);
        return confKeyExpiryThread;
    }

    private void retrieveExistingConfKeyExpiryThreads() {
        List<Map<String, Object>> keys = loadTable(TableNames.getEmailConfirmation());
        for (Map<String, Object> key : keys) {
            String email = key.get(ColumnInfo.getEMAIL()).toString();
            startConfKeyExpiryThreadAndStoreLock(email);
        }
    }
}
