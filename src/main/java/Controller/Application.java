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
        setProperties(args);
        new LoginDBInit().initialize();
        new JournalDBInit().initialize();
        SpringApplication.run(Application.class, args);
    }

    private static void setProperties(String[] args) {
        PropertyManager.setTestMode(args[0]);
        PropertyManager.setDBUsername(args[1]);
        PropertyManager.setDBPassword(args[2]);
        PropertyManager.setDBFilename(args[3]);
        try {
            PropertyManager.setURL();
        }
    }

}



