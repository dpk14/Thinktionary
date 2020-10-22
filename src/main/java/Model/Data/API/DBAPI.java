package Model.Data.API;

import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ConfigUtils.PathUtils.PathManager;
import Model.ConfigUtils.PropertyUtils.PropertyManager;

public abstract class DBAPI {

    private static final String DB_USERNAME_DEFAULT = "kingsbda";
    private static final String DB_PASSWORD_DEFAULT = "1qazxsw2";
    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;
    protected String myDBRelFilename;
    protected String myDBAbsFilename;

    public DBAPI() throws LoadPropertiesException {
            String dbUsername = PropertyManager.getDBUsername();
            myDBUsername = dbUsername == null ? DB_USERNAME_DEFAULT : dbUsername;
            String dbPassword = PropertyManager.getDBPwd();
            myDBPassword = dbPassword == null ? DB_PASSWORD_DEFAULT : dbPassword;

            String dbRelFilename = PropertyManager.getRelFilepath();
            myDBRelFilename = dbRelFilename == null ? PathManager.getDBRelPath(PathManager.getDefaultTestName()) : dbRelFilename;

            String dbAbsFilename = PropertyManager.getAbsFilepath();
            myDBAbsFilename = dbAbsFilename == null ? PathManager.getDBAbsPathFromRel(dbRelFilename) : dbAbsFilename;

            myDBUrl = PropertyManager.getDBUrl();
    }
}
