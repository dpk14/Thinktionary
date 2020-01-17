import Model.API.Login.LoginAPI;
import Model.Data.API.Initialization.LoginDBInit;
import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public static void TestLoginAPI(){

        LoginDBInit loginDBInit = new LoginDBInit(null, null, DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath()));
        loginDBInit.initialize();

        LoginAPI loginAPI = new LoginAPI();
        LoginTest.test(loginAPI);

        loginDBInit.clearTables();
    }

    @Test
    public static void TestJournalAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());

        LoginDBInit loginDBInit = new LoginDBInit(dbUsername, dbPassword, testDBUrl);
        loginDBInit.createTables();

        makeAndTestJournal(dbUsername, dbPassword, testDBUrl);

        loginDBInit.clearTables();
    }

    private static void makeAndTestJournal(String dbUsername, String dbPassword, String testDBUrl){
        LoginAPI loginAPI = new LoginAPI(dbUsername, dbPassword, testDBUrl);
        int userID = generateJournal(loginAPI);

        JournalDBInit journalDBInit = new JournalDBInit(dbUsername, dbPassword, testDBUrl);
        journalDBInit.createTables();

        JournalAPI journalAPI = new JournalAPI(dbUsername, dbPassword, testDBUrl, userID);
        JournalTest.test(journalAPI, userID);

        journalDBInit.clearTables();
    }

    private static int generateJournal(LoginAPI loginAPI){
        String randUsername = "username";
        String randPassword = "password";

        try {
            loginAPI.makeAccount(randUsername, randPassword);
            int userID = loginAPI.login(randUsername, randPassword);
            return userID;
        }
        catch(Exception e){
            System.out.println("Error in login; run TestLoginAPI test for particulars.");
            System.exit(0);
        }
        return Integer.MIN_VALUE;
    }

}

