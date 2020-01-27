package Controller;

import Controller.Exceptions.BooleanParseException;
import Model.Data.API.Initialization.JournalDBInit;
import Model.Data.API.Initialization.LoginDBInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try{
            boolean testMode = testModeParser(args[0]);
            if( Boolean.parseBoolean(args[0]) == true)
            new LoginDBInit(testMode).initialize();
            new JournalDBInit(testMode).initialize();
            SpringApplication.run(Application.class, args);
        }
        catch(BooleanParseException e){
            System.out.println(e.toString());
        }
    }

    private static boolean testModeParser(String mode) throws BooleanParseException {
        String modeLower = mode.toLowerCase();
        if(modeLower.equals("true")) return true;
        else if (modeLower.equals("false")) return false;
        else{
            throw new BooleanParseException();
        }
    }
}


