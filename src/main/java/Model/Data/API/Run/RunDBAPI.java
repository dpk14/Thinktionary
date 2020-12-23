package Model.Data.API.Run;

import Model.Data.API.DBAPI;
import Model.Data.SQL.Queries.Select;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RunDBAPI extends DBAPI {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    public RunDBAPI() {
        super();
    }

    protected List<Map<String, Object>> loadTable(String tableName) {
        return userQuery(new Select(tableName, new ArrayList<>()));
    }

    protected List<Map<String, Object>> loadTableByParamater(String tableName, String parameterType, Object parameter) {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(parameterType, parameter));

        List<Map<String, Object>> ret = userQuery(new Select(tableName, conditions));
        return ret;
    }
}
