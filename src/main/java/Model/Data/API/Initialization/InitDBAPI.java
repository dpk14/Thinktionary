package Model.Data.API.Initialization;

import Model.Data.API.DBAPI;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.SQLQuery;
import Model.Data.Utils.DBUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InitDBAPI extends DBAPI {

    String myDBUrl;
    String myDBUsername;
    String myDBPassword;
    String myDBFilename;

    public InitDBAPI(){
        super();
    }

    public String initialize(){
        if(!DBexists(myDBFilename)){
            createDatabase(myDBFilename);
            createTables();
        }
        return myDBFilename.toString();
    }

    private boolean DBexists(String dbUrl){
        File file = new File(dbUrl);
        return file.exists();
    }

    private void createDatabase(String url){
        File file = new File(url);
        try {
            file.createNewFile();
        }
        catch(IOException e){
            System.out.println(e.getStackTrace());
        }
    }

    public abstract void createTables();

    public abstract void clearTables();

    protected void createTablesHelper(List<String> tableNames) {
        for (String table : tableNames) {
            Map<String, String> colNamesToType = ColumnInfo.getColumnMap(table);
            createTable(table, colNamesToType);
        }
    }

    protected void clearTablesHelper(List<String> tableNames) {
        for (String table : tableNames) {
            removeTable(table);
        }
    }

    private void createTable(String tableName, Map<String, String> columnMap){
        String action = SQLQuery.createTable(tableName, columnMap);
        tryCatchUserAction(action);
    }

    private void removeTable(String tableName){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        String action = SQLQuery.removeTable();
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

