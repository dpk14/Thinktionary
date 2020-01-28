package Controller;

import Controller.Exceptions.ModeParseError;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.Data.Lib.PropertiesManager.PropertyKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.applet.AppletClassLoader;
import sun.text.normalizer.NormalizerBase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        String path = Application.class.getResource(PropertyKeys.getDbPropertiesName()).getPath();
        updateProperties(path, args);
        new LoginDBInit().initialize();
        new JournalDBInit().initialize();
        SpringApplication.run(Application.class, args);
    }

    private static boolean testModeParser(String mode) throws ModeParseError {
        String modeLower = mode.toLowerCase();
        if(modeLower.equals("true")) return true;
        else if (modeLower.equals("false")) return false;
        else{
            throw new ModeParseError();
        }
    }

    private static void updateProperties(String path, String[] args){
        try {
            OutputStream output = new FileOutputStream(path);
            try{
                setProperties(path, args, output);
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

    private static void setProperties(String path, String[] args, OutputStream output) throws IOException {
        Properties prop = new Properties();
        try {
            testModeParser(args[0]);
            prop.setProperty(PropertyKeys.getUserProp(), args[0]);
        }
        catch(ModeParseError e){
            System.out.println(e.toString());
        }
        prop.setProperty(PropertyKeys.getPwdProp(), args[1]);
        prop.setProperty(PropertyKeys.getTestmodeProp(), args[2]);
        prop.store(output, null);
    }
}


