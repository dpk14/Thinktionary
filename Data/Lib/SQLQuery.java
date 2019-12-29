package Data.Lib;

public class SQLQuery {
    private static final String GET_ENTRY_MAP = "SELECT Entries WITH userId = ?";
    private static final String GET_TOPICS = "SELECT  ";
    private static final String GET_ENTRY_TO_TOPIC = "SELECT ";
    private static final String GET_ENTRY_ID = " ";
    private static final String ADD_ENTRY = " ";
    private static final String ADD_TOPIC = " ";
    private static final String REMOVE = " ";
    private static final String MODIFY_ENTRY = " ";


    public static String getEntryMap() {
        return GET_ENTRY_MAP;
    }

    public static String getTopics() {
        return GET_TOPICS;
    }

    public static String getEntryToTopic() { return GET_ENTRY_TO_TOPIC; }

    public static String addEntry() { return ADD_ENTRY; }

    public static String addTopic() { return ADD_TOPIC; }

    public static String getEntryID() { return GET_ENTRY_ID; }

    public static String remove() { return REMOVE; }

    public static String modifyEntry() { return MODIFY_ENTRY; }
}
