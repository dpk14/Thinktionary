package BackEnd.Data.Lib.SQLStrings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColumnLabels {
    private static final String TOPIC = "Topic";
    private static final String COLOR = "Color";
    private static final String ENTRY_ID = "Entry ID";
    private static final String USER_ID = "User ID";
    private static final String TITLE = "Title";
    private static final String TEXT = "Text";
    private static final String CREATED = "Created";
    private static final String MODIFIED = "Modified";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";


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

    public static String getUSERID() {return String.valueOf(USER_ID); }

    public static String getUSERNAME() {return String.valueOf(USERNAME);}

    public static String getPASSWORD() { return String.valueOf(PASSWORD);}

    public static List<String> getEntryTopicColumnNames(){
        List<String> columnNames = new ArrayList();
        columnNames.add(ColumnLabels.getEntryId());
        columnNames.add(ColumnLabels.getTOPIC());
        columnNames.add(ColumnLabels.getCOLOR());
        return columnNames;
    }

    public static List<String> getUserTopicColumnNames(){
        List<String> columnNames = new ArrayList();
        columnNames.add(ColumnLabels.getUSERID());
        columnNames.add(ColumnLabels.getTOPIC());
        columnNames.add(ColumnLabels.getCOLOR());
        return columnNames;
    }

    public static List<String> getEntryInfoColumnNames(){
        List<String> columnNames = new ArrayList();
        columnNames.add(ColumnLabels.getEntryId());
        columnNames.add(ColumnLabels.getUSERID());
        columnNames.add(ColumnLabels.getTITLE());
        columnNames.add(ColumnLabels.getTEXT());
        columnNames.add(ColumnLabels.getCREATED());
        columnNames.add(ColumnLabels.getMODIFIED());
        return columnNames;
    }

    public static List<String> getUserInfoColumnNames(){
        List<String> columnNames = new ArrayList();
        columnNames.add(ColumnLabels.getUSERID());
        columnNames.add(ColumnLabels.getUSERNAME());
        columnNames.add(ColumnLabels.getPASSWORD());
        return columnNames;
    }

    public static List<String> getTableColumnNames(String tableName){
        Map<String, List<String>> tableToColumns = new HashMap();
        tableToColumns.put(TableNames.getEntryInfo(), ColumnLabels.getEntryInfoColumnNames());
        tableToColumns.put(TableNames.getUserTopic(), ColumnLabels.getUserTopicColumnNames());
        tableToColumns.put(TableNames.getEntryToTopic(), ColumnLabels.getEntryTopicColumnNames());
        tableToColumns.put(TableNames.getUserInfo(), ColumnLabels.getUserInfoColumnNames());
        return tableToColumns.get(tableName);
    }
}
