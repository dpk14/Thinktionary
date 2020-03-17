package Model.Utils.PropertyUtils;

public class PropertyKeys {
    private static final String DB_PROPERTIES_NAME = "/db.properties";
    private static final String USER_PROP = "db.user";
    private static final String PWD_PROP = "db.pwd";
    private static final String URL_PROP = "db.url";
    private static final String TESTMODE_PROP = "db.testmode";
    private static final String REL_FILENAME_PROP = "db.file.rel";
    private static final String ABS_FILENAME_PROP = "db.file.abs";
    private static final String FILENAME = "db.file.name";
    private static final String JAR_MODE_PROP = "jarMode";

    public static String getDbPropertiesName() {
        return DB_PROPERTIES_NAME;
    }

    public static String getUserProp() {
        return USER_PROP;
    }

    public static String getPwdProp() {
        return PWD_PROP;
    }

    public static String getTestmodeProp() {
        return TESTMODE_PROP;
    }

    public static String getRelFilepathProp() {
        return REL_FILENAME_PROP;
    }

    public static String getAbsFilepathProp() {
        return ABS_FILENAME_PROP;
    }

    public static String getFilenameProp() {
        return FILENAME;
    }

    public static String getURLProp() {
        return URL_PROP;
    }

    public static String getJarModeProp() {
        return JAR_MODE_PROP;
    }
}
