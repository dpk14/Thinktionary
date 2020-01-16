package Model.API.Login;

import Model.Data.API.LoginDBAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;

import java.util.Map;
import java.util.List;

public class LoginAPI {

    public static int login(String username, String password) throws InvalidLoginException {
        List<Map<String, Object>> userInfo = new LoginDBAPI().login(username, password);
        return LoginDBParser.getUserID(userInfo);
    }

    public static void makeAccount(String username, String password) throws AccountExistsException { //second exception has two inheritances, password exists and username exists
        new LoginDBAPI().createAccount(username, password);
    }

    //Testing:

    public static Map<Integer, User> loadUserInfoMap(){
        List<Map<String, Object>> userInfoTable = new LoginDBAPI().loadUserInfoTable();
        return LoginDBParser.parseUserInfoMap(userInfoTable);
    }

}
