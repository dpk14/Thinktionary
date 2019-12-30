package BackEnd.API.Objects;

import BackEnd.API.Journal;

public class User {
    private String myUsername;
    private String myPassword;
    private Journal myJournal;

    User(String username, String password){
        myUsername = username;
        myPassword = password;
        myJournal = new Journal();
    }

}
