package Controller;

import Controller.Exceptions.ArgumentFormatError;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.TableExceptions.CreateTableException;
import Model.Utils.PropertyUtils.PropertyManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static String DEFAULT_DBUSERNAME = "dpk14";
    private static String DEFAULT_DBPASSWORD = "1qazxsw23edcvfr4";
    private static String DEFAULT_DBFILE = "main.db";

    public static void main(String[] args) throws LoadPropertiesException, CreateTableException {
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
        if(args.length == 0) {
            PropertyManager.setDBUsername(DEFAULT_DBUSERNAME);
            PropertyManager.setDBPassword(DEFAULT_DBPASSWORD);
            PropertyManager.setDBFilename(DEFAULT_DBFILE);
        }
        else {
            PropertyManager.setDBUsername(args[0]);
            PropertyManager.setDBPassword(args[1]);
            PropertyManager.setDBFilename(args[2]);
        }
        PropertyManager.setURL();
    }

}



