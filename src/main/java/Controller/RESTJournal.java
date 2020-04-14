package Controller;

import Controller.Exceptions.NotLoggedInException;
import Controller.Exceptions.Utils.ExceptionUtils;
import Controller.JSONBuilders.EntryBuilder;
import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import Model.API.Journal.EntryWithID;
import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import Model.ErrorHandling.Exceptions.NoSuchEntryException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.AccountExistsException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.CannotDeleteTopicException;
import Model.ErrorHandling.Exceptions.UserErrorExceptions.InvalidLoginException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class RESTJournal {

    private static final String PROTECTED_PATH = "/{userID}";
    @Autowired
    SessionManager mySessionManager;

    @GetMapping("/login")
    public ResponseEntity login(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) throws Exception {
        try {
            Journal journal = LoginAPI.login(username, password);
            mySessionManager.addUser(journal.getUserID(), journal);
            return ResponseEntity.ok(journal);
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            Journal journal = LoginAPI.login(username, password);
            mySessionManager.removeUser(journal.getUserID());
            return ResponseEntity.ok(journal.getUserID());
        } catch (LoadPropertiesException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
        catch (InvalidLoginException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @PutMapping(value="/")
    public ResponseEntity makeAccount(@RequestParam(value="user") String username, @RequestParam(value = "pwd") String password) {
        try {
            int userId = LoginAPI.makeAccount(username, password);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{userID}")
                    .buildAndExpand(userId)
                    .toUri();

            return ResponseEntity.created(uri).body(userId);
        }
        catch(AccountExistsException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
        catch(LoadPropertiesException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
    }

    @PostMapping(value=PROTECTED_PATH + "/entries", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity createEntry(@PathVariable int userID, @RequestBody EntryBuilder entryBuilder) {
        Entry entry = entryBuilder.getMyEntry();
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                int entryID = journal.createEntry(entry);
                EntryWithID entryWithID = new EntryWithID(entry, entryID);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{entryID}")
                        .buildAndExpand(entryID)
                        .toUri();
                return ResponseEntity.created(uri).body(entryWithID);
            }
            catch(Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        }
        catch(NotLoggedInException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
        //add entryId when you get it
    }

    @PutMapping(PROTECTED_PATH + "/entries/{entryID}")
    public ResponseEntity modifyEntry(@PathVariable int userID, @PathVariable int entryID, @RequestBody EntryBuilder entryBuilder) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                Entry savedEntry = journal.saveEntry(entryID, entryBuilder.getMyEntry());
                EntryWithID savedEntryWithID = new EntryWithID(savedEntry, entryID);
                return ResponseEntity.ok().body(savedEntryWithID);
            }
            catch (NoSuchEntryException e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
            catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
            }
        }
        catch(NotLoggedInException e){
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
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        }
        catch(NotLoggedInException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }

    @DeleteMapping (PROTECTED_PATH + "/topics/remove")
    public ResponseEntity deleteTopic(@PathVariable int userID, @RequestParam(value="name") String topicName) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                journal.removeUnusedTopicFromBank(topicName);
                return ResponseEntity.ok(topicName);
            } catch (CannotDeleteTopicException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
            } catch (Exception e){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        }
        catch(NotLoggedInException e){
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
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        }
        catch(NotLoggedInException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }


    @RequestMapping (PROTECTED_PATH + "/entries/getRand")
    public ResponseEntity getRandom(@PathVariable int userID, @RequestBody Set<Topic> topics) {
        try {
            Journal journal = mySessionManager.getSessionInfo(userID);
            try {
                Entry entry = journal.getRandomEntry(topics);
                return ResponseEntity.ok(entry);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        }
        catch(NotLoggedInException e){
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
        }
    }


}