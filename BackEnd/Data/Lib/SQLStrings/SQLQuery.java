package BackEnd.Data.Lib.SQLStrings;

import java.util.List;

public class SQLQuery {
    private static final String GET_TABLE = "SELECT * FROM ? WHERE userID = '?'";
    private static final String GET_ENTRY = "SELECT * FROM " + TableNames.getEntryInfo() + " " +
            "WHERE " + ColumnLabels.getUSERID() + " = ? " +
            "AND " + ColumnLabels.getTITLE() + " = ? " +
            "AND " + ColumnLabels.getTEXT() + " = ? " +
            "AND " + ColumnLabels.getCOLOR() + " = ? " +
            "AND " + ColumnLabels.getCREATED() + " = ? " +
            "AND " + ColumnLabels.getMODIFIED() + " = ?";

    private static final String GET_BY_ENTRY_ID = "SELECT * FROM ? " +
            "WHERE " + ColumnLabels.getUSERID() + " = ? " +
            "AND " + ColumnLabels.getEntryId() + " = ?";

    private static final String ADD_ENTRY = "INSERT INTO " + TableNames.getEntryInfo() + " " +
            "(" + ColumnLabels.getUSERID() + "," +
            "(" + ColumnLabels.getTITLE() + "," +
            "(" + ColumnLabels.getTEXT() + "," +
            "(" + ColumnLabels.getCOLOR() + "," +
            "(" + ColumnLabels.getCREATED() + ") " +
            "VALUES (?,?,?,?,?)";

    private static final String ADD_TOPIC = "INSERT INTO " + TableNames.getUserTopic() +
            "(" + ColumnLabels.getUSERID() + "," +
            "(" + ColumnLabels.getTOPIC() + "," +
            "(" + ColumnLabels.getCOLOR() + ") " +
            "VALUES (?,?,?)";

    private static final String REMOVE_GIVEN_USERID_ENTRY_ID = "DELETE FROM ? WHERE " + ColumnLabels.getUSERID() + " = ? " +
            "AND " + ColumnLabels.getEntryId() + " = ?";

    private static final String MODIFY_ENTRY_INFO = "UPDATE " + TableNames.getEntryInfo() + " SET " +
            ColumnLabels.getUSERID() + " = ?, " +
            ColumnLabels.getTITLE() + " = ?, " +
            ColumnLabels.getTEXT() + " = ?, " +
            ColumnLabels.getCOLOR() + " = ?, " +
            ColumnLabels.getCREATED() + " = ?, " +
            ColumnLabels.getMODIFIED() + " = ? " +
            "WHERE " + ColumnLabels.getEntryId() + " = ?";

    private static final String LOAD_USER = "SELECT * FROM " + TableNames.getUserInfo() + " " +
            "WHERE " + ColumnLabels.getUSERNAME() + " = ? " +
            "AND " + ColumnLabels.getPASSWORD() + " = ?";

    private static final String ADD_USER = "INSERT INTO " + TableNames.getUserTopic() +
            "(" + ColumnLabels.getUSERNAME() + "," +
            "(" + ColumnLabels.getPASSWORD() + ") " +
            "VALUES (?,?)";

    private static final String CLEAR_TABLE = "DELETE FROM ? *";

    public static String addEntry() {
        return ADD_ENTRY;
    }

    public static String addTopic() {
        return ADD_TOPIC;
    }

    public static String getEntry() {
        return GET_ENTRY;
    }

    public static String remove() {
        return REMOVE_GIVEN_USERID_ENTRY_ID;
    }

    public static String modifyEntryInfo() {
        return MODIFY_ENTRY_INFO;
    }

    public static String loadTable() {
        return GET_TABLE;
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

    public static final String clearTable() {
        return CLEAR_TABLE;
    }

    public static final String createTable(String tableName, List<String> columnNames) {
        String command = "CREATE TABLE " + tableName + " AS SELECT ";
        for (String columnName : columnNames) {
            command += columnName;
            if (!columnName.equals(columnNames.get(columnNames.size() - 1))) {
                command += " , ";
            }
        }
        return command;
    }
}
