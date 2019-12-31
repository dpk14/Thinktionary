package BackEnd.DB.Lib.SQLStrings;

public class SQLQuery {
    private static final String GET_TABLE = "SELECT * FROM ? WHERE userID = '?'";
    private static final String GET_ENTRY_ID = "SELECT * FROM " + TableNames.getEntryInfo() + " " +
                                                "WHERE " + ColumnLabels.getUSERID() + " = ? " +
                                                "AND " + ColumnLabels.getTITLE() + " = ? " +
                                                "AND " + ColumnLabels.getTEXT() + " = ? " +
                                                "AND " + ColumnLabels.getCOLOR() + " = ? " +
                                                "AND " + ColumnLabels.getCREATED() + " = ? " +
                                                "AND " + ColumnLabels.getMODIFIED()+ " = '?'";
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

    public static String addEntry() { return ADD_ENTRY; }

    public static String addTopic() { return ADD_TOPIC; }

    public static String getEntryID() { return GET_ENTRY_ID; }

    public static String remove() { return REMOVE_GIVEN_USERID_ENTRY_ID; }

    public static String modifyEntryInfo() { return MODIFY_ENTRY_INFO; }

    public static String loadTable() { return GET_TABLE; }
}
