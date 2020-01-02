package src.main.test.java;

import src.main.java.BackEnd.API.Journal.JournalAPI;
import src.main.java.BackEnd.API.Login.LoginAPI;
import src.main.java.BackEnd.Data.API.JournalDBAPI;

public class JournalTest {

    protected static void test(JournalAPI journalAPI){
         


        CreateAccountTest(username, password, loginAPI);
        IncorrectLoginTest(loginAPI);
        CorrectLoginTest(username, password, loginAPI);

    }

}
