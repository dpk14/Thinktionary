
import Model.API.Journal.Entry;
import Model.API.Journal.EntryComponents.Topic;
import Model.API.Journal.Journal;
import Model.API.Journal.JournalDBParser;
import Model.Data.API.Run.JournalDBAPI;
import Model.ErrorHandling.Exceptions.LoadPropertiesException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JournalTest {

    protected static void test(Journal journal, int userID) throws LoadPropertiesException {
        CreateEntryTest(journal, userID);
        /*TODO:
        journalAPI.getRandomEntry();
        journalAPI.getTopicalEntries();
        journalAPI.removeEntry();
        journalAPI.saveEntry();
        */

    }

    @Test
    private static void CreateEntryTest(Journal journalAPI, int userID) throws LoadPropertiesException {
        Set<Topic> set = new HashSet();
        set.add(new Topic("Confidence", "Blue"));
        set.add(new Topic("Motivation", "Green"));
        String text = "I must believe in myself!";
        String title = "Believing";
        LocalDateTime now = LocalDateTime.now();
        try {
            journalAPI.createEntry(new Entry(title, text, now, set));
        }
        catch (Exception e){
            System.out.print(e.toString() + e.getStackTrace().toString());
        }
        List<Map<String, Object>> entryTable = new JournalDBAPI(userID).loadEntryTable();
        List<Map<String, Object>> entryTopic = new JournalDBAPI(userID).loadEntryTopicsTable();
        Map<Integer, Entry> map = JournalDBParser.parseEntryMap(entryTable, entryTopic);
        int entryID = JournalDBParser.getEntryID(entryTable);
        Entry e = map.get(entryID);

        assertTrue(e.getmyTitle().equals(title), "Title not properly stored when Entry created");
        assertTrue(e.getmyText().equals(text), "Text not properly stored when Entry created");
        assertTrue(e.getMyCreated().equals(now.toString()), "Created Date not properly stored when Entry created");
        assertTrue(e.getMyModfied().equals(now.toString()), "Modified Date not properly stored when Entry created");

        //TODO: test other two tables

    }

}
