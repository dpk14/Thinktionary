package Model.API.Login;

import Model.API.Journal.Journal;
import Model.Data.API.Run.LoginDBAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;

import java.util.Map;
import java.util.List;

public class LoginAPI {

    public Journal login(String username, String password) throws InvalidLoginException {
        List<Map<String, Object>> userInfo = new LoginDBAPI().login(username, password);
        int userID = LoginDBParser.getUserID(userInfo);
        return new Journal(userID);
    }

    public static int makeAccount(String username, String password) throws AccountExistsException { //second exception has two inheritances, password exists and username exists
        List<Map<String, Object>> userInfo = new LoginDBAPI().createAccount(username, password);
        return LoginDBParser.getUserID(userInfo);
    }

    //Testing:

    public static Map<Integer, User> loadUserInfoMap(){
        List<Map<String, Object>> userInfoTable = new LoginDBAPI().loadUserInfoTable();
        return LoginDBParser.parseUserInfoMap(userInfoTable);
    }

}
