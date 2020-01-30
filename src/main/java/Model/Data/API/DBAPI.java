package Model.Data.API;

import Model.Data.Exceptions.LoadPropertiesException;
import Model.Utils.PathUtils.PathManager;
import Model.Utils.PathUtils.DBNames;
import Model.Utils.PathUtils.DBUrls;
import Model.Utils.PropertyUtils.PropertyManager;

import java.nio.file.Path;

public abstract class DBAPI {

    private static final String DB_URL_TEST_DEFAULT = DBUrls.getURL(DBNames.getSQLITE(), PathManager.getTestDbName());
    private static final String DB_URL_MAIN_DEFAULT = DBUrls.getURL(DBNames.getSQLITE(), PathManager.getMainDbName());
    private static final String DB_TEST_FILEPATH_DEFAULT = PathManager.getDefaultTestDbPath(DBNames.getSQLITE());
    private static final String DB_MAIN_FILEPATH_DEFAULT = PathManager.getDefaultMainDbPath(DBNames.getSQLITE());
    private static final String DB_USERNAME_DEFAULT = "kingsbda";
    private static final String DB_PASSWORD_DEFAULT = "1qazxsw2";
    private String myDBUrl;
    private String myDBUsername;
    private String myDBPassword;
    private String myDBRelFilename;
    private String myDBAbsFilename;

    public DBAPI() throws LoadPropertiesException {

        String dbUsername = PropertyManager.getDBUsername();
        myDBUsername = dbUsername == null? DB_USERNAME_DEFAULT : dbUsername;
        String dbPassword = PropertyManager.getDBPwd();
        myDBPassword = dbPassword == null? DB_PASSWORD_DEFAULT : dbPassword;
        boolean testMode = PropertyManager.getTestmode();
        String dbRelFilename = PropertyManager.getRelFilename();
        myDBRelFilename = dbRelFilename == null?
                    (testMode? PathManager.getDBRelPath(PathManager.getDefaultTestName()) :
                               PathManager.getDBRelPath(PathManager.getDefaultMainName()))
                    : dbRelFilename;
        String dbAbsFilename = PropertyManager.getAbsFilename();
        myDBAbsFilename = dbAbsFilename == null?
                    (testMode? PathManager.getDBAbsPathFromRel(dbRelFilename) :
                               PathManager.getDBAbsPathFromRel(dbRelFilename))
                    : dbAbsFilename;
        String dbUrl = PropertyManager.getDBUrl();
        myDBUrl = dbUrl == null? (testMode ? DB_URL_TEST_DEFAULT : DB_URL_MAIN_DEFAULT) : dbUrl;
    }

    public DBAPI(boolean testMode){
        myDBUsername = DB_USERNAME_DEFAULT;
        myDBPassword = DB_PASSWORD_DEFAULT;
        myDBUrl = testMode ? DB_URL_TEST_DEFAULT : DB_URL_MAIN_DEFAULT;
        myDBFilename = testMode ? DB_TEST_FILEPATH_DEFAULT : DB_MAIN_FILEPATH_DEFAULT;
    }

}
