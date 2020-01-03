package src.main.java.BackEnd.Data.API;

import src.main.java.BackEnd.Data.Initialization.DBInit;
import src.main.java.BackEnd.Data.Lib.Paths.DBFileNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBUrls;
import src.main.java.BackEnd.Data.Lib.SQLStrings.SQLQuery;
import src.main.java.BackEnd.Data.Utils.DBUtils;
import src.main.java.BackEnd.ErrorHandling.Errors.CorruptDBError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DBAPI {

    String myDBUrl;
    String myDBUsername;
    String myDBPassword;

    public DBAPI(String dbUsername, String dbPassword, String dbUrl) {
        myDBUsername = dbUsername;
        myDBPassword = dbPassword;
        if (dbUrl == null) {
            myDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());
        } else myDBUrl = dbUrl;
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
