package BackEnd.Data.Lib.Paths;

import java.util.HashMap;
import java.util.Map;

public class DBUrls {
    private static final String SQLITE = "jbdc:sqlite:";
    private static final String MAIN_DB_PATH = "myDB.db";
    private static final String TEST_DB_PATH = "testDB.db";

    private static String getConnectKey(String dbName){
        Map<String, String> nameToConnectKey = new HashMap<>();
        nameToConnectKey.put(DBNames.getSQLITE(), SQLITE);
        return String.valueOf(nameToConnectKey.get(dbName));
    }

    public static String getURL(String dbName, String fileName) {
        String connectKey = getConnectKey(dbName);
        return connectKey + fileName;
    }
}
