package Model.Data.SQL;

import java.util.ArrayList;
import java.util.List;

public class TableNames {
    private static final String ENTRY_INFO = "EntryInfo";
    private static final String ENTRY_TOPIC = "EntryTopic";
    private static final String USER_TOPIC = "UserTopic";
    private static final String USER_INFO = "UserInfo";
    private static final String EMAIL_CONFIRMATION = "EmailConfirmation";

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

    public static String getEmailConfirmation() {
        return EMAIL_CONFIRMATION;
    }

    public static List<String> getTableNames() {
        List<String> ret = new ArrayList();
        ret.add(getEntryInfo());
        ret.add(getEntryToTopic());
        ret.add(getUserInfo());
        ret.add(getUserTopic());
        return ret;
    }

    public static List<String> getJournalTableNames() {
        List<String> tableNames = new ArrayList();
        tableNames.add(TableNames.getEntryInfo());
        tableNames.add(TableNames.getEntryToTopic());
        tableNames.add(TableNames.getUserTopic());
        return tableNames;
    }

    public static List<String> getLoginTableNames() {
        List<String> tableNames = new ArrayList();
        tableNames.add(TableNames.getUserInfo());
        return tableNames;
    }

}
