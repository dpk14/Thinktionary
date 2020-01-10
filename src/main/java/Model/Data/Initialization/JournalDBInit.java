package Model.Data.Initialization;

import Model.Data.Lib.SQLStrings.TableNames;

public class JournalDBInit extends DBInit{

    public JournalDBInit(String dbUsername, String dbPassword, String dbUrl){
        super(dbUsername, dbPassword, dbUrl);
    }

    @Override
    public void createTables() {
        createTablesHelper(TableNames.getLoginTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getLoginTableNames());
    }


}
