package Model.Data.API.Run;

import Model.Data.API.DBAPI;
import Model.Data.SQL.SQLQuery;
import Model.Data.SQL.TableNames;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RunDBAPI extends DBAPI {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    public RunDBAPI(){
        super();
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
        map.put(1, parameter);
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            System.out.println(tableName + " " +  parameterType + " " +  parameter);
            System.out.println(DBUtils.userQuery(new HashMap<>(), "SELECT * FROM " + TableNames.getUserTopic() + " WHERE UserID = 1", myDBUrl, myDBUsername, myDBPassword).size());
            ret = DBUtils.userQuery(map, SQLQuery.getLoadTableByParameter(tableName, parameterType), myDBUrl, myDBUsername, myDBPassword);
            System.out.println(ret.size() + "OEJIUHDPIEUHDH");
            return ret;
        } catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

}
