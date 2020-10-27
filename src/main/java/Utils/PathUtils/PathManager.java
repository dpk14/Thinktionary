package Utils.PathUtils;

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
        String path = DATABASES_ROOT + File.separator + DBNames.getSQLITE() + File.separator + name;
        //System.out.println("db Relative Path: " + path);
        return path;
    }

    public static String getDBAbsPath(String name){
        String path = getResourcePath() + DATABASES_ROOT + File.separator + DBNames.getSQLITE() + File.separator + name;
        //System.out.println("db Abs Path: " + path);
        return path;
    }

    public static String getDBUrl(String dbRelPath){
        String path2 = DBUrls.getURL(DBNames.getSQLITE(), dbRelPath);
        //System.out.println("db url: " + path2);
        return path2;
    }

    public static String getDBAbsPathFromRel(String relPath){
        try {
            return getResourcePath() + File.separator + relPath;
        }
        catch(IllegalArgumentException e){
            System.out.println("WARNING: absolute path not valid in JAR file");
            return null;
        }
        //System.out.println("db Abs Path: " + path);
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

    private static String getResourcePath() throws IllegalArgumentException{
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
