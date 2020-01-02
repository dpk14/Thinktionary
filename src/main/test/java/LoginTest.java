package src.main.test.java;

import org.junit.jupiter.api.Test;
import src.main.java.BackEnd.API.Login.LoginAPI;
import src.main.java.BackEnd.API.Login.User;
import src.main.java.BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import src.main.java.BackEnd.ErrorHandling.Exceptions.InvalidLoginException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LoginTest {

    protected static void test(LoginAPI loginAPI){
        String username = "dpk14";
        String password = "10gg40w716";
        CreateAccountTest(username, password, loginAPI);
        IncorrectLoginTest(loginAPI);
        CorrectLoginTest(username, password, loginAPI);

    }

    @Test
    private static void CreateAccountTest(String username, String password, LoginAPI loginAPI){
        try {
            loginAPI.makeAccount(username, password);
        }
        catch(Exception e){
            assertFalse(e instanceof AccountExistsException, "AccountExistsException incorrectly thrown");
        }

        Map<Integer, User> users = loginAPI.getMyLoginDBAPI().loadUserInfoMap();
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


}
