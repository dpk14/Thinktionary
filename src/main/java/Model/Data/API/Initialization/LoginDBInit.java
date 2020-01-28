package Model.Data.API.Initialization;

import Model.Data.Lib.SQLStrings.TableNames;

public class LoginDBInit extends InitDBAPI {

    public LoginDBInit(){super();}

    @Override
    public void createTables() {
        createTablesHelper(TableNames.getJournalTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getJournalTableNames());
    }

}
