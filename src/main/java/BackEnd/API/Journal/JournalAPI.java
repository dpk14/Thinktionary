package src.main.java.BackEnd.API.Journal;

import src.main.java.BackEnd.Data.API.JournalDBAPI;
import src.main.java.BackEnd.API.Journal.EntryComponents.Date;
import src.main.java.BackEnd.API.Journal.EntryComponents.Topic;
import src.main.java.BackEnd.ErrorHandling.Errors.CorruptDBError;
import src.main.java.BackEnd.ErrorHandling.Exceptions.NoSuchEntryException;

import java.sql.SQLException;
import java.util.*;

public class JournalAPI {
    List<Entry> myEntries;
    Map<Integer, Entry> myEntryMap;
    Map<String, Topic> myTopics;
    int myUserID;
    JournalDBAPI myJournalDBAPI;

    /*
    Sorter and manager of Entries; has full access privileges on Entries
     */

    //used prepared statements in Java so that you dont have to recompile queries

    public JournalAPI(String userName, String password, String dbUrl, int userID){
        myJournalDBAPI = new JournalDBAPI(userID, userName, password, dbUrl);
        List<Map<String, Object>> entryTopic = myJournalDBAPI.loadEntryTopicsTable();
        List<Map<String, Object>> entryTable = myJournalDBAPI.loadEntryTable(); //uses primary IDs and maps them to Entry
        JournalDBParser.parseEntryMap(entryTable, entryTopic);
        List<Map<String, Object>> topicTable = myJournalDBAPI.loadTopicBankTable();
        myTopics = JournalDBParser.parseTopics(topicTable);
        myEntries = JournalDBParser.parseEntries(myEntryMap);
        myUserID = userID;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    public void createEntry(Set<Topic> topics, String text, String title, Date date)
    {
        try {
            updateTopicBank(topics);
            addEntry(text, topics, title, date);
        }
        catch(Exception e){
            throw new CorruptDBError(e);
        }
    }

    public void saveEntry(int entryID, Set<Topic> topics, String text, String title, Date creationDate) {
        try {
            updateTopicBank(topics);
            modifyEntry(entryID, topics, text, title, creationDate);
        }
        catch(Exception e){
            throw new CorruptDBError(e);
        }
    }

    public void removeEntry(int entryID) throws NoSuchEntryException{
        myJournalDBAPI.removeEntry(entryID);
        myEntries.remove(myEntryMap.get(entryID));
        myEntryMap.remove(entryID);
    }

    public Entry getRandomEntry(Set<Topic> topics){
        List<Entry> topicalEntries = getTopicalEntries(topics);
        int index = (int) Math.round(Math.random()*topicalEntries.size());
        return topicalEntries.get(index);
    }

    public List<Entry> getTopicalEntries(Set<Topic> topics){
        List<Entry> topicalEntries = new ArrayList<>();
        for(Entry entry : myEntries){
            Set<Topic> topicsCpy = new HashSet<>(topics);
            if(containsAll(topicsCpy, entry.getMyTopics())) {
                topicalEntries.add(entry);
            }
        }
        return topicalEntries;
    }

    public JournalDBAPI getMyJournalDBAPI(){
        return myJournalDBAPI;
    }

    public int getUserID() {
        return myUserID;
    }


    /*
    ----------------------------
    Helpers:
    ----------------------------
     */

    private void updateTopicBank(Set<Topic> topics) throws SQLException{
        Map<String, Topic> topicsMap = new HashMap();
        for(Topic topic : topics){
            topicsMap.put(topic.getMyTopic(), topic);
        }
        topicsMap.keySet().removeAll(myTopics.keySet()); //looks for new topics, makes map with which to update topic bank in Data
        Map<String, String> topicToColor = new HashMap();
        for(String key : topicsMap.keySet()){
            myTopics.put(key, topicsMap.get(key));
            topicToColor.put(key, topicsMap.get(key).getMyColor());
        }
        myJournalDBAPI.addToTopicBank(topicToColor); //updates Data topics
    }

    private void addEntry(String text, Set<Topic> topics, String title, src.main.java.BackEnd.API.Journal.EntryComponents.Date date)
            throws SQLException, IndexOutOfBoundsException, ClassCastException{

        Entry entry = date == null ? new Entry(topics, title, text) : new Entry(topics, title, text, date);
        int id = myJournalDBAPI.createEntry(entry);

        myEntryMap.put(id, entry);
        myEntries.add(entry);
    }

    private void modifyEntry(int entryID, Set<Topic> topics, String text, String title, Date creationDate) throws SQLException{
        Entry e = myEntryMap.get(entryID);
        e.setMyTitle(title);
        e.updateModification();
        e.setText(text);
        e.setMyTopics(topics);

        if(!creationDate.equals(e.getMyCreated())){ // if creation date has been modified, adjust order
            e.setMyCreated(creationDate);
            reorder(e);
        }

        myJournalDBAPI.save(entryID, e);
    }

    private void reorder(Entry e){
        myEntries.remove(e);
        for(int i = 0; i<myEntries.size(); i++){
            if(e.youngerThan(myEntries.get(i))){
                myEntries.add(i, e);
                break;
            }
        }
        myEntries.add(e);
    }

    private boolean containsAll(Set<Topic> topics1, Set<Topic> topics2){
        topics1.removeAll(topics2);
        return topics1.size() == 0;
    }

    private class EntryComparator implements Comparator<Entry> {
        @Override
        public int compare(Entry e1, Entry e2) {
            return e1.getMyCreated().compareTo(e2.getMyCreated());
        }

    }

}