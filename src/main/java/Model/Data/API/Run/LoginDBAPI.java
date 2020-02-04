package Model.Data.API.Run;

import Model.Data.SQL.SQLQuery;
import Model.Data.SQL.TableNames;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;

import javax.management.Query;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends RunDBAPI {

    public LoginDBAPI(){super();}

    public List<Map<String, Object>> login(String userName, String passWord) throws InvalidLoginException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        try {
            List<Map<String, Object>> userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), myDBUrl, myDBUsername, myDBPassword);
            if(userInfo.size() != 1){
                throw new InvalidLoginException();
            }
            return userInfo;
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public List<Map<String, Object>> createAccount(String userName, String passWord) throws AccountExistsException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "'"+userName+"'");
        map.put(2, "'"+passWord+"'");
        List<Map<String, Object>> userInfo = new ArrayList<>();
        try {
            System.out.println(SQLQuery.getUser());
            userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), myDBUrl, myDBUsername, myDBPassword);
            if(userInfo.size() != 0) {
                throw new AccountExistsException();
            }
            DBUtils.userAction(map, SQLQuery.addUser(), myDBUrl, myDBUsername, myDBPassword);
            userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), myDBUrl, myDBUsername, myDBPassword);
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
