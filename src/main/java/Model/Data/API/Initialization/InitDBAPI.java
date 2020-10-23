package Model.Data.API.Initialization;

import Model.Data.API.DBAPI;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.Utils.DBUtils;
import Model.ErrorHandling.Exceptions.ServerExceptions.DBExceptions.EmptyDatabaseError;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.CreateTableException;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.RemoveTableException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class InitDBAPI extends DBAPI {

    public InitDBAPI() {
        super();
    }

    public String initialize() throws CreateTableException, EmptyDatabaseError, LoadPropertiesException {
        createTablesIfNull();
        return myDBRelFilename;
    }

    public abstract void createTablesIfNull() throws CreateTableException;

    public abstract void clearTables() throws RemoveTableException;

    protected void createTablesHelper(List<String> tableNames) throws CreateTableException {
        for (String table : tableNames) {
            Map<String, String> colNamesToType = ColumnInfo.getColumnMap(table);
            createTable(table, colNamesToType);
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

}

