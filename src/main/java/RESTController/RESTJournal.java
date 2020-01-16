package RESTController;

import Model.API.Journal.Entry;
import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import RESTController.SerializableModels.ErrorMessage;
import RESTController.SerializableModels.ResponsePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/users")
public class RESTJournal {

    private static final String PROTECTED_PATH = "/{userID}";

    @PostMapping(PROTECTED_PATH + "/")
    public ResponseEntity createEntry(HttpServletRequest httpServletRequest,
                                              @PathVariable String userName, @PathVariable String password, @PathVariable int userId,
                                              @RequestBody Entry entry) {
        try {
            Journal journal = (Journal) httpServletRequest.getSession().getAttribute(RESTStrings.getJournalAttribute());
            try {
                int entryID = journal.createEntry(entry);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{entryID}")
                        .buildAndExpand(entryID)
                        .toUri();
                return ResponseEntity.created(uri).body(entry);
            }
            catch(InvalidLoginException e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RESTStrings.getNoSessionException());
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