package BackEnd.Data.API;

import BackEnd.Data.Lib.SQLStrings.ColumnLabels;
import BackEnd.Data.Lib.SQLStrings.SQLQuery;
import BackEnd.Data.Lib.SQLStrings.TableNames;
import BackEnd.Data.Utils.DBUtils;
import jdk.javadoc.internal.doclets.formats.html.markup.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneralDBAPI {

    String myDBUrl;
    String myDBUsername;
    String myDBPassword;

    public GeneralDBAPI(String dbUrl, String dbUsername, String dbPassword){
        myDBUrl = dbUrl;
        myDBUsername = dbUsername;
        myDBPassword = dbPassword;
    }

    public void createTable(String tableName, List<String> columnNames){
        String action = SQLQuery.createTable(tableName, columnNames);
        tryCatchUserAction(action);
    }

    public void createEntryTopicTable(){
        List<String> columnNames = ColumnLabels.getEntryTopicColumnNames();
        String action = SQLQuery.createTable(TableNames.getEntryToTopic(), columnNames);
        tryCatchUserAction(action);
    }

    public void createUserTopicTable(){
        List<String> columnNames = ColumnLabels.getUserTopicColumnNames();
        String action = SQLQuery.createTable(TableNames.getUserTopic(), columnNames);
    }

    public void createEntryInfoTable(){
        List<String> columnNames = ColumnLabels.getEntryInfoColumnNames();
        String action = SQLQuery.createTable(TableNames.getEntryInfo(), columnNames);
        tryCatchUserAction(action);
    }

    public void createUserInfoTable(){
        List<String> columnNames = ColumnLabels.getUserInfoColumnNames();
        String action = SQLQuery.createTable(TableNames.getUserInfo(), columnNames);
        tryCatchUserAction(action);
    }

    public void removeTable(String tableName){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        String action = SQLQuery.clearTable();
        tryCatchUserAction(action);
    }

    private void tryCatchUserAction(String action){
        try {
            DBUtils.userAction(action, myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            System.out.println(e.toString());
            System.out.println(e.getStackTrace());
        }
    }




}
