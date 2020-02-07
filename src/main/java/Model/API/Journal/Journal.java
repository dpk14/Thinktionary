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
        List<Map<String, Object>> entryTable = journalDBAPI.loadEntryTable(); //uses primary IDs and maps them to Entry
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

    public int createEntry(Entry entry) throws TopicBankAddException {
        updateTopicBank(entry.getMyTopics());
        return addEntry(entry);
    }

    public void saveEntry(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException {
        updateTopicBank(entry.getMyTopics());
        modifyEntry(entryID, entry);
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
            Set<Topic> topicsCpy = new HashSet<>(topics);
            if(containsAll(topicsCpy, entry.getMyTopics())) {
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

    private void updateTopicBank(Set<Topic> topics) throws TopicBankAddException {
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
        new JournalDBAPI(myUserID).addToTopicBank(topicToColor); //updates Data topics
    }

    private int addEntry(Entry entry) throws IndexOutOfBoundsException, ClassCastException, TopicBankAddException {

        int id = new JournalDBAPI(myUserID).createEntry(entry);

        myEntryMap.put(id, entry);
        myEntries.add(entry);
        return id;
    }

    private void modifyEntry(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException {
        entry.updateModification();
        String oldCreation = myEntryMap.get(entryID).getMyCreatedasString();
        myEntryMap.put(entryID, entry);

        if(!entry.getMyCreatedasString().equals(oldCreation)){ // if creation date has been modified, adjust order
            reorder(entry);
        }

        new JournalDBAPI(myUserID).save(entryID, entry);
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
            return Date.compare(e1.getMyCreated(), e2.getMyCreated());
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