package BackEnd.DB.Lib.Paths;

import BackEnd.DB.Lib.SQLStrings.TableNames;

import java.util.HashMap;
import java.util.Map;

public class DBUrls {
    private static final String SQLITE = "jbdc:sqlite:";
    private static final String MAIN_DB_PATH = "myDB.db";

    private static String getConnectKey(String dbName){
        Map<String, String> nameToConnectKey = new HashMap<>();
        nameToConnectKey.put(DBNames.getSQLITE(), SQLITE);
        return String.valueOf(nameToConnectKey.get(dbName));
    }

    public static String getURL(String dbName) {
        String connectKey = getConnectKey(dbName);
        return connectKey + MAIN_DB_PATH;
    }
}
