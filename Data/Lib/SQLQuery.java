package Data.Lib;

public class SQLQuery {
    private static final String GET_ENTRY_MAP = "SELECT Entries WITH userId = ?";
    private static final String GET_TOPICS = "SELECT";

    public static String getEntryMap() {
        return GET_ENTRY_MAP;
    }

    public static String getTopics() {
        return GET_TOPICS;
    }
}
