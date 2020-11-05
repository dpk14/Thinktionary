package Model.API.Journal;

import Model.API.Journal.EntryComponents.Date;
import Model.API.Journal.EntryComponents.Topic;
import Model.Data.API.Run.JournalDBAPI;
import Utils.ErrorHandling.Exceptions.ServerExceptions.NoSuchEntryException;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.CannotDeleteTopicException;

import java.util.*;

public class Journal {
    List<EntryWithID> myEntries;
    Map<Integer, Entry> myEntryMap;
    Map<String, Topic> myTopics;
    int myUserID;
    String myUsername;
    String myEmail;
    /*
    Sorter and manager of Entries; has full access privileges on Entries
     */

    //used prepared statements in Java so that you dont have to recompile queries

    public Journal() {

    }

    public Journal(List<EntryWithID> entries, Map<Integer, Entry> entryMap, Map<String, Topic> topics, int userID, String username, String email) {
        myEntries = entries;
        myEntryMap = entryMap;
        myTopics = topics;
        myUserID = userID;
        myUsername = username;
        myEmail = email;
    }

    public Journal(int userID, String username, String email) {
        JournalDBAPI journalDBAPI = new JournalDBAPI(userID);
        List<Map<String, Object>> entryTopic = journalDBAPI.loadEntryTopicsTable();
        List<Map<String, Object>> entryTable = journalDBAPI.loadEntryTable(); //uses primary IDs and maps them to Entry'
        myEntryMap = JournalDBParser.parseEntryMap(entryTable, entryTopic);
        List<Map<String, Object>> topicTable = journalDBAPI.loadTopicBankTable();
        myTopics = JournalDBParser.parseTopics(topicTable);
        myEntries = JournalDBParser.parseEntries(myEntryMap);
        myUserID = userID;
        myUsername = username;
        myEmail = email;
    }

    /*
    ----------------------------
    Public Run:
    ----------------------------
     */

    public int createEntry(Entry entry) throws CannotDeleteTopicException {
        updateTopicBank(entry.getMyTopics());
        int entryID = new JournalDBAPI(myUserID).addToEntryInfo(entry);
        updateEntryTopic(entryID, entry.getMyTopics());
        myEntryMap.put(entryID, entry);
        myEntries.add(new EntryWithID(entry, entryID));
        return entryID;
    }

    public Entry saveEntry(int entryID, Entry entry) throws NoSuchEntryException, CannotDeleteTopicException {
        Set<Topic> newTopics = entry.getMyTopics();
        updateTopicBank(newTopics);
        Entry existingEntry = myEntryMap.get(entryID);
        if (existingEntry == null) {
            throw new NoSuchEntryException(entryID);
        }
        entry.setMyCreated(existingEntry.getMyCreatedDate());
        entry.updateModification();
        updateEntryTopic(entryID, newTopics);
        myEntryMap.put(entryID, entry);
        new JournalDBAPI(myUserID).save(entryID, entry);
        return entry;
    }

    public void removeEntry(int entryID) throws NoSuchEntryException {
        new JournalDBAPI(myUserID).removeEntry(entryID);
        Entry entry = myEntryMap.get(entryID);
        myEntries.remove(myEntryMap.get(entryID));
        myEntryMap.remove(entryID);
        Set<Topic> topics = entry.getMyTopics();
        for (Topic topic : topics) {
            removeUnusedTopicFromBank(topic.getMyTopic());
        }
    }

    public Entry getRandomEntry(Set<Topic> topics) throws IndexOutOfBoundsException {
        List<Entry> topicalEntries = getTopicalEntries(topics);
        int index = (int) Math.round(Math.random() * (topicalEntries.size() - 1));
        return topicalEntries.get(index);
    }

    public List<Entry> getTopicalEntries(Set<Topic> topics) {
        List<Entry> topicalEntries = new ArrayList<>();
        for (Entry entry : myEntries) {
            if (firstSubsetOfSecond(topics, entry.getMyTopics())) {
                topicalEntries.add(entry);
            }
        }
        return topicalEntries;
    }

    private void removeUnusedTopicFromBank(String topicName) {
        JournalDBAPI journalDBAPI = new JournalDBAPI(myUserID);
        if (!journalDBAPI.anyEntryUsesTopic(topicName)) {
            journalDBAPI.removeTopicFromBank(topicName);
            myTopics.remove(topicName);
        } else {
            throw new RuntimeException(new CannotDeleteTopicException(topicName));
        }
    }


    public int getUserID() {
        return myUserID;
    }

    /*
    ----------------------------
    Helpers:
    ----------------------------
     */

    private void updateEntryTopic(int entryID, Set<Topic> topics) {
        JournalDBAPI journalDBAPI = new JournalDBAPI(myUserID);
        Entry existingEntry = myEntryMap.getOrDefault(entryID, null);
        Map<String, String> existingEntryTopics = existingEntry == null ? new HashMap<>() : existingEntry.myTopicsAsMap();
        Set<String> newTopics = new HashSet<>();
        for (Topic topic : topics) {
            newTopics.add(topic.getMyTopic());
        }
        Map<String, String> newExisting = new HashMap<>();
        for (String existingTopic : existingEntryTopics.keySet()) {
            if (!newTopics.contains(existingTopic)) {
                journalDBAPI.removeTopicFromEntry(entryID, existingTopic);
                removeUnusedTopicFromBank(existingTopic);
            } else newExisting.put(existingTopic, existingEntryTopics.get(existingTopic));
        }
        for (Topic topic : topics) {
            if (!newExisting.containsKey(topic.getMyTopic())) {
                newExisting.put(topic.getMyTopic(), topic.getMyColor());
                journalDBAPI.addToEntryTopic(entryID, topic.getMyTopic(), topic.getMyColor());
            }
        }
        existingEntryTopics.clear();
        for (String newExistingTop : newExisting.keySet()) {
            existingEntryTopics.put(newExistingTop, newExisting.get(newExistingTop));
        }
    }

    private void updateTopicBank(Set<Topic> topics) {
        Map<String, Topic> topicsMap = new HashMap();
        Map<String, String> topicToColor = new HashMap();
        for (Topic topic : topics) {
            if (!myTopics.containsKey(topic.getMyTopic())) {
                topicsMap.put(topic.getMyTopic(), topic);
                topicToColor.put(topic.getMyTopic(), topic.getMyColor());
            }
            myTopics.put(topic.getMyTopic(), topic);
        }
        new JournalDBAPI(myUserID).addToTopicBank(topicToColor); //updates Data topics
    }

    /*
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
    */

    private boolean firstSubsetOfSecond(Set<Topic> first, Set<Topic> second) {
        for (Topic topic : first) {
            boolean contains = false;
            for (Topic topic2 : second) {
                if (topic.getMyTopic().equals(topic2.getMyTopic())) {
                    contains = true;
                    break;
                }
            }
            if (contains == false) return false;
        }
        return true;
    }

    private class EntryComparator implements Comparator<Entry> {
        @Override
        public int compare(Entry e1, Entry e2) {
            return Date.compare(e1.getMyCreatedDate(), e2.getMyCreatedDate());
        }

    }

    //Getters:

    public List<EntryWithID> getMyEntries() {
        return myEntries;
    }

    public Map<Integer, Entry> getMyEntryMap() {
        return myEntryMap;
    }

    public Map<String, Topic> getMyTopics() {
        return myTopics;
    }

    public int getMyUserID() {
        return myUserID;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public String getMyEmail() {
        return myEmail;
    }
}