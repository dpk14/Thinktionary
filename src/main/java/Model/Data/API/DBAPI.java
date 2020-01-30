package Model.Data.API;

import Model.Data.Exceptions.LoadPropertiesException;
import Model.Utils.PathManager.DBFileInfo;
import Model.Utils.PathManager.DBNames;
import Model.Utils.PathManager.DBUrls;
import Model.Utils.PropertyUtils.PropertyManager;

public abstract class DBAPI {

    private static final String DB_URL_TEST_DEFAULT = DBUrls.getURL(DBNames.getSQLITE(), DBFileInfo.getTestDbName());
    private static final String DB_URL_MAIN_DEFAULT = DBUrls.getURL(DBNames.getSQLITE(), DBFileInfo.getMainDbName());
    private static final String DB_TEST_FILEPATH_DEFAULT = DBFileInfo.getTestDbPath(DBNames.getSQLITE());
    private static final String DB_MAIN_FILEPATH_DEFAULT = DBFileInfo.getMainDbPath(DBNames.getSQLITE());
    private static final String DB_USERNAME_DEFAULT = "kingsbda";
    private static final String DB_PASSWORD_DEFAULT = "1qazxsw2";
    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;
    protected String myDBFilename;

    public DBAPI() throws LoadPropertiesException {

        String dbUsername = PropertyManager.getDBUsername();
        myDBUsername = dbUsername == null? DB_USERNAME_DEFAULT : dbUsername;
        String dbPassword = PropertyManager.getDBPwd();
        myDBPassword = dbPassword == null? DB_PASSWORD_DEFAULT : dbPassword;
        String testMode = PropertyManager.getTestmode();
        String dbFilename = PropertyManager.getFilename();
        myDBFilename = dbFilename == null? (testMode ? DB_TEST_FILEPATH_DEFAULT : DB_MAIN_FILEPATH_DEFAULT) : dbFilename;
        myDBUrl = dbUrl == null? (testMode ? DB_URL_TEST_DEFAULT : DB_URL_MAIN_DEFAULT) : dbUrl;
    }

    public DBAPI(boolean testMode){
        myDBUsername = DB_USERNAME_DEFAULT;
        myDBPassword = DB_PASSWORD_DEFAULT;
        myDBUrl = testMode ? DB_URL_TEST_DEFAULT : DB_URL_MAIN_DEFAULT;
        myDBFilename = testMode ? DB_TEST_FILEPATH_DEFAULT : DB_MAIN_FILEPATH_DEFAULT;
    }

}
