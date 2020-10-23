package Controller;

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
        new LoginDBInit().initialize();
        new JournalDBInit().initialize();
        SpringApplication.run(Application.class, args);
    }

}



