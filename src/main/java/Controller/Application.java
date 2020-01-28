package Controller;

import Controller.Exceptions.ModeParseError;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sun.applet.AppletClassLoader;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

@SpringBootApplication
public class Application {

    private static String USER_PROP = "db.user";
    private static String PWD_PROP = "db.pwd";
    private static String USER_PROP = "db.user";

    public static void main(String[] args) {
        String path = Application.class.getResource("db.properties").getPath();
        try{
            boolean testMode = testModeParser(args[0]);
            new LoginDBInit(testMode).initialize();
            new JournalDBInit(testMode).initialize();
            SpringApplication.run(Application.class, args);
        }
        catch(ModeParseError e){
            System.out.println(e.toString());
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

    private void setProperties(String path, String[] args){
        try (OutputStream output = new FileOutputStream(path)) {

            Properties prop = new Properties();

            // set the properties value
            prop.setProperty(USER_PROP, args[0]);
            prop.setProperty("db.user", args[1]);
            prop.setProperty("db.password", "password");

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }

    }
}


