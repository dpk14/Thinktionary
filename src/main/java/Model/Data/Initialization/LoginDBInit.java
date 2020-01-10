package src.main.java.Model.Data.Initialization;

import src.main.java.Model.Data.Lib.SQLStrings.TableNames;

public class LoginDBInit extends DBInit{

    public LoginDBInit(String dbUsername, String dbPassword, String dbUrl){
        super(dbUsername, dbPassword, dbUrl);
    }

    @Override
    public void createTables() {
        createTablesHelper(TableNames.getJournalTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getJournalTableNames());
    }

}
