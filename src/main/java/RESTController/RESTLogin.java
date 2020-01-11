package RESTController;

import Model.API.Login.LoginAPI;
import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import RESTController.SerializableModels.ErrorMessage;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/login")
public class RESTLogin {
    private final String myDBUsername = "dbUsername";
    private final String myDBPassword = "dbPassword";
    private final String myDBURL = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());

    @GetMapping("/{username}/{password}/login")
    public UserInfo login(@PathVariable String username, @PathVariable String password) {
        LoginAPI loginAPI = new LoginAPI(myDBUsername, myDBPassword, myDBURL);
        try {
            int userId = loginAPI.login(username, password);
            return new UserInfo(userId, null);
        }
        catch(InvalidLoginException e){
            return new UserInfo(-1, new ErrorMessage(e.toString(), e.getStackTrace()));
        }

        //need to return uri of new resource
    }

    @RequestMapping("/{username}/{password}/makeAccount")
    public ErrorMessage makeAccount(@RequestParam(value = "username", defaultValue = "") String username,
                                 @RequestParam(value = "password", defaultValue = "") String password) {
        LoginAPI loginAPI = new LoginAPI(myDBUsername, myDBPassword, myDBURL);
        try {
            loginAPI.makeAccount(username, password);
            return null;
        }
        catch(AccountExistsException e){
            return new ErrorMessage(e.toString(), e.getStackTrace());
        }
    }

    //makeaccount needs to add /id, the id is another layer validating data

}