package BackEnd.Main;

import BackEnd.Exceptions.DateExceptions.InvalidDateFormatException;
import Data.Communication.DataParser;
import Data.Communication.Main;
import BackEnd.Main.Components.Date;
import BackEnd.Main.Components.Topic;

import java.sql.SQLException;
import java.util.*;

public class Journal {
    List<Entry> myEntries;
    Map<Integer, Entry> myEntryMap;
    Set<Topic> myTopics;
    int myUserID;
    Main myDataManager;

    /*
    Sorter and manager of Entries; has full access privileges on Entries
     */

    //used prepared statements in Java so that you dont have to recompile queries

    public Journal(int userID) throws SQLException, InvalidDateFormatException {
        myDataManager = new Main(userID);
        List<Map<String, Object>> entryMap = myDataManager.loadEntryMap(); //uses primary IDs and maps them to Entry
        List<Map<String, Object>> entryTopic = myDataManager.loadEntryTopics();
        List<Map<String, Object>> topics = myDataManager.loadTopicBank();
        myEntryMap = DataParser.parseEntryMap(entryMap, entryTopic);
        myEntries = DataParser.parseEntries(myEntryMap);
        myTopics = DataParser.parseTopics(topics);
        myUserID = userID;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    public void createEntry(Set<Topic> topics, String text, String title, Date date, String color)
    throws SQLException, IndexOutOfBoundsException, ClassCastException{

        updateTopicBank(topics);
        addEntry(text, topics, title, date, color);
    }

    public void saveEntry(int entryID, Set<Topic> topics, String text, String title, Date creationDate)
    throws SQLException{

        updateTopicBank(topics);
        modifyEntry(entryID, topics, text, title, creationDate);
    }

    public void removeEntry(int entryID) throws SQLException{
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

    private void updateTopicBank(Set<Topic> topics) throws SQLException{
        Set<Topic> topicsCpy = new HashSet<>(topics);
        topicsCpy.removeAll(myTopics); //looks for new topics, makes map with which to update topic bank in DB
        Map<String, String> topicToColor = new HashMap<>();
        for(Topic topic : topicsCpy){
            topicToColor.put(topic.getMyTopic(), topic.getMyColor());
        }
        myDataManager.addToTopicBank(topicToColor); //updates DB topics
        myTopics.addAll(topics); //updates topic bank datatype
    }

    private void addEntry(String text, Set<Topic> topics, String title, Date date, String color)
            throws SQLException, IndexOutOfBoundsException, ClassCastException{

        Entry entry = date == null ? new Entry(title, topics, text, color) : new Entry(title, topics, text, color, date);
        int id = myDataManager.createEntry(entry);

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

        myDataManager.save(entryID, e);
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