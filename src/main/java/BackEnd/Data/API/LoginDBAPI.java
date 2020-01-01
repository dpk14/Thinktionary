package src.main.java.BackEnd.Data.API;

import src.main.java.BackEnd.API.Journal.Entry;
import src.main.java.BackEnd.API.Login.User;
import src.main.java.BackEnd.Data.Lib.Paths.DBFileNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBUrls;
import src.main.java.BackEnd.Data.Lib.SQLStrings.ColumnLabels;
import src.main.java.BackEnd.Data.Lib.SQLStrings.SQLQuery;
import src.main.java.BackEnd.Data.Lib.SQLStrings.TableNames;
import src.main.java.BackEnd.Data.Utils.DBUtils;
import src.main.java.BackEnd.Data.Utils.ParserUtils;
import src.main.java.BackEnd.ErrorHandling.Errors.CorruptDBError;
import src.main.java.BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import src.main.java.BackEnd.ErrorHandling.Exceptions.InvalidLoginException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBAPI extends DBAPI{

    public LoginDBAPI(String dbUsername, String dbPassword, String dbUrl){
        super(dbUsername, dbPassword, dbUrl);
    }

    public int login(String userName, String passWord) throws InvalidLoginException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        try {
            List<Map<String, Object>> userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), myDBUrl, myDBUsername, myDBPassword);
            if(userInfo.size() != 1){
                throw new InvalidLoginException();
            }
            return ParserUtils.getUserID(userInfo);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public void createAccount(String userName, String passWord) throws AccountExistsException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        List<Map<String, Object>> userInfo = new ArrayList<>();
        try {
            userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), myDBUrl, myDBUsername, myDBPassword);
            if(userInfo.size() != 0) {
                throw new AccountExistsException();
            }
            DBUtils.userQuery(map, SQLQuery.addUser(), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public Map<Integer, User> loadUserInfoMap() {
        List<Map<String, Object>> table = loadTable(TableNames.getUserInfo());
        try {
            return ParserUtils.parseUserInfoMap(table);
        }
        catch(Exception e){
            throw new CorruptDBError(e);
        }
    }

    @Override
    public void createTables() {
        String tableName = TableNames.getUserInfo();
        List<String> columnNames = ColumnLabels.getTableColumnNames(tableName);
        createTable(tableName, columnNames);
    }

}
