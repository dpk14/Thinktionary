package RESTController;

import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import Model.API.Journal.Journal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Set;
import java.util.List;

@RestController
@RequestMapping("/users")
public class RESTJournal {

    private static final String PROTECTED_PATH = "/{userID}";

    @PostMapping(PROTECTED_PATH + "/entries")
    public ResponseEntity createEntry(HttpServletRequest httpServletRequest,
                                              @PathVariable int userID, @RequestBody Entry entry) {
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
    public ResponseEntity modifyEntry(HttpServletRequest httpServletRequest, @PathVariable int userID, @PathVariable int entryID, @RequestBody Entry entry) {
        Journal journal = (Journal) httpServletRequest.getSession().getAttribute(RESTStrings.getJournalAttribute());
        try {
            journal.saveEntry(entryID, entry);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
        }
    }

    @DeleteMapping (PROTECTED_PATH + "/entries/{entryID}")
    public ResponseEntity delete(HttpServletRequest httpServletRequest, @PathVariable int userID, @PathVariable int entryID) {
        Journal journal = (Journal) httpServletRequest.getSession().getAttribute(RESTStrings.getJournalAttribute());
        try {
            journal.removeEntry(entryID);
            return ResponseEntity.noContent().build();
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
        }
    }

    @RequestMapping (PROTECTED_PATH + "/entries/get")
    public ResponseEntity get(HttpServletRequest httpServletRequest, @PathVariable int userID, @RequestBody Set<Topic> topics) {
        Journal journal = (Journal) httpServletRequest.getSession().getAttribute(RESTStrings.getJournalAttribute());
        try {
            List<Entry> entries = journal.getTopicalEntries(topics);
            return ResponseEntity.ok(entries);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
        }
    }

    @RequestMapping (PROTECTED_PATH + "/entries/getrand")
    public ResponseEntity getRandom(HttpServletRequest httpServletRequest, @PathVariable int userID, @RequestBody Set<Topic> topics) {
        Journal journal = (Journal) httpServletRequest.getSession().getAttribute(RESTStrings.getJournalAttribute());
        try {
            Entry entry = journal.getRandomEntry(topics);
            return ResponseEntity.ok(entry);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.toString() + " " + e.getStackTrace().toString());
        }
    }




}