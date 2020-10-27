package Utils.PropertyUtils;

public class PropertyKeys {
    private static final String DB_PROPERTIES_NAME = "/db.properties";
    private static final String USER_PROP = "db.user";
    private static final String PWD_PROP = "db.pwd";
    private static final String URL_PROP = "db.url";

    public static String getDbPropertiesName() {
        return DB_PROPERTIES_NAME;
    }

    public static String getUserProp() {
        return USER_PROP;
    }

    public static String getPwdProp() {
        return PWD_PROP;
    }

    public static String getURLProp() {
        return URL_PROP;
    }

}
