package Controller;

import Controller.Exceptions.ModeParseError;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.Utils.PropertyUtils.PropertyManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        PropertyManager.setUserMode(args[0]);
        PropertyManager.setUsername(args[1]);
        PropertyManager.setPassword(args[2]);
        PropertyManager.completeProperties();
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



