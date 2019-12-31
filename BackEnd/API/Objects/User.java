package BackEnd.API.Objects;

import BackEnd.API.Journal;

import java.sql.SQLException;

public class User {
    private String myUsername;
    private String myPassword;
    private Journal myJournal;

    User(String username, String password){
        myUsername = username;
        myPassword = password;
        try {
            myJournal = new Journal(myUsername);
        }
        catch(Exception e){

        }
    }

}
