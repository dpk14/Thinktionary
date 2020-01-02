package src.main.test.java;

import org.junit.jupiter.api.Test;
import src.main.java.BackEnd.API.Journal.Entry;
import src.main.java.BackEnd.API.Journal.EntryComponents.Date;
import src.main.java.BackEnd.API.Journal.EntryComponents.Topic;
import src.main.java.BackEnd.API.Journal.JournalAPI;
import src.main.java.BackEnd.API.Journal.JournalDBParser;
import src.main.java.BackEnd.API.Login.LoginAPI;
import src.main.java.BackEnd.Data.API.JournalDBAPI;
import src.main.java.BackEnd.Data.Lib.SQLStrings.ColumnLabels;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static src.main.java.BackEnd.API.Journal.JournalDBParser.parseEntryMap;

public class JournalTest {

    protected static void test(JournalAPI journalAPI, int userID){
        CreateEntryTest(journalAPI, userID);
        /*TODO:
        journalAPI.getRandomEntry();
        journalAPI.getTopicalEntries();
        journalAPI.removeEntry();
        journalAPI.saveEntry();
        */

    }

    @Test
    private static void CreateEntryTest(JournalAPI journalAPI, int userID){
        Set<Topic> set = new HashSet();
        set.add(new Topic("Confidence", "Blue"));
        set.add(new Topic("Motivation", "Green"));
        String text = "I must believe in myself!";
        String title = "Believing";
        Date date = new Date();
        try {
            date = new Date(1, 14, 2018);
        }
        catch (Exception e){}

        journalAPI.createEntry(set, text, title, date);
        List<Map<String, Object>> entryTable = journalAPI.getMyJournalDBAPI().loadEntryTable();
        List<Map<String, Object>> entryTopic = journalAPI.getMyJournalDBAPI().loadEntryTopicsTable();
        Map<Integer, Entry> map = JournalDBParser.parseEntryMap(entryTable, entryTopic);
        int entryID = JournalDBParser.getEntryID(entryTable);
        Entry e = map.get(entryID);

        assertTrue(e.getmyTitle().equals(title), "Title not properly stored when Entry created");
        assertTrue(e.getmyText().equals(text), "Text not properly stored when Entry created");
        assertTrue(e.getMyCreatedasString().equals(date.toString()), "Created Date not properly stored when Entry created");
        assertTrue(e.getMyModfiedasString().equals(date.toString()), "Modified Date not properly stored when Entry created");

        List<Map<String, Object>> entryTopicTable = journalAPI.getMyJournalDBAPI().loadEntryTopicsTable();

    }

}
