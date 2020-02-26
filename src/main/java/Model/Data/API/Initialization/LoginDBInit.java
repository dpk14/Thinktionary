package Model.Data.API.Initialization;

import Model.Data.SQL.TableNames;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.TableExceptions.CreateTableException;
import Model.ErrorHandling.Exceptions.TableExceptions.RemoveTableException;

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
