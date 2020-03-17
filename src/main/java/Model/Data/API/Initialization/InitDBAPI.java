package Model.Data.API.Initialization;

import Model.Data.API.DBAPI;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.DBExceptions.EmptyDatabaseError;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.TableExceptions.CreateTableException;
import Model.ErrorHandling.Exceptions.TableExceptions.RemoveTableException;
import Model.ConfigUtils.PropertyUtils.PropertyManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InitDBAPI extends DBAPI {

    public InitDBAPI() throws LoadPropertiesException {
        super();
    }

    public String initialize() throws CreateTableException, EmptyDatabaseError, LoadPropertiesException {
        if(!DBexists(myDBAbsFilename)){
            if(programIsJAR()){
                throw new EmptyDatabaseError();
            }
            createDatabase(myDBAbsFilename);
        }
        createTablesIfNull();
        return myDBAbsFilename.toString();
    }

    private boolean DBexists(String dbFilename){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(dbFilename);
        return is != null;
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

    public abstract void createTablesIfNull() throws CreateTableException;

    public abstract void clearTables() throws RemoveTableException;

    protected void createTablesHelper(List<String> tableNames) throws CreateTableException {
        for (String table : tableNames) {
            if(!tableExists(table)) {
                Map<String, String> colNamesToType = ColumnInfo.getColumnMap(table);
                createTable(table, colNamesToType);
            }
        }
    }

    protected void clearTablesHelper(List<String> tableNames) throws RemoveTableException {
        for (String table : tableNames) {
            removeTable(table);
        }
    }

    private void createTable(String tableName, Map<String, String> columnMap) throws CreateTableException {
        try {
            DBUtils.userAction(SQLQueryBuilder.createTable(tableName, columnMap), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new CreateTableException(e);
        }
    }

    private void removeTable(String tableName) throws RemoveTableException {
        try {
            DBUtils.userAction(SQLQueryBuilder.removeTable(tableName), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e) {
            throw new RemoveTableException(e);
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

    protected boolean programIsJAR() throws LoadPropertiesException {
        return PropertyManager.getJarMode();
    };
}

