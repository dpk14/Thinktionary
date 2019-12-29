package Data.Lib;

public class SQLQuery {
    private static final String GET_TABLE= "SELECT * FROM ? WHERE userID = '?'";
    private static final String GET_ENTRY_ID = "SELECT * FROM " + TabelNames.getEntryInfo() + " " +
                                                "WHERE userID = '?' " +
                                                "WHERE Title = '?' " +
                                                "WHERE Title = '?' " +
                                                "WHERE Color = '?' " +
                                                "WHERE Created = '?' " +
                                                "WHERE Modified = '?'";
    private static final String ADD_ENTRY = " ";
    private static final String ADD_TOPIC = " ";
    private static final String REMOVE = " ";
    private static final String MODIFY_ENTRY = " ";



    public static String addEntry() { return ADD_ENTRY; }

    public static String addTopic() { return ADD_TOPIC; }

    public static String getEntryID() { return GET_ENTRY_ID; }

    public static String remove() { return REMOVE; }

    public static String modifyEntry() { return MODIFY_ENTRY; }

    public static String loadTable() { return GET_TABLE; }
}
