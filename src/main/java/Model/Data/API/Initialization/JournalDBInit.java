package Model.Data.API.Initialization;

import Model.Data.Lib.SQLStrings.TableNames;

public class JournalDBInit extends InitDBAPI {

    public JournalDBInit(String dbUsername, String dbPassword, String dbUrl, String dbFilename){
        super(dbUsername, dbPassword, dbUrl, dbFilename);
    }

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
