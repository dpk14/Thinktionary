package Model.Data.Lib.PropertiesManager;

public class PropertyKeys {
    private static String USER_PROP = "db.user";
    private static String PWD_PROP = "db.pwd";
    private static String DB_PROPERTIES_NAME = "db.properties";
    private static String TESTMODE_PROP = "db.testmode";

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
}
