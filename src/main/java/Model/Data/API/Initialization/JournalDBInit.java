package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.CreateTableException;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.RemoveTableException;

public class JournalDBInit extends InitDBAPI {

    public JournalDBInit() {super();}

    @Override
    public void createTablesIfNull() throws CreateTableException {
        createTablesHelper(TableNames.getJournalTableNames());
    }

    @Override
    public void clearTables() throws RemoveTableException {
        clearTablesHelper(TableNames.getJournalTableNames());
    }

}
