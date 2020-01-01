package BackEnd.Testing;

import BackEnd.API.LoginAPI;
import BackEnd.Data.Lib.Paths.DBFileNames;
import BackEnd.Data.Lib.Paths.DBNames;
import BackEnd.Data.Lib.Paths.DBUrls;
import BackEnd.ErrorHandling.Exceptions.AccountExistsException;

import java.util.Map;

public class Main {
    public static void main(String[] args){
        String dbUsername = (String) args[0];
        String dbPassword = (String) args[1];

        LoginAPI loginAPI = new LoginAPI(dbUsername, dbPassword, DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath()));
        String username = "dpk14";
        String password = "10gg40w716";
        //do this twice to see if exception is thrown

        try {
            loginAPI.makeAccount(username, password);
        }
        catch(AccountExistsException e){
            System.out.println(e.toString());
            System.out.println(e.getStackTrace());
        }
        catch(Exception e){
        }

        TestIncorrectLogin();

        TestCorrectLogin();

        s



    }


    private void TestIncorrectLogin(Map<String, String> usernameToPassword){  //test to see if incorrect username-password combo throws exception

    }



}

