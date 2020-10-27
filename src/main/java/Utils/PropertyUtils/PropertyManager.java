package Utils.PropertyUtils;

public class PropertyManager {

    //Setters

    public static void setDBUsername(String value) {
        System.setProperty(PropertyKeys.getUserProp(), value);
    }

    public static void setDBPassword(String value) {
        System.setProperty(PropertyKeys.getPwdProp(), value);
    }

    public static void setURL(String value) {
        System.setProperty(PropertyKeys.getURLProp(), value);
    }

    //Getters

    public static String getDBUsername() {
        return System.getProperty(PropertyKeys.getUserProp());
    }

    public static String getDBPwd() {
        return System.getProperty(PropertyKeys.getPwdProp());
    }

    public static String getDBUrl() {
        return System.getProperty(PropertyKeys.getURLProp());
    }

}
