package RESTController;

import Model.API.Journal.Entry;
import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.DBExceptions.TopicBankAddException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import RESTController.SerializableModels.ErrorMessage;
import RESTController.SerializableModels.ResponsePair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.sql.SQLException;

@RestController
@RequestMapping("/users")
public class RESTJournal {

    private static final String PROTECTED_PATH = "/{userID}";

    @PostMapping(PROTECTED_PATH + "/entries")
    public ResponseEntity createEntry(HttpServletRequest httpServletRequest,
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
            catch(Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
            }
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RESTStrings.getNoSessionException());
        }
        //add entryId when you get it
    }

    @PutMapping(PROTECTED_PATH + "/entries/{entryID}")
    public ResponseEntity modifyEntry(HttpServletRequest httpServletRequest, @PathVariable int entryID, @RequestBody Entry entry) {
        Journal journal = (Journal) httpServletRequest.getSession().getAttribute(RESTStrings.getJournalAttribute());
        try {
            journal.saveEntry(entryID, entry);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
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