package RESTController;

import Model.API.Login.LoginAPI;
import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RESTJournal {

    private final String myDBUsername = "dbUsername";
    private final String myDBPassword = "dbPassword";
    private final String myDBURL = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());

    @RequestMapping("/login")
    public UserInfo login(@RequestParam(value = "username", defaultValue = "") String username,
                          @RequestParam(value = "password", defaultValue = "") String password) {
        LoginAPI loginAPI = new LoginAPI(myDBUsername, myDBPassword, myDBURL);
        try {
            int userId = loginAPI.login(username, password);
            return new UserInfo(userId, null);
        }
        catch(InvalidLoginException e){
            return new UserInfo(-1, new ErrorMessage(e.toString(), e.getStackTrace()));
        }
    }

    @RequestMapping("/makeAccount")
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


}