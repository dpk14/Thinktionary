package Model.Data.SQL.Queries;

import Model.Data.SQL.QueryObjects.Condition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Select extends Query {

    private List<Condition> conditions;

    public Select(String tableName, List<Condition> conditions) {
        super(tableName);
        this.conditions = conditions;
    }

    @Override
    public PreparedStatement buildStatement(Connection con) throws SQLException {
        return conditionalQuery(con, SELECT_ALL, tableName, conditions);
    }

    @Override
    public String getQueryString() {
        return getConditionalQueryString(SELECT_ALL, tableName, conditions);
    }
}