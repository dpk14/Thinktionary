package BackEnd.API.Login;

import BackEnd.API.Journal.Journal;
import BackEnd.Data.API.LoginDBAPI;
import BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import BackEnd.ErrorHandling.Exceptions.InvalidLoginException;
import sun.rmi.runtime.Log;

import java.sql.SQLException;
import java.util.HashMap;

public class Main {

    LoginDBAPI myLoginDBAPI;

    public Main(String dbUsername, String dbPassword){
        myLoginDBAPI = new LoginDBAPI(dbUsername, dbPassword);
    }

    public Journal login(String username, String password) throws InvalidLoginException {
        int userID = myLoginDBAPI.login(username, password);
        return new Journal(userID, username, password);

    }

    public void makeAccount(String username, String password) throws AccountExistsException { //second exception has two inheritances, password exists and username exists
        myLoginDBAPI.createAccount(username, password);
    }
}
