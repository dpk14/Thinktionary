package Controller;

import Controller.Exceptions.ArgumentFormatError;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.Utils.PropertyUtils.PropertyManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try{
            setProperties(args);
        }
        catch(LoadPropertiesException e){
            e.printStackTrace();
            throw new ArgumentFormatError();
        }
        new LoginDBInit().initialize();
        new JournalDBInit().initialize();
        SpringApplication.run(Application.class, args);
    }

    private static void setProperties(String[] args) throws LoadPropertiesException {
        PropertyManager.setDBUsername(args[0]);
        PropertyManager.setDBPassword(args[1]);
        PropertyManager.setDBFilename(args[2]);
        PropertyManager.setURL();
    }

}



