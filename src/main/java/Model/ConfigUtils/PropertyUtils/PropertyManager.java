package Model.ConfigUtils.PropertyUtils;

import Controller.Application;
import Controller.Exceptions.ModeParseError;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ConfigUtils.PathUtils.PathManager;

import java.io.*;
import java.util.Properties;

public class PropertyManager {

    //Setters

    public static void setDBUsername(String value) {
        setProperty(PropertyKeys.getUserProp(), value);
    }

    public static void setDBPassword(String value) {
        setProperty(PropertyKeys.getPwdProp(), value);
    }

    public static void setDBFilename(String value) {
        String relPath = PathManager.getDBRelPath(value);
        String absPath = PathManager.getDBAbsPath(value);
        setProperty(PropertyKeys.getFilenameProp(), value);
        setProperty(PropertyKeys.getRelFilepathProp(), relPath);
        setProperty(PropertyKeys.getAbsFilepathProp(), absPath);
    }

    public static void setJarMode(boolean b) {
        setProperty(PropertyKeys.getJarModeProp(), Boolean.toString(b));
    }

    //Getters

    public static boolean getJarMode() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return Boolean.parseBoolean(prop.getProperty(PropertyKeys.getJarModeProp()));
    }

    public static String getDBUsername() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getUserProp());
    }

    public static String getDBPwd() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getPwdProp());
    }


    public static String getRelFilepath() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getRelFilepathProp());
    }

    public static String getAbsFilepath() throws LoadPropertiesException {
        return PathManager.getDBAbsPathFromRel(getRelFilepath());
    }

    public static String getDBUrl() throws LoadPropertiesException {
        Properties prop = loadProperties();
        String fileName = prop.getProperty(PropertyKeys.getRelFilepathProp());
        return PathManager.getDBUrl(fileName);
    }

    @Deprecated
    public static String getDBUrlFromProps() throws LoadPropertiesException {
        Properties prop = loadProperties();
        return prop.getProperty(PropertyKeys.getURLProp());
    }

    //Helpers

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
        String path = PropertyManager.class.getResource(PropertyKeys.getDbPropertiesName()).getPath();
        try {
            File dp_properties = new File(path);
            OutputStream output = new FileOutputStream(dp_properties, true);
            try{
                Properties prop = new Properties();
                for(Object existingKey: prop.keySet()){
                    System.out.println(existingKey);
                    prop.setProperty((String) existingKey, prop.getProperty((String) existingKey));
                }
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

    private static boolean testModeParser(String mode) throws ModeParseError {
        String modeLower = mode.toLowerCase();
        if(modeLower.equals("true")) return true;
        else if (modeLower.equals("false")) return false;
        else{
            throw new ModeParseError();
        }
    }

}
