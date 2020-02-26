package Model.Data.API.Run;

import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import sun.tools.jconsole.Tab;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends RunDBAPI {

    public LoginDBAPI(){super();}

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

    public List<Map<String, Object>> createUser(String userName, String passWord) throws AccountExistsException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERNAME(), userName));
        conditions.add(new Equals(ColumnInfo.getPASSWORD(), passWord));

        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERNAME(), userName));
        parameters.add(new Parameter(ColumnInfo.getPASSWORD(), passWord));

        List<Map<String, Object>> userInfo;
        try {
            userInfo = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions), myDBUrl, myDBUsername, myDBPassword);
            if(userInfo.size() != 0) {
                throw new AccountExistsException();
            }
            DBUtils.userAction(SQLQueryBuilder.insert(TableNames.getUserInfo(), parameters), myDBUrl, myDBUsername, myDBPassword);
            userInfo = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getUserInfo(), conditions), myDBUrl, myDBUsername, myDBPassword);
            return userInfo;
        }
        catch(SQLException e){
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
