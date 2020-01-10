package src.main.java.Model.Data.Initialization;

import src.main.java.Model.Data.Lib.Paths.DBFileNames;
import src.main.java.Model.Data.Lib.Paths.DBNames;
import src.main.java.Model.Data.Lib.Paths.DBUrls;
import src.main.java.Model.Data.Lib.SQLStrings.ColumnInfo;
import src.main.java.Model.Data.Lib.SQLStrings.SQLQuery;
import src.main.java.Model.Data.Utils.DBUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DBInit {

    String myDBUrl;
    String myDBUsername;
    String myDBPassword;

    public DBInit(String dbUsername, String dbPassword, String dbUrl) {
        myDBUsername = dbUsername;
        myDBPassword = dbPassword;
        if (dbUrl == null) {
            myDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());
        } else myDBUrl = dbUrl;

        if(!DBexists(dbUrl)){
            createDatabase(dbUrl);
            createTables();
        }
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

