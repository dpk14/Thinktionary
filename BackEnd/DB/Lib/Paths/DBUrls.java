package BackEnd.DB.Lib.Paths;

import BackEnd.DB.Lib.SQLStrings.TableNames;

import java.util.HashMap;
import java.util.Map;

public class DBUrls {
    private static final String ENTRY_INFO = "entry_info.db";
    private static final String ENTRY_TO_TOPIC = "entry-topic.db";
    private static final String LOGIN_INFO = "login_info.db";
    private static final String USER_TO_TOPIC = "user-topic.db";
    private static final String SQLITE = "jbdc:sqlite:";

    private static String getConnectKey(String dbName){
        Map<String, String> nameToConnectKey = new HashMap<>();
        nameToConnectKey.put(DBNames.getSQLITE(), SQLITE);
        return String.valueOf(nameToConnectKey.get(dbName));
    }

    public static String getURL(String dbName, String tableName) {
        Map<String, String> tableNameToPath = new HashMap<>();
        tableNameToPath.put(TableNames.getEntryInfo(), ENTRY_INFO);
        tableNameToPath.put(TableNames.getEntryToTopic(), ENTRY_TO_TOPIC);
        tableNameToPath.put(TableNames.getUserTopic(), USER_TO_TOPIC);
        String DBpath = tableNameToPath.get(tableName));
        return getConnectKey(dbName) + DBpath;
    }
}
