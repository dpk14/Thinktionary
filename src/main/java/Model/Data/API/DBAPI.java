package Model.Data.API;

import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;

public abstract class DBAPI {

    private static final String DB_URL_DEFAULT = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getTestDbPath());
    private static final String DB_USERNAME_DEFAULT = "kingsbda";
    private static final String DB_PASSWORD_DEFAULT = "1qazxsw2";
    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;

    public DBAPI(String dbUsername, String dbPassword, String dbUrl) {
        myDBUsername = dbUsername == null? DB_USERNAME_DEFAULT : dbUsername;
        myDBPassword = dbPassword == null? DB_PASSWORD_DEFAULT : dbPassword;
        myDBUrl = dbUrl == null? DB_URL_DEFAULT : dbUrl;
    }

    public DBAPI(){
        myDBUsername = DB_USERNAME_DEFAULT;
        myDBPassword = DB_PASSWORD_DEFAULT;
        myDBUrl = DB_URL_DEFAULT;
    }

}
