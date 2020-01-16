package RESTController;

import Model.API.Journal.Entry;
import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.Data.Lib.Paths.DBFileNames;
import Model.Data.Lib.Paths.DBNames;
import Model.Data.Lib.Paths.DBUrls;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import RESTController.SerializableModels.ErrorMessage;
import RESTController.SerializableModels.ResponsePair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class RESTJournal {

    private final String myDBUsername = "dbUsername";
    private final String myDBPassword = "dbPassword";
    private final String myDBURL = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());

    @PostMapping("/{userID}/")
    public ResponsePair createEntry(@PathVariable String userName, @PathVariable String password, @PathVariable int userId,
                                    @RequestBody Entry entry) {
        Journal journalAPI = new Journal(myDBUsername, myDBPassword, myDBURL, userId);
        try {
            int id = journalAPI.createEntry(entry);
            return new ResponsePair(ResponseEntity., null);
        }
        catch(InvalidLoginException e){
            return new (-1, new ErrorMessage(e.toString(), e.getStackTrace()));
        }

        //add entryId when you get it
    }

    @PutMapping("/{userName}/{password}/{userID}/Journal/{entryID}")
    public UserInfo modifyEntry(@PathVariable String userName, @PathVariable String password, @PathVariable int userID,
                                @PathVariable int entryID, @RequestBody Entry entry) {
        Journal journalAPI = new Journal(myDBUsername, myDBPassword, myDBURL, userID);
        try {
            journalAPI.saveEntry(entryID, entry);
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