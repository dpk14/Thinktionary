package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;

public class JournalDBInit extends InitDBAPI {

    public JournalDBInit(){super();}

    @Override
    public void createTables() {
        createTablesHelper(TableNames.getLoginTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getLoginTableNames());
    }


}
