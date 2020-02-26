package Model.Data.API.Initialization;

import Model.Data.API.DBAPI;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InitDBAPI extends DBAPI {

    public InitDBAPI(){
        super();
    }

    public String initialize(){
        if(!DBexists(myDBAbsFilename)){
            createDatabase(myDBAbsFilename);
        }
        createTablesIfNull();
        return myDBAbsFilename.toString();
    }

    private boolean DBexists(String dBFilename){
        File file = new File(dBFilename);
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

    public abstract void createTablesIfNull();

    public abstract void clearTables();

    protected void createTablesHelper(List<String> tableNames) {
        for (String table : tableNames) {
            if(!tableExists(table)) {
                Map<String, String> colNamesToType = ColumnInfo.getColumnMap(table);
                createTable(table, colNamesToType);
            }
        }
    }

    protected void clearTablesHelper(List<String> tableNames) {
        for (String table : tableNames) {
            removeTable(table);
        }
    }

    private void createTable(String tableName, Map<String, String> columnMap) {
        String action = SQLQueryBuilder.createTable(tableName, columnMap);
        try {
            DBUtils.userAction(SQLQueryBuilder.createTable(tableName, columnMap), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            tryCatchUserAction(action);
        }
    }

    private void removeTable(String tableName){
        try {
            DBUtils.userAction(SQLQueryBuilder.removeTable(tableName), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e) {
        }
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

    protected boolean tableExists(String tableName){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        List<Map<String, Object>> ret = new ArrayList<>();
        try {
            ret = DBUtils.userQuery(map, SQLQueryBuilder.tableExists(), myDBUrl, myDBUsername, myDBPassword);
            return ret.size() != 0;
        } catch (SQLException e) {
            throw new CorruptDBError(e);
        }
    }

}

