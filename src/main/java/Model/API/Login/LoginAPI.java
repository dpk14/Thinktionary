package Model.API.Login;

import Model.API.Journal.Journal;
import Model.Data.API.Run.LoginDBAPI;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.InvalidLoginException;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;

import java.util.Map;
import java.util.List;

public class LoginAPI {

    public static Journal login(String username, String password) throws InvalidLoginException, LoadPropertiesException {
        List<Map<String, Object>> userInfo = LoginDBAPI.login(username, password);
        int userID = LoginDBParser.getUserID(userInfo);
        return new Journal(userID, username, password);
    }

    public static int makeAccount(String username, String password) throws AccountExistsException, LoadPropertiesException { //second exception has two inheritances, password exists and username exists
        List<Map<String, Object>> userInfo = LoginDBAPI.createUser(username, password);
        return LoginDBParser.getUserID(userInfo);
    }

    //Testing:

    public static Map<Integer, User> loadUserInfoMap() throws LoadPropertiesException {
        List<Map<String, Object>> userInfoTable = LoginDBAPI.loadUserInfoTable();
        return LoginDBParser.parseUserInfoMap(userInfoTable);
    }

}
