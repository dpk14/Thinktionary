package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;

public class LoginDBInit extends InitDBAPI {

    public LoginDBInit(){super();}

    @Override
    public void createTablesIfNull() {
        createTablesHelper(TableNames.getLoginTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getLoginTableNames());
    }

}
