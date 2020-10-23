package Model.Data.API.Run;

import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends RunDBAPI {

    public LoginDBAPI() {
        super();
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
        if (usernameExists(userName)) {
            throw new AccountExistsException();
        } else if (emailExists(email)) {
            throw new EmailExistsException();
        }
        userAction(SQLQueryBuilder.insert(TableNames.getUserInfo(), parameters));
        userInfo = userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions));
        return userInfo;
    }

    public void storeEmailConfirmationKey(String email, int key) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getEMAIL(), email));
        parameters.add(new Parameter(ColumnInfo.CONF_KEY, key));

        userAction(SQLQueryBuilder.insert(TableNames.getEmailConfirmation(), parameters));
    }

    public void removeEmailConfirmationKey(String email) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        userAction(SQLQueryBuilder.remove(TableNames.getEmailConfirmation(), conditions));
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

    public boolean emailExists(String email) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions));
        System.out.println("YU");
        System.out.println(userInfo);
        return userInfo.size() != 0;
    }

    public boolean usernameExists(String username) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), username));
        List<Map<String, Object>> userInfo = userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions));
        return userInfo.size() != 0;
    }

    public List<Map<String, Object>> loadUserInfoTable() {
        List<Map<String, Object>> table = loadTable(TableNames.getUserInfo());
        try {
            return table;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
