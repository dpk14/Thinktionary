package Model.Data.SQL;

import java.util.HashMap;
import java.util.Map;

public class ColumnInfo {
    private static final String TOPIC = "Topic";
    private static final String COLOR = "Color";
    private static final String ENTRY_ID = "EntryID";
    private static final String USER_ID = "UserID";
    private static final String TITLE = "Title";
    private static final String TEXT = "Text";
    private static final String CREATED = "Created";
    private static final String MODIFIED = "Modified";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";

    private static final String TOPIC_TYPE = "text";
    private static final String COLOR_TYPE = "text";
    private static final String ENTRY_ID_TYPE = "int";
    private static final String USER_ID_TYPE = "int";
    private static final String TITLE_TYPE = "text";
    private static final String TEXT_TYPE = "text";
    private static final String CREATED_TYPE = "text";
    private static final String MODIFIED_TYPE = "text";
    private static final String USERNAME_TYPE = "text";
    private static final String PASSWORD_TYPE = "text";

    private static final String PRIMARY_KEY_LABEL = " PRIMARY KEY";


    public static String getCOLOR() {
        return String.valueOf(COLOR);
    }

    public static String getEntryId() {
        return String.valueOf(ENTRY_ID);
    }

    public static String getTOPIC() {
        return String.valueOf(TOPIC);
    }

    public static String getTITLE() {
        return String.valueOf(TITLE);
    }

    public static String getTEXT() {
        return String.valueOf(TEXT);
    }

    public static String getMODIFIED() {
        return String.valueOf(MODIFIED);
    }

    public static String getCREATED() {return String.valueOf(CREATED);}

    public static String getUSERID() { return String.valueOf(USER_ID);}

    public static String getUSERNAME() {return String.valueOf(USERNAME);}

    public static String getPASSWORD() { return String.valueOf(PASSWORD);}

    public static String getColorType() {
        return COLOR_TYPE;
    }

    public static String getCreatedType() {
        return CREATED_TYPE;
    }

    public static String getEntryIdType(boolean primaryKey) {
        return String.valueOf(ENTRY_ID_TYPE) + (primaryKey ? PRIMARY_KEY_LABEL : "");
    }

    public static String getUserIdType(boolean primaryKey) {
        return String.valueOf(USER_ID_TYPE) + (primaryKey ? PRIMARY_KEY_LABEL : "");
    }

    public static String getModifiedType() {
        return MODIFIED_TYPE;
    }

    public static String getPasswordType() {
        return PASSWORD_TYPE;
    }

    public static String getTextType() {
        return TEXT_TYPE;
    }

    public static String getTitleType() {
        return TITLE_TYPE;
    }

    public static String getTopicType() {
        return TOPIC_TYPE;
    }

    public static String getUsernameType() {
        return USERNAME_TYPE;
    }


    public static Map<String, String> getEntryTopicColumnMap(){
        Map<String, String> columnMap = new HashMap();
        columnMap.put(getEntryId(), getEntryIdType(false));
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
        return tableToColumns.get(tableName);
    }

}
