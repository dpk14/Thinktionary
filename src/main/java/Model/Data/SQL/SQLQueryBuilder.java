package Model.Data.SQL;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Parameter;
import java.util.Map;
import java.util.List;

public class SQLQueryBuilder {
    private static final String REMOVE = " DELETE FROM ";
    private static final String SELECT_ALL = " SELECT * FROM ";
    private static final String INSERT = " INSERT INTO ";
    private static final String MODIFY = "  UPDATE ";
    private static final String GET_LAST_INSERT = "SELECT last_insert_rowid();";
    private static final String TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";
    private static final String REMOVE_TABLE = "DROP TABLE %s;";

    public static final String tableExists() {return TABLE_EXISTS;}

    public static final String removeTable(String tableName) {return String.format(REMOVE_TABLE, tableName);}

    public static String getLastInsertID() {
        return GET_LAST_INSERT;
    }

    public static final String createTable(String tableName, Map<String, String> columnToType) {
        String command = "CREATE TABLE " + tableName + " (";
        int count = 0;
        for (String columnName : columnToType.keySet()) {
            String type = columnToType.get(columnName);
            command += columnName + " " + type + " ";
            count++;
            if (count!=columnToType.keySet().size()) {
                command += ", ";
            }
        }
        command+=")";
        return command;
    }

    public static String insert(String tableName, List<Parameter> parameters) {
        String header = INSERT + tableName;
        String paramsString = " (";
        String valsString = " VALUES (";

        for(Parameter param : parameters){
            paramsString += param.getMyParamName();
            valsString += param.getMyValue();
            if(!param.equals(parameters.get(parameters.size()-1))) {
                paramsString+=", ";
                valsString+=", ";
            }
            else {
                paramsString+=")";
                valsString+=")";
            }
        }

        String statement = header + paramsString + valsString + ";";
        return statement;
    }

    public static String modify(String tableName, List<Parameter> parameters, List<Condition> conditions){
        String header = MODIFY + tableName + " SET ";
        String paramsString = "";
        for(Parameter param : parameters){
            paramsString += param.getMyParamName() + " = " + param.getMyValue();
            if(!param.equals(parameters.get(parameters.size()-1))) {
                paramsString+=",";
            }
            paramsString+=" ";
        }
        String query = header + paramsString + buildConditional(conditions) + ";";
        return query;
    }

    public static String select(String tableName, List<Condition> conditions) {
        return conditionalQuery(SELECT_ALL, tableName, conditions);
    }

    public static String remove(String tableName, List<Condition> conditions) {
        return conditionalQuery(REMOVE, tableName, conditions);
    }

    //Helpers:

    private static String conditionalQuery(String action, String from, List<Condition> condition){
        String query = action + from + buildConditional(condition) + ";";
        return query;
    }

    private static String buildConditional(List<Condition> conditions) {
        String conditional = " ";
        for (int i = 0; i < conditions.size(); i++) {
            String header = " AND ";
            if (i == 0) header = " WHERE ";
            conditional += header + conditions.get(i).toString();
        }
        return conditional;
    }

}
