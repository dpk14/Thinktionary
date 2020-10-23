package Controller;

import Model.ConfigUtils.PropertyUtils.PropertyManager;
import Model.Data.API.DBAPI;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import Model.ErrorHandling.Exceptions.ServerExceptions.TableExceptions.CreateTableException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class Application {

    public static void main (String[] args) throws CreateTableException, URISyntaxException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        PropertyManager.setDBUsername(username);
        PropertyManager.setDBPassword(password);
        PropertyManager.setURL(url);
        new DBAPI().getAccessKeys().apply();
        new LoginDBInit().initialize();
        new JournalDBInit().initialize();
        SpringApplication.run(Application.class, args);
    }

}



