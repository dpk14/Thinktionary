package src.main.test.java;

import src.main.java.BackEnd.API.Login.LoginAPI;

public class JournalTest {

    protected static void test(LoginAPI loginAPI){
        String username = "dpk14";
        String password = "10gg40w716";
        CreateAccountTest(username, password, loginAPI);
        IncorrectLoginTest(loginAPI);
        CorrectLoginTest(username, password, loginAPI);

    }

}
