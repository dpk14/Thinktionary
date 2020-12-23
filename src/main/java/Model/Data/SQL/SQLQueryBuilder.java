package Model.Data.SQL;

import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Parameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Deprecated
public class SQLQueryBuilder {
    private static final String REMOVE = " DELETE FROM ";
    private static final String SELECT_ALL = " SELECT * FROM ";
    private static final String INSERT = " INSERT INTO ";
    private static final String UPDATE = " UPDATE ";
    private static final String MODIFY = "  UPDATE ";
    private static final String GET_LAST_INSERT = " SELECT last_insert_rowid();";
    private static final String TABLE_EXISTS = " SELECT EXISTS " +
            "(" +
            "SELECT 1 " +
            "FROM information_schema.tables " +
            "WHERE table_name =? " +
            ");";
    private static final String REMOVE_TABLE = " DROP TABLE %s;";

    public static final String removeTable(String tableName) {return String.format(REMOVE_TABLE, tableName);}

    public static final PreparedStatement createTable(Connection con, String tableName, Map<String, String> columnToType) throws SQLException {
        String command = "CREATE TABLE IF NOT EXISTS ? (";
        for (int count = 0; count < columnToType.keySet().size(); count++) {
            command += "? ? ";
            if (count!=columnToType.keySet().size()-1) {
                command += ", ";
            }
        }
        command+=")";

        PreparedStatement commandSt = con.prepareStatement(command);
        commandSt.setString(0, tableName);
        int count = 1;
        for (String columnName : columnToType.keySet()) {
            String type = columnToType.get(columnName);
            commandSt.setString(count, columnName);
            count++;
            commandSt.setString(count, type);
            count++;
        }
        return commandSt;
    }

    public static PreparedStatement insert(Connection con, String tableName, List<Parameter> parameters) throws SQLException {
        String header = INSERT + "?";
        String string = " (";

        for(Parameter param : parameters){
            string += "?";
            if(!param.equals(parameters.get(parameters.size()-1))) {
                string+=", ";
            } else {
                string+=")";
            }
        }

        String statement = header + string + " VALUES " + string + ";";
        PreparedStatement commandSt = con.prepareStatement(statement);
        commandSt.setString(0, tableName);

        int index = 1;
        for(Parameter param : parameters){
            commandSt.setString(index, param.getMyParamName());
            index++;
        }
        for(Parameter param : parameters){
            commandSt.setString(index, param.getMyValue());
            index++;
        }

        return commandSt;
    }

    public static PreparedStatement modify(Connection con, String tableName, List<Parameter> parameters, List<Condition> conditions) throws SQLException {
        String command = MODIFY + "? SET ";
        for(Parameter param : parameters){
            command += "? = ?";
            if(!param.equals(parameters.get(parameters.size()-1))) {
                command+=",";
            }
            command+=" ";
        }

        String query = command + buildConditional(conditions) + ";";

        PreparedStatement commandSt = con.prepareStatement(query);
        commandSt.setString(0, tableName);
        int index = 1;
        for(Parameter param : parameters){
            commandSt.setString(index, param.getMyParamName());
            index++;
            commandSt.setString(index, param.getMyValue());
            index++;
        }

        fillConditional(index, commandSt, conditions);

        return commandSt;
    }

    public static PreparedStatement select(Connection con, String tableName, List<Condition> conditions) throws SQLException {
        return conditionalQuery(con, SELECT_ALL, tableName, conditions);
    }

    public static PreparedStatement remove(Connection con, String tableName, List<Condition> conditions) throws SQLException {
        return conditionalQuery(con, REMOVE, tableName, conditions);
    }

    //Helpers:

    private static PreparedStatement conditionalQuery(Connection con, String action, String from, List<Condition> condition) throws SQLException {
        String query = action + from + buildConditional(condition) + ";";
        PreparedStatement commandSt = con.prepareStatement(query);
        fillConditional(0, commandSt, condition);
        return commandSt;
    }

    private static String buildConditional(List<Condition> conditions) {
        String conditional = " ";
        for (int i = 0; i < conditions.size(); i++) {
            String header = " AND ";
            if (i == 0) header = " WHERE ";
            conditional += header + "?";
        }
        return conditional;
    }

    private static PreparedStatement fillConditional(int index, PreparedStatement pst, List<Condition> conditions) throws SQLException {
        for (int i = 0; i < conditions.size(); i++) {
            pst.setString(index, conditions.get(i).toString());
        }
        return pst;
    }

}
