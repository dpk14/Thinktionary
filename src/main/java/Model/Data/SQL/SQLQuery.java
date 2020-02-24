package Model.Data.SQL;

import java.util.Map;

public class SQLQuery {
    private static final String LOAD_TABLE = "SELECT * FROM ?;";
    private static final String LOAD_TABLE_BY_PARAMETER = "SELECT * FROM %s WHERE %s = ? ";
    private static final String GET_ENTRY = "SELECT * FROM %s " +
            "WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getTITLE() + " = ? " +
            "AND " + ColumnInfo.getTEXT() + " = ? " +
            "AND " + ColumnInfo.getCREATED() + " = ? " +
            "AND " + ColumnInfo.getMODIFIED() + " = ?;";

    private static final String GET_BY_ENTRY_ID = "SELECT * FROM ? " +
            "WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getEntryId() + " = ?;";

    private static final String ADD_ENTRY = "INSERT INTO %s " +
            "(" + ColumnInfo.getUSERID() + ", "
            + ColumnInfo.getTITLE() + ", "
            + ColumnInfo.getTEXT() + ", "
            + ColumnInfo.getCREATED() + ", "
            + ColumnInfo.getMODIFIED() + ") " +
            "VALUES (?,?,?,?,?);";

    private static final String ADD_TOPIC = "INSERT INTO %s " +
            "(" + ColumnInfo.getUSERID() + ", " +
            ColumnInfo.getTOPIC() + ", " +
            ColumnInfo.getCOLOR() + ") " +
            "VALUES (?,?,?);";

    private static final String REMOVE_GIVEN_USERID_ENTRY_ID = "DELETE FROM ? WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getEntryId() + " = ?;";

    private static final String MODIFY_ENTRY_INFO = "UPDATE " + TableNames.getEntryInfo() + " SET " +
            ColumnInfo.getUSERID() + " = ?, " +
            ColumnInfo.getTITLE() + " = ?, " +
            ColumnInfo.getTEXT() + " = ?, " +
            ColumnInfo.getCREATED() + " = ?, " +
            ColumnInfo.getMODIFIED() + " = ? " +
            "WHERE " + ColumnInfo.getEntryId() + " = ?;";

    private static final String LOAD_USER = "SELECT * FROM " + TableNames.getUserInfo() + " " +
            "WHERE " + ColumnInfo.getUSERNAME() + " = ? " +
            "AND " + ColumnInfo.getPASSWORD() + " = ?;";

    private static final String ADD_USER = "INSERT INTO " + TableNames.getUserInfo() +
            " (" + ColumnInfo.getUSERNAME() + ", " +
            ColumnInfo.getPASSWORD() + ") " +
            "VALUES (?,?);";

    private static final String REMOVE_TABLE = "DROP TABLE ?;";

    private static final String TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name=?;";
    private static final String ADD_TO_ENTRY_TOPIC = "INSERT INTO %s " +
            "(" + ColumnInfo.getEntryId() + ", " +
            ColumnInfo.getTOPIC() + ", " +
            ColumnInfo.getCOLOR() + ") " +
            "VALUES (?,?,?);";

    public static String addEntry() {
        return String.format(ADD_ENTRY, TableNames.getEntryInfo());
    }

    public static String addTopic() {
            System.out.println(ADD_TOPIC);
            return String.format(ADD_TOPIC, TableNames.getUserTopic());
    }

    public static String getEntry() {
        return String.format(GET_ENTRY, TableNames.getEntryInfo());
    }

    public static String remove() {
        return REMOVE_GIVEN_USERID_ENTRY_ID;
    }

    public static String modifyEntryInfo() {
        return MODIFY_ENTRY_INFO;
    }

    public static String loadTable() {
        return LOAD_TABLE;
    }

    public static String getLoadTableByParameter(String tableName, String parameterType) {
        String str = String.format(LOAD_TABLE_BY_PARAMETER, tableName, parameterType);
        System.out.println(str);
        return str;
    }

    public static String getUser() {
        return LOAD_USER;
    }

    public static String addUser() {
        return ADD_USER;
    }

    public static String getByEntryID() {
        return GET_BY_ENTRY_ID;
    }

    public static final String removeTable() {
        return REMOVE_TABLE;
    }

    public static final String tableExists() {return TABLE_EXISTS;}

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

    public static String addToEntryTopic() {
        return String.format(ADD_TO_ENTRY_TOPIC, TableNames.getEntryToTopic());
    }
}
