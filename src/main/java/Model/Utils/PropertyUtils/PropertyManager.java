package Model.Utils.PropertyUtils;

import Controller.Application;
import Model.Data.Exceptions.LoadPropertiesException;
import Model.Utils.PathUtils.DBNames;
import Model.Utils.PathUtils.PathManager;

import java.io.*;
import java.util.Properties;

public class PropertyManager {

    public static void setTestMode(String value) {
        setProperty(PropertyKeys.getTestmodeProp(), value);
    }

    public static void setDBUsername(String value) {
        setProperty(PropertyKeys.getUserProp(), value);
    }

    public static void setDBPassword(String value) {
        setProperty(PropertyKeys.getPwdProp(), value);
    }

    public static void setDBFilename(String value) {
        String relPath = PathManager.getDBRelPath(DBNames.getSQLITE(), value);
        String absPath = PathManager.getDBAbsPath(DBNames.getSQLITE(), value);
        setProperty(PropertyKeys.getRelFilenameProp(), relPath);
        setProperty(PropertyKeys.getAbsFilenameProp(), absPath);
    }

    public static void setURL() {

    }

    //Getters

    public static String getDBUsername() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getUserProp());
    }

    public static String getDBPwd() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getPwdProp());
    }

    public static String getTestmode() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getTestmodeProp());
    }

    public static String getFilename() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getAbsFilenameProp());
    }

    private static Properties loadProperties() throws LoadPropertiesException {
        InputStream stream = Application.class.getResourceAsStream(PropertyKeys.getDbPropertiesName());
        Properties prop = new Properties();
        try {
            prop.load(stream);
        }
        catch(IOException e){
            throw new LoadPropertiesException(e);
        }
        return prop;
    }

    public static void setProperty(String key, String value) {
        String path = Application.class.getResource(PropertyKeys.getDbPropertiesName()).getPath();
        try {
            OutputStream output = new FileOutputStream(path);
            try{
                Properties prop = new Properties();
                prop.setProperty(key, value);
                prop.store(output, null);
            }
            catch(IOException e){
                System.out.println("Properties could not be properly set");
                e.printStackTrace();
            }
        }
        catch(FileNotFoundException e){
            System.out.println("Properties filepath incorrect");
            e.printStackTrace();
        }
    }

}
