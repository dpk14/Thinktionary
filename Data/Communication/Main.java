package Data.Communication;

import BackEnd.Main.Entry;
import Data.Communication.Utils.DBUtils;
import Data.Lib.Labels;
import Data.Lib.SQLQuery;
import Data.Lib.TabelNames;

import java.sql.*;
import java.util.*;

public class Main {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    private final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";
    private final String USER = "testuser";
    private final String PASSWORD = "test623";
    int myUserID;

    public Main(int userID){
        myUserID = userID;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    //Loading:

    public List<Map<String, Object>> loadEntryMap() throws SQLException {
        return loadTable(TabelNames.getEntryInfo());
    }

    public List<Map<String, Object>> loadEntryTopics() throws SQLException{
        return loadTable(TabelNames.getEntryToTopic());
    }

    public List<Map<String, Object>> loadTopicBank() throws SQLException{
        return loadTable(TabelNames.getUserTopic());
    }

    //Saving:

    public int createEntry(Entry entry) throws SQLException, IndexOutOfBoundsException, ClassCastException{
        int entryID = addToEntryInfo(entry);
        addTopics(TabelNames.getUserTopic(), entryID, entry.getMyTopicsAsMap());
        return entryID;
    }

    public void addToTopicBank(Map<String, String> topicToColor) throws SQLException{
        addTopics(TabelNames.getUserTopic(), myUserID, topicToColor);
    }

    public void removeEntry(int entryID) throws SQLException{
        remove(entryID, TabelNames.getEntryToTopic());
        remove(entryID, TabelNames.getEntryInfo());
    }

    public void save(int entryID, Entry e) throws SQLException{
        addTopics(TabelNames.getUserTopic(), entryID, e.getMyTopicsAsMap());

        Map<Integer, String> map = new HashMap<>();
        map.put(1, TabelNames.getEntryInfo());
        map.put(2, Integer.toString(myUserID));
        map.put(3, Integer.toString(entryID));
        map.put(4, e.getmyTitleasString());
        map.put(5, e.getMyColorasString());
        map.put(6, e.getMyCreatedasString());
        map.put(7, e.getMyModfiedasString());
        DBUtils.userAction(map, SQLQuery.modifyEntry(), URL, USER, PASSWORD);
    }

    /*
    ----------------------------
    Private Helpers:
    ----------------------------
     */

    private List<Map<String, Object>> loadTable(String tableName) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, Integer.toString(myUserID));
        return DBUtils.userQuery(map, SQLQuery.loadTable(), URL, USER, PASSWORD);
    }

    private int addToEntryInfo(Entry entry) throws SQLException, IndexOutOfBoundsException, ClassCastException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitleasString());
        map.put(3, entry.getMyColorasString());
        map.put(4, entry.getMyCreatedasString());
        map.put(5, entry.getMyModfiedasString());
        DBUtils.userAction(map, SQLQuery.addEntry(), URL, USER, PASSWORD);
        List<Map<String, Object>> ent = DBUtils.userQuery(map, SQLQuery.getEntryID(), URL, USER, PASSWORD);
        return (int) ent.get(0).get(Labels.getEntryId());
    }

    private void addTopics(String tableName, int ID, Map<String, String> topicToColor) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        for(String topic : topicToColor.keySet()){
            String color = topicToColor.get(topic);
            map.put(1, tableName);
            map.put(2, Integer.toString(ID));
            map.put(3, topic);
            map.put(4, color);
            DBUtils.userAction(map, SQLQuery.addTopic(), URL, USER, PASSWORD);
        }
    }

    private void remove(int entryID, String tableName) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, Integer.toString(myUserID));
        map.put(3, Integer.toString(entryID));
        DBUtils.userAction(map, SQLQuery.remove(), URL, USER, PASSWORD);
    }



}
