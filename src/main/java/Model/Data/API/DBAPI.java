package Model.Data.API;

import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;
import Model.Data.Lib.SQLStrings.SQLQuery;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DBAPI {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    private static final String DB_URL_DEFAULT = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());
    private static final String DB_USERNAME_DEFAULT = "kingsbda";
    private static final String DB_PASSWORD_DEFAULT = "1qazxsw2";
    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;

    public DBAPI(String dbUsername, String dbPassword, String dbUrl) {
        myDBUsername = dbUsername == null? DB_USERNAME_DEFAULT : dbUsername;
        myDBPassword = dbPassword == null? DB_PASSWORD_DEFAULT : dbPassword;
        myDBUrl = dbUrl == null? DB_URL_DEFAULT : dbUrl;
    }

    public DBAPI(){
        myDBUsername = DB_USERNAME_DEFAULT;
        myDBPassword = DB_PASSWORD_DEFAULT;
        myDBUrl = DB_URL_DEFAULT;
    }

    protected List<Map<String, Object>> loadTable(String tableName) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            return DBUtils.userQuery(map, SQLQuery.loadTable(), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

    protected List<Map<String, Object>> loadTableByParamater(String tableName, String parameterType, String parameter) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, parameterType);
        map.put(3, parameter);
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            return DBUtils.userQuery(map, SQLQuery.loadTable(), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }
}
