package BackEnd.Data.Lib.SQLStrings;

public class TableNames {
    private static final String ENTRY_INFO = "Entry Info";
    private static final String ENTRY_TOPIC = "Entry-Topic";
    private static final String USER_TOPIC = "User-Topic";
    private static final String USER_INFO = "User Info";

    public static String getUserTopic() {
        return USER_TOPIC;
    }

    public static String getEntryInfo()  {
        return ENTRY_INFO;
    }

    public static String getEntryToTopic()  {
        return ENTRY_TOPIC;
    }

    public static String getUserInfo() { return USER_INFO; }
}
