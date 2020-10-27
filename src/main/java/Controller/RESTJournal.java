package Controller;

import Controller.Exceptions.NotLoggedInException;
import Controller.Exceptions.Utils.ExceptionUtils;
import Controller.JSONBuilders.EntryBuilder;
import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import Model.API.Journal.EntryWithID;
import Model.API.Journal.Journal;
import Model.API.Login.LoginAPI;
import Utils.PropertyUtils.PropertyManager;
import Model.Data.API.DBAPI;
import Model.Data.API.Initialization.JournalDBInitAPI;
import Model.Data.API.Initialization.LoginDBInitAPI;
import Model.Data.API.Run.LoginDBAPI;
import Utils.ErrorHandling.Exceptions.ServerExceptions.NoSuchEntryException;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.CannotDeleteTopicException;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.InvalidLoginException;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.UserErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class RESTJournal {

    private static final String PROTECTED_PATH = "/{userID}";

    SessionManager sessionManager;
    LoginAPI loginAPI;

    @Autowired
    public RESTJournal() throws URISyntaxException {
        this.sessionManager = new SessionManager();
        URI dbUri = new URI(System.getenv("DATABASE_URL"));
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        PropertyManager.setDBUsername(username);
        PropertyManager.setDBPassword(password);
        PropertyManager.setURL(url);
        new DBAPI().getAccessKeys().apply();
        new LoginDBInitAPI().createTablesIfDoNotExist();
        new JournalDBInitAPI().createTablesIfDoNotExist();
        this.loginAPI = new LoginAPI(new LoginDBAPI());
    }

    @PostMapping(value = "/login", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity login(@RequestBody UserCredentials credentials) {
        try {
            Journal journal = this.loginAPI.login(credentials.getUsername(), credentials.getPwd());
            this.sessionManager.addUser(journal.getUserID(), journal);
            return ResponseEntity.ok(journal);
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
    }

    @PostMapping(value = "/logout", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity logout(@RequestBody UserCredentials credentials) {
        try {
            Journal journal = this.loginAPI.login(credentials.getUsername(), credentials.getPwd());
            this.sessionManager.removeUser(journal.getUserID());
            return ResponseEntity.ok(journal.getUserID());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @PostMapping(value = "/verify", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity verifyAccountInfo(@RequestBody UserCredentials credentials) {
        try {
            this.loginAPI.verifyAccountDoesNotExistAndGenerateEmailConfirmation(credentials.getUsername(), credentials.getEmail());
            return ResponseEntity.ok().build();
        } catch (UserErrorException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
    }

    @PutMapping(value = "/", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity makeAccount(@RequestBody UserCredentials credentials) {
        try {
            int userId = this.loginAPI.makeAccount(credentials.getUsername(),
                    credentials.getPwd(),
                    credentials.getEmail(),
                    credentials.getConfKey());
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{userID}")
                    .buildAndExpand(userId)
                    .toUri();
            return ResponseEntity.created(uri).body(userId);
        } catch (UserErrorException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
    }

    @PostMapping(value = "/forgotpwd", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity sendConfKey(@RequestBody UserCredentials credentials) {
        try {
            this.loginAPI.verifyAccountExistsAndGenerateEmailConfirmation(credentials.getEmail(), credentials.getUsername());
            return ResponseEntity.ok().build();
        } catch (UserErrorException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
    }

    @PostMapping(value = "/resetpwd", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity resetPassword(@RequestBody UserCredentials credentials) {
        try {
            this.loginAPI.resetPassword(credentials.getUsername(),
                    credentials.getPwd(),
                    credentials.getEmail(),
                    credentials.getConfKey());
            return ResponseEntity.ok().build();
        } catch (UserErrorException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
        }
    }

    @PostMapping(value = PROTECTED_PATH + "/entries", consumes = "application/json;charset=UTF-8;")
    public ResponseEntity createEntry(@PathVariable int userID, @RequestBody EntryBuilder entryBuilder) {
        Entry entry = entryBuilder.getMyEntry();
        try {
            Journal journal = this.sessionManager.getSessionInfo(userID);
            try {
                int entryID = journal.createEntry(entry);
                EntryWithID entryWithID = new EntryWithID(entry, entryID);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{entryID}")
                        .buildAndExpand(entryID)
                        .toUri();
                return ResponseEntity.created(uri).body(entryWithID);
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
            }
        } catch (NotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
        //add entryId when you get it
    }

    @PutMapping(PROTECTED_PATH + "/entries/{entryID}")
    public ResponseEntity modifyEntry(@PathVariable int userID, @PathVariable int entryID, @RequestBody EntryBuilder entryBuilder) {
        try {
            Journal journal = this.sessionManager.getSessionInfo(userID);
            try {
                Entry savedEntry = journal.saveEntry(entryID, entryBuilder.getMyEntry());
                EntryWithID savedEntryWithID = new EntryWithID(savedEntry, entryID);
                return ResponseEntity.ok().body(savedEntryWithID);
            } catch (NoSuchEntryException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionUtils.stackTraceToString(e));
            }
        } catch (NotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

    @DeleteMapping(PROTECTED_PATH + "/entries/{entryID}/remove")
    public ResponseEntity delete(@PathVariable int userID, @PathVariable int entryID) {
        try {
            Journal journal = this.sessionManager.getSessionInfo(userID);
            try {
                journal.removeEntry(entryID);
                return ResponseEntity.ok(entryID);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        } catch (NotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }

    @DeleteMapping(PROTECTED_PATH + "/topics/remove")
    public ResponseEntity deleteTopic(@PathVariable int userID, @RequestParam(value = "name") String topicName) {
        try {
            Journal journal = this.sessionManager.getSessionInfo(userID);
            try {
                journal.removeUnusedTopicFromBank(topicName);
                return ResponseEntity.ok(topicName);
            } catch (CannotDeleteTopicException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        } catch (NotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }

    @PostMapping(PROTECTED_PATH + "/entries/get")
    public ResponseEntity get(@PathVariable int userID, @RequestBody Set<Topic> topics) {
        try {
            Journal journal = this.sessionManager.getSessionInfo(userID);
            try {
                List<Entry> entries = journal.getTopicalEntries(topics);
                return ResponseEntity.ok(entries);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        } catch (NotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }

    }


    @RequestMapping(PROTECTED_PATH + "/entries/getRand")
    public ResponseEntity getRandom(@PathVariable int userID, @RequestBody Set<Topic> topics) {
        try {
            Journal journal = this.sessionManager.getSessionInfo(userID);
            try {
                Entry entry = journal.getRandomEntry(topics);
                return ResponseEntity.ok(entry);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionUtils.stackTraceToString(e));
            }
        } catch (NotLoggedInException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString());
        }
    }

}