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

    public static String getDBRelPath(String name){
        return DATABASES_ROOT + "\\" + DBNames.getSQLITE() + "\\" + name;
    }

    public static String getDBAbsPath(String name){
        return getResourcePath() + DATABASES_ROOT + "\\" + DBNames.getSQLITE() + "\\" + name;
    }

    public static String getDBAbsPathFromRel(String relPath){
        return getResourcePath() + "\\" + relPath;
    }

    public static String getDBUrl(String path){
        return DBUrls.getURL(DBNames.getSQLITE(), path);
    }

    //Default getters

    public static String getDefaultTestName() {
        return MAIN_DB_NAME_DEFAULT;
    }

    public static String getDefaultMainName(){
        return TEST_DB_NAME_DEFAULT;
    }

    //Helpers

    private static String checkDBPath(String name){
        URL res = PathManager.class.getClassLoader().getClass().getResource(getDBRelPath(name));
        File file = null;
        try {
            file = Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            System.out.println("DB path could not be converted to URI");
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private static String getResourcePath() {
        URL url = PathManager.class.getClassLoader().getResource("EMPTY.txt");
        try {
            return new File(url.toURI()).getAbsolutePath().split("EMPTY.txt")[0];
        } catch (URISyntaxException e) {
            System.out.println("Sample resource filepath is incorrect");
            System.exit(1);
        }
        return null;
    }
}
