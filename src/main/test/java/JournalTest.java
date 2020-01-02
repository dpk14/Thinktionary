package src.main.test.java;

import org.junit.jupiter.api.Test;
import src.main.java.BackEnd.API.Journal.JournalAPI;
import src.main.java.BackEnd.API.Login.LoginAPI;
import src.main.java.BackEnd.Data.API.JournalDBAPI;

public class JournalTest {

    protected static void test(JournalAPI journalAPI){
        journalAPI.createEntry();
        journalAPI.getRandomEntry();
        journalAPI.getTopicalEntries();
        journalAPI.removeEntry();
        journalAPI.saveEntry();

    }

    @Test
    private void CreateEntryTest(JournalAPI journalAPI){

        journalAPI.createEntry();
    }

    @Test
    private void GetRandomEntryTest(){}

    @Test
    private void GetTopicalEntriesTest(){}

    @Test
    private void RemoveEntryTest(){}

    @Test
    private void SaveEntryTest(){}

}
