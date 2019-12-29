package BackEnd.Main;

import BackEnd.DataBase.UserDataManager;
import BackEnd.Main.Components.Date;
import BackEnd.Main.Components.Topic;

import java.sql.SQLException;
import java.util.*;

public class Journal {
    List<Entry> myEntries;
    Map<Integer, Entry> myEntryMap;
    Set<Topic> myTopics;
    int myUserID;
    UserDataManager myDataManager;

    /*
    Sorter and manager of Entries; has full access priveleges on Entries
     */

    //used prepared statements in Java so that you dont have to recompile queries

    public Journal(int userID){
        try {
            myDataManager = new UserDataManager(userID);
            List<Map<String, Object>> entryMap = myDataManager.loadEntryMap(); //uses primary IDs and maps them to Entry
            List<Map<String, Object>> entryTopic = myDataManager.loadEntryTopics();
            List<Map<String, Object>> topics = myDataManager.loadTopics();
            myEntryMap = DataParser.parseEntryMap(entryMap, entryTopic);
            myEntries = DataParser.parseEntries(myEntryMap);
            myTopics = DataParser.parseTopics(topics);
            myUserID = userID;
        }
        catch(SQLException e){
            e.printStackTrace();
            //EXIT
        }
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    public void addEntry(Set<Topic> topics, String text, String title, Date date, String color){
        myTopics.addAll(topics);
        myDataManager.addTopics(topics);

        Entry entry = date == null ? new Entry(title, topics, text, color) : new Entry(title, topics, text, color, date);
        int id = myDataManager.createEntry(entry);

        myEntryMap.put(id, entry);
        myEntries.add(entry);
        myDataManager.addEntry(entry);
    }

    public void updateEntry(int entryID, Set<Topic> topics, String text, String title, Date creationDate){
        Entry e = myEntryMap.get(entryID);
        e.setMyTitle(title);
        e.updateModification();
        e.setText(text);
        if(!creationDate.equals(e.getMyCreated())){ // if creation date has been modified, adjust order
            e.setMyCreated(creationDate);
            reorder(e);
        }
        updateTopics(e, topics);
        myDataManager.save(e, myTopics);
    }

    public void removeEntry(int entryID) {
        myDataManager.removeEntry(entryID);
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

    /*
    ----------------------------
    Helpers:
    ----------------------------
     */

    private void updateTopics(Entry e, Set<Topic> topics){
        for(Topic topic : topics) {
            if(!myTopics.contains(topic)){
                topics.add(topic);
            }
        }
        e.setMyTopics(topics);
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

}