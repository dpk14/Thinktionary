package Model.Data.SQL;

import java.util.HashMap;
import java.util.Map;

public class ColumnInfo {
    private static final String TOPIC = "topic";
    private static final String COLOR = "color";
    private static final String ENTRY_ID = "entryid";
    private static final String USER_ID = "userid";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String CREATED = "created";
    private static final String MODIFIED = "modified";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    public static final String AWS_ACCESS_KEY = "awsaccesskey";
    public static final String AWS_SECRET_KEY = "awssecretkey";
    private static final String EMAIL = "email";
    public static final String CONF_KEY = "confkey";

    private static final String TEXT_TYPE = "TEXT NOT NULL";
    private static final String EMAIL_FOR_CONF_KEY_TYPE = "TEXT PRIMARY KEY";
    private static final String INTEGER_TYPE = "INT";

    private static final String PRIMARY_KEY_LABEL = " PRIMARY KEY NOT NULL";


    public static String getCOLOR() {
        return COLOR;
    }

    public static String getEntryId() {
        return ENTRY_ID;
    }

    public static String getTOPIC() {
        return TOPIC;
    }

    public static String getTITLE() {
        return TITLE;
    }

    public static String getTEXT() {
        return TEXT;
    }

    public static String getMODIFIED() {
        return MODIFIED;
    }

    public static String getCREATED() {return CREATED;}

    public static String getUSERID() { return USER_ID;}

    public static String getUSERNAME() {return USERNAME;}

    public static String getPASSWORD() { return PASSWORD;}

    public static String getEMAIL() {
        return EMAIL;
    }

    public static String getEmailType() { return TEXT_TYPE; }

    public static String getColorType() {
        return TEXT_TYPE;
    }

    public static String getCreatedType() {
        return TEXT_TYPE;
    }

    public static String getEntryIdType(boolean primaryKey) {
        return INTEGER_TYPE + (primaryKey ? PRIMARY_KEY_LABEL : "");
    }

    public static String getUserIdType(boolean primaryKey) {
        return INTEGER_TYPE + (primaryKey ? PRIMARY_KEY_LABEL : "");
    }

    public static String getModifiedType() {
        return TEXT_TYPE;
    }

    public static String getPasswordType() {
        return TEXT_TYPE;
    }

    public static String getTextType() {
        return TEXT_TYPE;
    }

    public static String getTitleType() {
        return TEXT_TYPE;
    }

    public static String getTopicType() {
        return TEXT_TYPE;
    }

    public static String getUsernameType() {
        return TEXT_TYPE;
    }


    public static Map<String, String> getEntryTopicColumnMap(){
        Map<String, String> columnMap = new HashMap();
        columnMap.put(getEntryId(), getEntryIdType(false));
        columnMap.put(getUSERID(), getUserIdType(false));
        columnMap.put(getTOPIC(), getTopicType());
        columnMap.put(getCOLOR(), getColorType());
        return columnMap;
    }

    public static Map<String, String> getUserTopicColumnMap(){
        Map<String, String> columnMap = new HashMap();
        columnMap.put(getUSERID(), getUserIdType(false));
        columnMap.put(getTOPIC(), getTopicType());
        columnMap.put(getCOLOR(), getColorType());
        return columnMap;
    }

    public static Map<String, String> getEntryInfoColumnMap(){
        Map<String, String> columnMap = new HashMap();
        columnMap.put(getEntryId(), getEntryIdType(true));
        columnMap.put(getUSERID(), getUserIdType(false));
        columnMap.put(getTITLE(), getTitleType());
        columnMap.put(getTEXT(), getTextType());
        columnMap.put(getCREATED(), getCreatedType());
        columnMap.put(getMODIFIED(), getModifiedType());
        return columnMap;
    }

    public static Map<String, String> getUserInfoColumnMap(){
        Map<String, String> columnMap = new HashMap();
        columnMap.put(getUSERID(), getUserIdType(true));
        columnMap.put(getUSERNAME(), getUsernameType());
        columnMap.put(getPASSWORD(), getPasswordType());
        columnMap.put(EMAIL, TEXT_TYPE);
        return columnMap;
    }

    public static Map<String, String> getEmailVerifyColumnMap(){
        Map<String, String> columnMap = new HashMap();
        columnMap.put(EMAIL, EMAIL_FOR_CONF_KEY_TYPE);
        columnMap.put(CONF_KEY, INTEGER_TYPE);
        return columnMap;
    }


    public static Map<String, String> getColumnMap(String tableName){
        Map<String, Map<String, String>> tableToColumns = new HashMap();
        Map<String, String> columnToType = getEntryInfoColumnMap();
        tableToColumns.put(TableNames.getEntryInfo(), columnToType);
        columnToType = getUserTopicColumnMap();
        tableToColumns.put(TableNames.getUserTopic(), columnToType);
        columnToType = getEntryTopicColumnMap();
        tableToColumns.put(TableNames.getEntryToTopic(), columnToType);
        columnToType = getUserInfoColumnMap();
        tableToColumns.put(TableNames.getUserInfo(), columnToType);
        columnToType = getEmailVerifyColumnMap();
        tableToColumns.put(TableNames.getEmailConfirmation(), columnToType);
        return tableToColumns.get(tableName);
    }
    
}
