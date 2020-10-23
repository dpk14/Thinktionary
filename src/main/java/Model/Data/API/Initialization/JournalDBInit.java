package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;

public class JournalDBInit extends InitDBAPI {

    public JournalDBInit() {super();}

    @Override
    public void createTablesIfNull() {
        createTablesHelper(TableNames.getJournalTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getJournalTableNames());
    }

}
