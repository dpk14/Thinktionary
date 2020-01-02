package src.main.java.BackEnd.API.Login;

import src.main.java.BackEnd.API.Journal.JournalAPI;
import src.main.java.BackEnd.Data.API.LoginDBAPI;
import src.main.java.BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import src.main.java.BackEnd.ErrorHandling.Exceptions.InvalidLoginException;

import java.util.Map;
import java.util.List;

public class LoginAPI {

    LoginDBAPI myLoginDBAPI;

    public LoginAPI(String dbUsername, String dbPassword, String dbUrl){
        myLoginDBAPI = new LoginDBAPI(dbUsername, dbPassword, dbUrl);
    }

    public int login(String username, String password) throws InvalidLoginException {
        List<Map<String, Object>> userInfo = myLoginDBAPI.login(username, password);
        return LoginDBParser.getUserID(userInfo);
    }

    public void makeAccount(String username, String password) throws AccountExistsException { //second exception has two inheritances, password exists and username exists
        myLoginDBAPI.createAccount(username, password);
    }

    public LoginDBAPI getMyLoginDBAPI(){
        return myLoginDBAPI;
    }

    public Map<Integer, User> loadUserInfoMap(){
        List<Map<String, Object>> userInfoTable = myLoginDBAPI.loadUserInfoTable();
        return LoginDBParser.parseUserInfoMap(userInfoTable);
    }
}
