package Model.Data.API.Initialization;

import Model.Data.API.DBAPI;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.Queries.CreateTable;
import Model.Data.SQL.Queries.RemoveTable;
import Utils.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.CreateTableException;
import Utils.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.RemoveTableException;

import java.util.List;
import java.util.Map;

public abstract class InitDBAPI extends DBAPI {

    public InitDBAPI() {
        super();
    }

    public abstract void createTablesIfDoNotExist() throws CreateTableException;

    public abstract void clearTables() throws RemoveTableException;

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

    private void createTable(String tableName, Map<String, String> columnMap) {
        userAction(new CreateTable(tableName, columnMap));
    }

    private void removeTable(String tableName) {
        userAction(new RemoveTable(tableName));
    }

}

