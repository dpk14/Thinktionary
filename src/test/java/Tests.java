import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.Data.API.Run.LoginDBAPI;
import Model.Utils.PathManager.DBFileInfo;
import Model.Utils.PathManager.DBNames;
import Model.Utils.PathManager.DBUrls;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public static void TestLoginDBAPI(){

        LoginDBInit loginDBInit = new LoginDBInit(null, null, DBUrls.getURL(DBNames.getSQLITE(), DBFileInfo.getTestDbPath()));
        loginDBInit.initialize();

        LoginDBAPI loginDBAPI = new LoginDBAPI();
        LoginTest.test(loginDBAPI);

        loginDBInit.clearTables();
    }

    @Test
    public static void TestLoginAPI(){

        LoginDBInit loginDBInit = new LoginDBInit(null, null, DBUrls.getURL(DBNames.getSQLITE(), DBFileInfo.getTestDbPath()));
        loginDBInit.initialize();

        LoginAPI loginAPI = new LoginAPI();
        LoginTest.test(loginAPI);

        loginDBInit.clearTables();
    }

    @Test
    public static void TestJournalAPI(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileInfo.getTestDbPath());

        LoginDBInit loginDBInit = new LoginDBInit(dbUsername, dbPassword, testDBUrl);
        loginDBInit.createTables();

        makeAndTestJournal(dbUsername, dbPassword, testDBUrl);

        loginDBInit.clearTables();
    }

    private static void makeAndTestJournal(String dbUsername, String dbPassword, String testDBUrl){
        LoginAPI loginAPI = new LoginAPI();
        Journal journal = generateJournal(loginAPI);

        JournalDBInit journalDBInit = new JournalDBInit(dbUsername, dbPassword, testDBUrl);
        journalDBInit.createTables();

        JournalTest.test(journal, journal.getUserID());

        journalDBInit.clearTables();
    }

    private static Journal generateJournal(LoginAPI loginAPI){
        String randUsername = "username";
        String randPassword = "password";

        try {
            loginAPI.makeAccount(randUsername, randPassword);
            Journal journal = loginAPI.login(randUsername, randPassword);
            return journal;
        }
        catch(Exception e){
            System.out.println("Error in login; run TestLoginAPI test for particulars.");
            System.exit(0);
        }
        return null;
    }

}

