package Controller;

import Controller.Exceptions.NotLoggedInException;
import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.ErrorHandling.Exceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.InvalidLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@RestController
@RequestMapping("/users")
public class RESTJournal {

    private static final String PROTECTED_PATH = "/{userID}";
    @Autowired
    SessionManager mySessionManager;

    @GetMapping("/login")
    public ResponseEntity login(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            Journal journal = new LoginAPI().login(username, password);
            mySessionManager.addUser(journal.getUserID(), journal);
            return ResponseEntity.ok(journal);
        } catch (InvalidLoginException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            Journal journal = new LoginAPI().login(username, password);
            mySessionManager.removeUser(journal.getUserID());
            return ResponseEntity.ok(journal.getUserID());
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @PutMapping(value="/")
    public ResponseEntity makeAccount(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            int userId = new LoginAPI().makeAccount(username, password);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{userID}")
                    .buildAndExpand(userId)
                    .toUri();

            return ResponseEntity.created(uri).body(userId);
        }
        catch(AccountExistsException e){
            System.out.print(e.toString());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @PostMapping(PROTECTED_PATH + "/entries")
    public ResponseEntity createEntry(@PathVariable int userID, @RequestBody EntryBuilder entryBuilder) {
        Entry entry = entryBuilder.getMyEntry();
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                int entryID = journal.createEntry(entry);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{entryID}")
                        .buildAndExpand(entryID)
                        .toUri();
                return ResponseEntity.created(uri).body(entry);
            }
            catch(Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
            }
        }
        catch(NotLoggedInException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
        //add entryId when you get it
    }

    @PutMapping(PROTECTED_PATH + "/entries/{entryID}")
    public ResponseEntity modifyEntry(@PathVariable int userID, @PathVariable int entryID, @RequestBody Entry entry) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                journal.saveEntry(entryID, entry);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
            }
        }
        catch(NotLoggedInException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @DeleteMapping (PROTECTED_PATH + "/entries/{entryID}/remove")
    public ResponseEntity delete(@PathVariable int userID, @PathVariable int entryID) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                journal.removeEntry(entryID);
                return ResponseEntity.ok(entryID);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
            }
        }
        catch(NotLoggedInException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }

    @PostMapping (PROTECTED_PATH + "/entries/get")
    public ResponseEntity get(@PathVariable int userID, @RequestBody Set<Topic> topics) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                List<Entry> entries = journal.getTopicalEntries(topics);
                return ResponseEntity.ok(entries);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
            }
        }
        catch(NotLoggedInException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }


    @RequestMapping (PROTECTED_PATH + "/entries/getrand")
    public ResponseEntity getRandom(@PathVariable int userID, @RequestBody Set<Topic> topics) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                Entry entry = journal.getRandomEntry(topics);
                return ResponseEntity.ok(entry);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
            }
        }
        catch(NotLoggedInException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    //testing:

    @RequestMapping (PROTECTED_PATH + "/entries/getEntry")
    public ResponseEntity getEntry() {
        try {
            Set<Topic> topics = new HashSet<>();
            topics.add(new Topic("yorie", "blue"));
            topics.add(new Topic("horie", "red"));
            Entry entry = new Entry("yeet", "oh yeet that daddy", LocalDateTime.now(), topics);
            return ResponseEntity.ok(entry);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
        }
    }


}