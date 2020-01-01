package src.main.test.java;

import org.junit.jupiter.api.Test;
import src.main.java.BackEnd.API.Login.LoginAPI;
import src.main.java.BackEnd.Data.Lib.Paths.DBFileNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBUrls;
import src.main.java.BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import src.main.java.BackEnd.ErrorHandling.Exceptions.InvalidLoginException;


public class Main {

    @Test
    public static void TestLoginAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());
        LoginAPI loginAPI = new LoginAPI(dbUsername, dbPassword, testDBUrl);
        loginAPI.getMyLoginDBAPI().createTables();
        LoginTest.test(loginAPI);
        loginAPI.getMyLoginDBAPI().clearTables();
    }

    @Test
    public static void TestJournalAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());
        LoginAPI loginAPI = new LoginAPI(dbUsername, dbPassword, testDBUrl);
        loginAPI.getMyLoginDBAPI().createTables();

        try {
            loginAPI.makeAccount("username", "password");
            int userID = loginAPI.login(dbUsername, dbPassword);

        }
        catch(Exception e){
            System.out.println("Error in login; run TestLoginAPI test for particulars.");
            System.exit(0);
        }

        loginAPI.getMyLoginDBAPI().clearTables();

    }

    private void setUpTables(){

    }


}

