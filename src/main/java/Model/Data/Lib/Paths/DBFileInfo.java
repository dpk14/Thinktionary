package Model.Data.Lib.Paths;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class DBFileInfo {
    private static final String MAIN_DB_NAME = "myDB.db";
    private static final String TEST_DB_NAME = "testDB.db";
    private static final String DATABASES_ROOT = "Databases";

    public static String getMainDbName() {
        return String.valueOf(MAIN_DB_NAME);
    }
    public static String getTestDbName() {
        return String.valueOf(TEST_DB_NAME);
    }

    public String getTestDbPath(String dbType) {
        return getDBPath(dbType, TEST_DB_NAME);
    }

    public String getMainDbPath(String dbType) {
        return getDBPath(dbType, MAIN_DB_NAME);
    }

    private String getDBPath(String dbType, String name){
        URL res = getClass().getClassLoader().getResource(DATABASES_ROOT + "/" + dbType + "/" + name);
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
