package Model.Data.SQL.Queries;

import Model.Data.SQL.QueryObjects.Condition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class Query {

    String tableName;

    public Query(String tableName) {
        this.tableName = tableName;
    }

    protected static final String REMOVE = " DELETE FROM ";
    protected static final String SELECT_ALL = " SELECT * FROM ";
    protected static final String INSERT = " INSERT INTO ";
    protected static final String UPDATE = " UPDATE ";
    protected static final String MODIFY = "  UPDATE ";
    protected static final String GET_LAST_INSERT = " SELECT last_insert_rowid();";
    protected static final String TABLE_EXISTS = " SELECT EXISTS " +
            "(" +
            "SELECT 1 " +
            "FROM information_schema.tables " +
            "WHERE table_name =? " +
            ");";
    protected static final String REMOVE_TABLE = " DROP TABLE %s;";

    public abstract PreparedStatement buildStatement(Connection con) throws SQLException;

    public abstract String getQueryString();

    protected static PreparedStatement conditionalQuery(Connection con, String action, String from, List<Condition> condition) throws SQLException {
        String query = getConditionalQueryString(action, from, condition);
        PreparedStatement commandSt = con.prepareStatement(query);
        fillConditional(1, commandSt, condition);
        return commandSt;
    }

    protected static String getConditionalQueryString(String action, String from, List<Condition> condition) {
        String query = action + from + buildConditional(condition) + ";";
        return query;
    }

    protected static String buildConditional(List<Condition> conditions) {
        String conditional = " ";
        for (int i = 0; i < conditions.size(); i++) {
            String header = " AND ";
            if (i == 0) header = " WHERE ";
            conditional += header + conditions.get(i).toString();
        }
        return conditional;
    }

    protected static PreparedStatement fillConditional(int index, PreparedStatement pst, List<Condition> conditions) throws SQLException {
        for (int i = 0; i < conditions.size(); i++) {
            pst.setString(index, conditions.get(i).getMyValue());
            index++;
        }
        return pst;
    }
}
