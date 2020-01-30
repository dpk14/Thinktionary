package Model.Utils.PathUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class PathManager {

    private static final String MAIN_DB_NAME_DEFAULT = "myDB.db";
    private static final String TEST_DB_NAME_DEFAULT = "testDB.db";
    private static final String DATABASES_ROOT = "Databases";

    public static String getMainDbName() {
        return MAIN_DB_NAME_DEFAULT;
    }
    public static String getTestDbName() {
        return TEST_DB_NAME_DEFAULT;
    }

    public static String getDBPath(String dbType, String name){
        return DATABASES_ROOT + "/" + dbType + "/" + name;
    }

    public static String getDBUrl(String dbType, String name){
        return DBUrls.getURL(DBNames.getSQLITE(), name);
    }

    public static String getDefaultTestDbPath(String dbType) {
        return getDBPath(dbType, TEST_DB_NAME_DEFAULT);
    }

    public static String getDefaultMainDbPath(String dbType) {
        return getDBPath(dbType, MAIN_DB_NAME_DEFAULT);
    }

    private static String checkDBPath(String dbType, String name){
        URL res = PathManager.class.getClassLoader().getClass().getResource(getDBPath(dbType, name));
        File file = null;
        try {
            file = Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            System.out.println("DB path could not be converted to URI");
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
