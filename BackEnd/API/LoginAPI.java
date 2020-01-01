package BackEnd.API;

import BackEnd.API.Journal.JournalAPI;
import BackEnd.Data.API.LoginDBAPI;
import BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import BackEnd.ErrorHandling.Exceptions.InvalidLoginException;

public class LoginAPI {

    LoginDBAPI myLoginDBAPI;

    public LoginAPI(String dbUsername, String dbPassword){
        myLoginDBAPI = new LoginDBAPI(dbUsername, dbPassword);
    }

    public JournalAPI login(String username, String password) throws InvalidLoginException {
        int userID = myLoginDBAPI.login(username, password);
        return new JournalAPI(userID, username, password);

    }

    public void makeAccount(String username, String password) throws AccountExistsException { //second exception has two inheritances, password exists and username exists
        myLoginDBAPI.createAccount(username, password);
    }
}
