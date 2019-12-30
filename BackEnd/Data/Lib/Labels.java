package BackEnd.Data.Lib;

public class Labels {
    private static final String TOPIC = "Topic";
    private static final String COLOR = "Color";
    private static final String ENTRY_ID = "Entry ID";
    private static final String USER_ID = "User ID";
    private static final String TITLE = "Title";
    private static final String TEXT = "Text";
    private static final String CREATED = "Created";
    private static final String MODIFIED = "Modified";

    public static String getCOLOR() {
        return String.valueOf(COLOR);
    }

    public static String getEntryId() {
        return String.valueOf(ENTRY_ID);
    }

    public static String getTOPIC() {
        return String.valueOf(TOPIC);
    }

    public static String getTITLE() {
        return String.valueOf(TITLE);
    }

    public static String getTEXT() {
        return String.valueOf(TEXT);
    }

    public static String getMODIFIED() {
        return String.valueOf(MODIFIED);
    }

    public static String getCREATED() {return String.valueOf(CREATED);}

    public static String getUSERID() {return String.valueOf(USER_ID); }

}
