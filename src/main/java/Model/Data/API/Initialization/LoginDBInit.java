package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.CreateTableException;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.RemoveTableException;

public class LoginDBInit extends InitDBAPI {

    public LoginDBInit() throws LoadPropertiesException {super();}

    @Override
    public void createTablesIfNull() throws CreateTableException {
        createTablesHelper(TableNames.getLoginTableNames());
    }

    @Override
    public void clearTables() throws RemoveTableException {
        clearTablesHelper(TableNames.getLoginTableNames());
    }

}
