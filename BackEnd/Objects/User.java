package BackEnd.Objects;

import BackEnd.Main.Journal;

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
