package Model.Data.API.Initialization;

import Model.Data.Lib.SQLStrings.TableNames;

public class LoginDBInit extends InitDBAPI {

    public LoginDBInit(String dbUsername, String dbPassword, String dbUrl, String dbFilename){
        super(dbUsername, dbPassword, dbUrl, dbFilename);
    }

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
