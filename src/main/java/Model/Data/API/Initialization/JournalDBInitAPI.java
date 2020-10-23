package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;

public class JournalDBInitAPI extends InitDBAPI {

    public JournalDBInitAPI() {super();}

    @Override
    public void createTablesIfDoNotExist() {
        createTablesHelper(TableNames.getJournalTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getJournalTableNames());
    }

}
