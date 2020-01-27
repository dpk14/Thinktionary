package Model.Data.API.Initialization;

import Model.Data.Lib.SQLStrings.TableNames;

public class JournalDBInit extends InitDBAPI {

    public JournalDBInit(String dbUsername, String dbPassword, String dbUrl, String dbFilename, boolean testMode){
        super(dbUsername, dbPassword, dbUrl, dbFilename, testMode);
    }

    public JournalDBInit(boolean testMode){super(testMode);}

    @Override
    public void createTables() {
        createTablesHelper(TableNames.getLoginTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getLoginTableNames());
    }


}
