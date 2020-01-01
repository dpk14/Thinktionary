package src.main.test.java;

import src.main.java.BackEnd.API.LoginAPI;
import src.main.java.BackEnd.Data.API.LoginDBAPI;
import src.main.java.BackEnd.ErrorHandling.Exceptions.AccountExistsException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    public static void test(LoginAPI loginAPI){
        String username = "dpk14";
        String password = "10gg40w716";
        //do this twice to see if exception is thrown

        createAccountTest(username, password, loginAPI);
        assertEquals()

        TestIncorrectLogin();

        TestCorrectLogin();

        s


    }

    private static void createAccountTest(String username, String password, LoginAPI loginAPI){
        try {
            loginAPI.makeAccount(username, password);
        }
        catch(Exception e){
            assertFalse(e instanceof AccountExistsException, "AccountExistsException incorrectly thrown");
        }

        try{
            loginDBAPI.
        }
    }
}
