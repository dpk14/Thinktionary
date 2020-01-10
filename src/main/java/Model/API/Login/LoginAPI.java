package Model.API.Login;

import Model.Data.API.LoginDBAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;

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
