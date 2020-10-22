import Model.API.Login.LoginAPI;
import Model.API.Login.LoginDBParser;
import Model.API.Login.User;
import Model.Data.API.Run.LoginDBAPI;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.InvalidLoginException;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest {

    protected static void test(LoginAPI loginAPI) throws LoadPropertiesException {
        String username = "dpk14";
        String password = "10gg40w716";
        CreateAccountTest(username, password, loginAPI);
        IncorrectLoginTest(loginAPI);
        CorrectLoginTest(username, password, loginAPI);

    }

    protected static void test(LoginDBAPI loginDBAPI) throws LoadPropertiesException {
        String username = "dpk14";
        String password = "10gg40w716";
        CreateAccountTest(username, password);
        IncorrectLoginTest(loginDBAPI);
        CorrectLoginTest(username, password, loginDBAPI);
    }

    //API Tests

    @Test
    private static void CreateAccountTest(String username, String password, LoginAPI loginAPI) throws LoadPropertiesException {
        try {
            loginAPI.makeAccount(username, password);
        }
        catch(Exception e){
            assertFalse(e instanceof AccountExistsException, "AccountExistsException incorrectly thrown");
        }

        Map<Integer, User> users = loginAPI.loadUserInfoMap();
        assertFalse(users.size()== 0 , "Account not getting saved after creation");
        assertFalse(users.size()> 1 , "DB has too many account after just one account is added");
    }

    @Test
    private static void IncorrectLoginTest(LoginAPI loginAPI) {
        String username = "username";
        String password = "password";
        assertThrows(InvalidLoginException.class, () -> loginAPI.login(username, password), "Incorrect login info does not throw exception");
    }

    @Test
    private static void CorrectLoginTest(String username, String password, LoginAPI loginAPI) {
        assertDoesNotThrow(() -> loginAPI.login(username, password), "Correct username and password are throwing IncorrectLoginException");
    }


    //DB API Tests

    @Test
    private static void CreateAccountTest(String username, String password) throws LoadPropertiesException {
        try {
            new LoginDBAPI().createUser(username, password);
        }
        catch(Exception e){
            assertFalse(e instanceof AccountExistsException, "AccountExistsException incorrectly thrown");
        }

        List<Map<String, Object>> usersUnparsed = new LoginDBAPI().loadUserInfoTable();
        Map<Integer, User> users = LoginDBParser.parseUserInfoMap(usersUnparsed);
        assertFalse(users.size()== 0 , "Account not getting saved after creation");
        assertFalse(users.size()> 1 , "DB has too many account after just one account is added");
    }

    @Test
    private static void IncorrectLoginTest(LoginDBAPI loginDBAPI) {
        String username = "username";
        String password = "password";
        assertThrows(InvalidLoginException.class, () -> loginDBAPI.login(username, password), "Incorrect login info does not throw exception");
    }

    @Test
    private static void CorrectLoginTest(String username, String password, LoginDBAPI loginDBAPI) {
        assertDoesNotThrow(() -> loginDBAPI.login(username, password), "Correct username and password are throwing IncorrectLoginException");
    }


}
