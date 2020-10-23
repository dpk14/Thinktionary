package Model.Data.API.Run;

import Model.Data.API.DBAPI;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;

import java.sql.SQLException;
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

    public RunDBAPI() throws LoadPropertiesException {
        super();
    }

    protected List<Map<String, Object>> loadTable(String tableName) {
        try {
            return DBUtils.userQuery(SQLQueryBuilder.select(tableName, new ArrayList<>()), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

    protected List<Map<String, Object>> loadTableByParamater(String tableName, String parameterType, Object parameter) {
        List<Condition > conditions = new ArrayList<>();
        conditions.add(new Equals(parameterType, parameter));

        try {
            List<Map<String, Object>> ret = DBUtils.userQuery(SQLQueryBuilder.select(tableName, conditions), myDBUrl, myDBUsername, myDBPassword);
            return ret;
        } catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

}
