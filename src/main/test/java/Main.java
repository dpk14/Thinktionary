package src.main.test.java;

import org.junit.jupiter.api.Test;
import src.main.java.BackEnd.API.Login.LoginAPI;
import src.main.java.BackEnd.Data.Lib.Paths.DBFileNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBUrls;


public class Main {

    @Test
    public static void TestLoginAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());
        LoginAPI loginAPI = new LoginAPI(dbUsername, dbPassword, testDBUrl);
        loginAPI.getMyLoginDBAPI().createTables();

        LoginTest.test(loginAPI);

        generalDBAPI.clearAllTables();

    }

    @Test
    public static void TestJournalAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());
        GeneralDBAPI generalDBAPI = new GeneralDBAPI(dbUsername, dbPassword, testDBUrl);
        generalDBAPI.createAllTables();

        performTests(dbUsername, dbPassword, testDBUrl);

        generalDBAPI.clearAllTables();

    }


}

