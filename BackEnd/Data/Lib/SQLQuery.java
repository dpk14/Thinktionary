package BackEnd.Data.Lib;

public class SQLQuery {
    private static final String GET_TABLE= "SELECT * FROM ? WHERE userID = '?'";
    private static final String GET_ENTRY_ID = "SELECT * FROM " + TableNames.getEntryInfo() + " " +
                                                "WHERE " + Labels.getUSERID() + " = ? " +
                                                "AND " + Labels.getTITLE() + " = ? " +
                                                "AND " + Labels.getTEXT() + " = ? " +
                                                "AND " + Labels.getCOLOR() + " = ? " +
                                                "AND " + Labels.getCREATED() + " = ? " +
                                                "AND " + Labels.getMODIFIED()+ " = '?'";
    private static final String ADD_ENTRY = "INSERT INTO " + TableNames.getEntryInfo() + " " +
                                            "(" + Labels.getUSERID() + "," +
                                            "(" + Labels.getTITLE() + "," +
                                            "(" + Labels.getTEXT() + "," +
                                            "(" + Labels.getCOLOR() + "," +
                                            "(" + Labels.getCREATED() + ") " +
                                            "VALUES (?,?,?,?,?)";
    private static final String ADD_TOPIC = "INSERT INTO " + TableNames.getUserTopic() +
                                            "(" + Labels.getUSERID() + "," +
                                            "(" + Labels.getTOPIC() + "," +
                                            "(" + Labels.getCOLOR() + ") " +
                                            "VALUES (?,?,?)";
    private static final String REMOVE_GIVEN_USERID_ENTRY_ID = "DELETE FROM ? WHERE " + Labels.getUSERID() + " = ? " +
                                                                             "AND " + Labels.getEntryId() + " = ?";

    private static final String MODIFY_ENTRY = " ";



    public static String addEntry() { return ADD_ENTRY; }

    public static String addTopic() { return ADD_TOPIC; }

    public static String getEntryID() { return GET_ENTRY_ID; }

    public static String remove() { return REMOVE_GIVEN_USERID_ENTRY_ID; }

    public static String modifyEntry() { return MODIFY_ENTRY; }

    public static String loadTable() { return GET_TABLE; }
}
