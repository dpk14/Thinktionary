package Controller;

import Model.ConfigUtils.PropertyUtils.PropertyManager;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.ErrorHandling.Exceptions.ServerExceptions.DBExceptions.EmptyDatabaseError;
import Model.ErrorHandling.Exceptions.ServerExceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.CreateTableException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws LoadPropertiesException, CreateTableException, EmptyDatabaseError {
        setProperties(args);
        new LoginDBInit().initialize();
        new JournalDBInit().initialize();
        SpringApplication.run(Application.class, args);
    }

    private static void setProperties(String[] args) {
        if (args.length > 0){
            System.out.println("Running in cmd line mode: \n");
            PropertyManager.setDBUsername(args[0]);
            PropertyManager.setDBPassword(args[1]);
            PropertyManager.setDBFilename(args[2]);
            PropertyManager.setJarMode(false);
            //PropertyManager.setURL();
        }
        else{
            System.out.println("Running in JAR mode: \n");
        }
        System.setProperty("aws.accessKeyId", "AKIAIYYEPEPS4B52J7KA");
        System.setProperty("aws.secretKey", "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");
    }

}



