package Model.API.Journal;

import Model.Data.API.Run.JournalDBAPI;
import Model.API.Journal.EntryComponents.Date;
import Model.API.Journal.EntryComponents.Topic;
import Model.ErrorHandling.Exceptions.DBExceptions.ModifyEntryException;
import Model.ErrorHandling.Exceptions.DBExceptions.TopicBankAddException;
import Model.ErrorHandling.Exceptions.NoSuchEntryException;

import java.util.*;

public class Journal {
    List<Entry> myEntries;
    Map<Integer, Entry> myEntryMap;
    Map<String, Topic> myTopics;
    int myUserID;
    /*
    Sorter and manager of Entries; has full access privileges on Entries
     */

    //used prepared statements in Java so that you dont have to recompile queries

    public Journal(){

    }

    public Journal(List<Entry> entries, Map<Integer, Entry> entryMap, Map<String, Topic> topics, int userID){
        myEntries = entries;
        myEntryMap = entryMap;
        myTopics = topics;
        myUserID = userID;
    }

    public Journal(int userID){
        JournalDBAPI journalDBAPI = new JournalDBAPI(userID);
        List<Map<String, Object>> entryTopic = journalDBAPI.loadEntryTopicsTable();
        List<Map<String, Object>> entryTable = journalDBAPI.loadEntryTable(); //uses primary IDs and maps them to Entry'
        myEntryMap = JournalDBParser.parseEntryMap(entryTable, entryTopic);
        List<Map<String, Object>> topicTable = journalDBAPI.loadTopicBankTable();
        myTopics = JournalDBParser.parseTopics(topicTable);
        myEntries = JournalDBParser.parseEntries(myEntryMap);
        myUserID = userID;

    }

    /*
    ----------------------------
    Public Run:
    ----------------------------
     */

    public int createEntry(Entry entry) throws IndexOutOfBoundsException, ClassCastException, TopicBankAddException{
        updateTopicBank(entry.getMyTopics());
        int entryID = new JournalDBAPI(myUserID).addToEntryInfo(entry);
        updateEntryTopic(entryID, entry.getMyTopics());
        myEntryMap.put(entryID, entry);
        myEntries.add(entry);
        return entryID;
    }

    public void saveEntry(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException, NoSuchEntryException {
        Set<Topic> newTopics = entry.getMyTopics();
        updateTopicBank(newTopics);
        entry.updateModification();
        try {
            Entry existingEntry = myEntryMap.get(entryID);
            updateEntryTopic(entryID, newTopics);
            String oldCreation = existingEntry.getMyCreated();
            myEntryMap.put(entryID, entry);

            if(!entry.getMyCreated().equals(oldCreation)){ // if creation date has been modified, adjust order
                reorder(entry);
            }

            new JournalDBAPI(myUserID).save(entryID, entry);
        }
        catch(NullPointerException e){
            throw new NoSuchEntryException(entryID);
        }

    }

    public void removeEntry(int entryID) throws NoSuchEntryException{
        new JournalDBAPI(myUserID).removeEntry(entryID);
        myEntries.remove(myEntryMap.get(entryID));
        myEntryMap.remove(entryID);
    }

    public Entry getRandomEntry(Set<Topic> topics) throws IndexOutOfBoundsException{
        List<Entry> topicalEntries = getTopicalEntries(topics);
        int index = (int) Math.round(Math.random()*(topicalEntries.size()-1));
        return topicalEntries.get(index);
    }

    public List<Entry> getTopicalEntries(Set<Topic> topics){
        List<Entry> topicalEntries = new ArrayList<>();
        for(Entry entry : myEntries){
            System.out.println(entry.getMyTopics()==null);
            if(firstSubsetOfSecond(topics, entry.getMyTopics())) {
                topicalEntries.add(entry);
            }
        }
        return topicalEntries;
    }

    public int getUserID() {
        return myUserID;
    }


    /*
    ----------------------------
    Helpers:
    ----------------------------
     */

    private void updateEntryTopic(int entryID, Set<Topic> topics) throws TopicBankAddException {
        Entry existingEntry = myEntryMap.getOrDefault(entryID, null);
        Map<String, String> entryTopics = existingEntry == null ? new HashMap<>() : existingEntry.myTopicsAsMap();
        for(Topic topic : topics){
            if(!entryTopics.containsKey(topic.getMyTopic())){
                entryTopics.put(topic.getMyTopic(), topic.getMyColor());
                new JournalDBAPI(myUserID).addToEntryTopic(entryID, myUserID, topic.getMyTopic(), topic.getMyColor());
            }
        }
    }

    private void updateTopicBank(Set<Topic> topics) throws TopicBankAddException {
        Map<String, Topic> topicsMap = new HashMap();
        Map<String, String> topicToColor = new HashMap();
        for(Topic topic : topics){
            if(!myTopics.containsKey(topic.getMyTopic())) {
                topicsMap.put(topic.getMyTopic(), topic);
                topicToColor.put(topic.getMyTopic(), topic.getMyColor());
            }
            myTopics.put(topic.getMyTopic(), topic);
        }
        new JournalDBAPI(myUserID).addToTopicBank(topicToColor); //updates Data topics
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

    private boolean firstSubsetOfSecond(Set<Topic> first, Set<Topic> second){
        for(Topic topic : first){
            boolean contains = false;
            for(Topic topic2 : second){
                if (topic.getMyTopic().equals(topic2.getMyTopic())){
                    contains = true;
                    break;
                }
            }
            if(contains == false) return false;
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

    public List<Entry> getMyEntries() {
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

}