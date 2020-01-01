package BackEnd.Data.Lib.SQLStrings;

import java.util.ArrayList;
import java.util.List;

public class TableNames {
    private static final String ENTRY_INFO = "Entry Info";
    private static final String ENTRY_TOPIC = "Entry-Topic";
    private static final String USER_TOPIC = "User-Topic";
    private static final String USER_INFO = "User Info";

    public static String getUserTopic() {
        return String.valueOf(USER_TOPIC);
    }

    public static String getEntryInfo()  {
        return String.valueOf(ENTRY_INFO);
    }

    public static String getEntryToTopic()  {
        return String.valueOf(ENTRY_TOPIC);
    }

    public static String getUserInfo() { return String.valueOf(USER_INFO); }

    public static List<String> getTableNames() {
        List<String> ret = new ArrayList();
        ret.add(getEntryInfo());
        ret.add(getEntryToTopic());
        ret.add(getUserInfo());
        ret.add(getUserTopic());
        return ret;
    }
}
