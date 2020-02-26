package Model.Data.SQL;
import java.util.Map;

public class SQLQuery {
    private static final String LOAD_TABLE = "SELECT * FROM ?;";
    private static final String LOAD_TABLE_BY_PARAMETER = "SELECT * FROM %s WHERE %s = ? ";
    private static final String GET_LAST_INSERT = "SELECT last_insert_rowid();";
    private static final String GET_ENTRY = "SELECT * FROM %s " +
            "WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getTITLE() + " = ? " +
            "AND " + ColumnInfo.getTEXT() + " = ? " +
            "AND " + ColumnInfo.getCREATED() + " = ? " +
            "AND " + ColumnInfo.getMODIFIED() + " = ?;";

    private static final String GET_BY_ENTRY_ID = "SELECT * FROM %s " +
            "WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getEntryId() + " = ?;";

    private static final String GET_ENTRY_BY_TOPIC = "SELECT * FROM %s " +
            "WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getTOPIC() + " = ?;";

    private static final String ADD_ENTRY = "INSERT INTO %s " +
            "(" + ColumnInfo.getUSERID() + ", "
            + ColumnInfo.getTITLE() + ", "
            + ColumnInfo.getTEXT() + ", "
            + ColumnInfo.getCREATED() + ", "
            + ColumnInfo.getMODIFIED() + ") " +
            "VALUES (?,?,?,?,?); " + GET_LAST_INSERT;

    private static final String ADD_TOPIC = "INSERT INTO %s " +
            "(" + ColumnInfo.getUSERID() + ", " +
            ColumnInfo.getTOPIC() + ", " +
            ColumnInfo.getCOLOR() + ") " +
            "VALUES (?,?,?);";

    private static final String REMOVE_GIVEN_USERID_ENTRY_ID = "DELETE FROM %s WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getEntryId() + " = ? ;";

    private static final String MODIFY_ENTRY_INFO = "UPDATE %s SET " +
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
            ColumnInfo.getUSERID() + ", " +
            ColumnInfo.getTOPIC() + ", " +
            ColumnInfo.getCOLOR() + ") " +
            "VALUES (?,?,?,?);";

    private static final String REMOVE_TOPIC_FROM_BANK = "DELETE FROM " + TableNames.getUserTopic() +
        " WHERE " + ColumnInfo.getUSERID() + " = ? " + "AND " + ColumnInfo.getTOPIC() + " = ? ;";

    private static final String REMOVE_TOPIC_FROM_ENTRY = "DELETE FROM " + TableNames.getEntryToTopic() +
            " WHERE " + ColumnInfo.getUSERID() + " = ? " +
            "AND " + ColumnInfo.getEntryId() + " = ? " +
            "AND " + ColumnInfo.getTOPIC() + " = ? ";


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

    public static String remove(String tableName) {
        String str = String.format(REMOVE_GIVEN_USERID_ENTRY_ID, tableName);
        System.out.println(str);
        return str;
    }

    public static String modifyEntryInfo() {
        return String.format(MODIFY_ENTRY_INFO, TableNames.getEntryInfo());
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

    public static String getByEntryID(String tableName) {
        return String.format(GET_BY_ENTRY_ID, tableName);
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

    public static String getLastInsertID() {
        return GET_LAST_INSERT;
    }

    public static String getEntryByTopic() {
        return String.format(GET_ENTRY_BY_TOPIC, TableNames.getEntryToTopic());
    }

    public static String removeTopicFromBank() {
        return REMOVE_TOPIC_FROM_BANK;
    }

    public static String removeTopicFromEntry() {
        return REMOVE_TOPIC_FROM_ENTRY;
    }
}
