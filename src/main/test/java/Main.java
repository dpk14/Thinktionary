package src.main.test.java;

import org.junit.jupiter.api.Test;
import src.main.java.BackEnd.API.LoginAPI;
import src.main.java.BackEnd.Data.API.GeneralDBAPI;
import src.main.java.BackEnd.Data.Lib.Paths.DBFileNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBNames;
import src.main.java.BackEnd.Data.Lib.Paths.DBUrls;
import src.main.java.BackEnd.ErrorHandling.Exceptions.AccountExistsException;

import java.util.Map;


public class Main {

    @Test
    public static void TestLoginAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());
        GeneralDBAPI generalDBAPI = new GeneralDBAPI(dbUsername, dbPassword, testDBUrl);
        generalDBAPI.createAllTables();

        LoginTest.test(dbUsername, dbPassword, testDBUrl);

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

