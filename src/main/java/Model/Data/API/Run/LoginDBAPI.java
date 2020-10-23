package Model.Data.API.Run;

import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends RunDBAPI {

    public LoginDBAPI() throws LoadPropertiesException {super();}

    public List<Map<String, Object>> login(String userName, String passWord) throws InvalidLoginException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));
        conditions.add(new Equals(ColumnInfo.getPASSWORD(), passWord));
        try {
            List<Map<String, Object>> userInfo = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions), myDBUrl, myDBUsername, myDBPassword);
            if(userInfo.size() != 1){
                throw new InvalidLoginException();
            }
            return userInfo;
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public List<Map<String, Object>> createUser(String userName, String passWord, String email) throws UserErrorException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));
        conditions.add(new Equals(ColumnInfo.getPASSWORD(), passWord));

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERNAME(), userName));
        parameters.add(new Parameter(ColumnInfo.getPASSWORD(), passWord));

        List<Map<String, Object>> userInfo;
        try {
            throwExceptionIfUserInfoExists(userName, passWord, email);
            DBUtils.userAction(SQLQueryBuilder.insert(TableNames.getUserInfo(), parameters), myDBUrl, myDBUsername, myDBPassword);
            userInfo = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions), myDBUrl, myDBUsername, myDBPassword);
            return userInfo;
        }
        catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

    public void storeEmailConfirmationKey(String email, int key) {
        try {
            List<Parameter> parameters = new ArrayList<>();
            parameters.add(new Parameter(ColumnInfo.getEMAIL(), email));
            parameters.add(new Parameter(ColumnInfo.CONF_KEY, key));

            DBUtils.userAction(SQLQueryBuilder.insert(TableNames.getEmailConfirmation(), parameters), myDBUrl, myDBUsername, myDBPassword);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeEmailConfirmationKey(String email) {
        try {
            List<Condition> conditions = new ArrayList<>();
            conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
            DBUtils.userAction(SQLQueryBuilder.remove(TableNames.getEmailConfirmation(), conditions), myDBUrl, myDBUsername, myDBPassword);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void verifyConfirmationKey(String key, String email) throws UserErrorException {
        try {
            List<Condition> conditions = new ArrayList<>();
            conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
            conditions.add(new Equals(ColumnInfo.CONF_KEY, Integer.parseInt(key)));
            List<Map<String, Object>> userInfo = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getEmailConfirmation(), conditions), myDBUrl, myDBUsername, myDBPassword);
            System.out.println(userInfo);
            if (userInfo.size() != 1) {
                throw new InvalidConfirmationKeyException();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void throwExceptionIfUserInfoExists(String userName, String password, String email) throws UserErrorException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));
        throwExceptionIfExists(conditions, new AccountExistsException());
        conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEMAIL(), email));
        throwExceptionIfExists(conditions, new EmailExistsException());
    }

    private void throwExceptionIfExists(List<Condition> conditions, UserErrorException exceptionToThrow) throws UserErrorException {
        try {
            List<Map<String, Object>> userInfo = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions), myDBUrl, myDBUsername, myDBPassword);
            if (userInfo.size() != 0) {
                throw exceptionToThrow;
            }
        }
        catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

    public List<Map<String, Object>> loadUserInfoTable() {
        List<Map<String, Object>> table = loadTable(TableNames.getUserInfo());
        try {
            return table;
        }
        catch(Exception e){
            throw new CorruptDBError(e);
        }
    }
}
