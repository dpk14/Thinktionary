package Model.Utils.PropertyUtils;

public class PropertyKeys {
    private static final String USER_PROP = "db.user";
    private static final String PWD_PROP = "db.pwd";
    private static final String DB_PROPERTIES_NAME = "db.properties";
    private static final String TESTMODE_PROP = "db.testmode";
    private static final String REL_FILENAME_PROP = "db.file.rel";
    private static final String ABS_FILENAME_PROP = "db.file.abs";

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

    public static String getRelFilenameProp() {
        return REL_FILENAME_PROP;
    }

    public static String getAbsFilenameProp() {
        return ABS_FILENAME_PROP;
    }
}
