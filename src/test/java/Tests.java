import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.Data.API.Run.LoginDBAPI;
import Model.ConfigUtils.PathUtils.PathManager;
import Model.ConfigUtils.PathUtils.DBNames;
import Model.ConfigUtils.PathUtils.DBUrls;
import Model.ErrorHandling.Exceptions.DBExceptions.EmptyDatabaseError;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.TableExceptions.CreateTableException;
import Model.ErrorHandling.Exceptions.TableExceptions.RemoveTableException;
import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public static void TestLoginDBAPI() throws EmptyDatabaseError, LoadPropertiesException, CreateTableException, RemoveTableException {

        //LoginDBInit loginDBInit = new LoginDBInit(null, null, DBUrls.getURL(DBNames.getSQLITE(), PathManager.getTestDbPath()));
        //loginDBInit.initialize();

        LoginDBAPI loginDBAPI = new LoginDBAPI();
        LoginTest.test(loginDBAPI);

        //loginDBInit.clearTables();
    }

    @Test
    public static void TestLoginAPI() throws EmptyDatabaseError, LoadPropertiesException, CreateTableException, RemoveTableException {

        //LoginDBInit loginDBInit = new LoginDBInit(null, null, DBUrls.getURL(DBNames.getSQLITE(), PathManager.getTestDbPath()));
        //loginDBInit.initialize();

        LoginAPI loginAPI = new LoginAPI();
        LoginTest.test(loginAPI);

        //loginDBInit.clearTables();
    }

    @Test
    public static void TestJournalAPI(String[] args) throws LoadPropertiesException {
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        String testDBUrl = DBUrls.getURL(DBNames.getSQLITE(), "");

        LoginDBInit loginDBInit = new LoginDBInit();
        //loginDBInit.createTables();

        makeAndTestJournal(dbUsername, dbPassword, testDBUrl);

        //loginDBInit.clearTables();
    }

    private static void makeAndTestJournal(String dbUsername, String dbPassword, String testDBUrl) throws LoadPropertiesException {
        LoginAPI loginAPI = new LoginAPI();
        Journal journal = generateJournal(loginAPI);

        //JournalDBInit journalDBInit = new JournalDBInit(dbUsername, dbPassword, testDBUrl);
        //journalDBInit.createTables();

        JournalTest.test(journal, journal.getUserID());

        //journalDBInit.clearTables();
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

