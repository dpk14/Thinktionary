package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;

public class LoginDBInitAPI extends InitDBAPI {

    public LoginDBInitAPI() {super();}

    @Override
    public void createTablesIfDoNotExist() {
        createTablesHelper(TableNames.getLoginTableNames());
    }

    @Override
    public void clearTables() {
        clearTablesHelper(TableNames.getLoginTableNames());
    }

}
