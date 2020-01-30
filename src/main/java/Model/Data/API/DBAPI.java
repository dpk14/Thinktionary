package Model.Data.API;

import Model.Data.Exceptions.LoadPropertiesException;
import Model.Utils.PathUtils.PathManager;
import Model.Utils.PropertyUtils.PropertyManager;

public abstract class DBAPI {

    private static final String DB_USERNAME_DEFAULT = "kingsbda";
    private static final String DB_PASSWORD_DEFAULT = "1qazxsw2";
    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;
    protected String myDBRelFilename;
    protected String myDBAbsFilename;

    public DBAPI(){
        try {
            String dbUsername = PropertyManager.getDBUsername();
            myDBUsername = dbUsername == null ? DB_USERNAME_DEFAULT : dbUsername;
            String dbPassword = PropertyManager.getDBPwd();
            myDBPassword = dbPassword == null ? DB_PASSWORD_DEFAULT : dbPassword;

            String dbRelFilename = PropertyManager.getRelFilename();
            myDBRelFilename = dbRelFilename == null ? PathManager.getDBRelPath(PathManager.getDefaultTestName()) : dbRelFilename;

            String dbAbsFilename = PropertyManager.getAbsFilename();
            myDBAbsFilename = dbAbsFilename == null ? PathManager.getDBAbsPathFromRel(dbRelFilename) : dbAbsFilename;

            myDBUrl = PropertyManager.getDBUrl();
        }
        catch(LoadPropertiesException e){
            e.printStackTrace();
        }
    }

}
