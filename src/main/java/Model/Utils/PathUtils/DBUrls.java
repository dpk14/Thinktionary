package Model.Utils.PathUtils;

import java.util.HashMap;
import java.util.Map;

public class DBUrls {
    private static final String SQLITE = "jdbc:sqlite::resource:";

    private static String getConnectKey(String dbName){
        Map<String, String> nameToConnectKey = new HashMap<>();
        nameToConnectKey.put(DBNames.getSQLITE(), SQLITE);
        return String.valueOf(nameToConnectKey.get(dbName));
    }

    public static String getURL(String dbName, String absolutePath) {
        String connectKey = getConnectKey(dbName);
        return connectKey + absolutePath;
    }
}
