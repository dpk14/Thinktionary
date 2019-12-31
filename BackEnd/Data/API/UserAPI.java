package BackEnd.Data.API;

import BackEnd.API.Entry;
import BackEnd.API.EntryComponents.Topic;
import BackEnd.API.Exceptions.DateExceptions.InvalidDateFormatException;
import BackEnd.API.Journal;
import BackEnd.Data.Utils.DBUtils;
import BackEnd.Data.Lib.Paths.DBNames;
import BackEnd.Data.Lib.Paths.DBUrls;
import BackEnd.Data.Lib.SQLStrings.ColumnLabels;
import BackEnd.Data.Lib.SQLStrings.SQLQuery;
import BackEnd.Data.Lib.SQLStrings.TableNames;
import BackEnd.Data.Utils.ParserUtils;

import java.sql.*;
import java.util.*;

public class UserAPI {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    private final String USER = "testuser";
    private final String PASSWORD = "test623";
    int myUserID;

    public UserAPI(int userID){
        myUserID = userID;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    //Loading:

    public Map<Integer, Entry> loadEntryMap(List<Map<String, Object>> entryTopic) throws SQLException, InvalidDateFormatException {
        List<Map<String, Object>> table = loadTable(TableNames.getEntryInfo());
        return ParserUtils.parseEntryMap(table, entryTopic);
    }

    public List<Map<String, Object>> loadEntryTopics() throws SQLException{
        return loadTable(TableNames.getEntryToTopic());
    }

    public Set<Topic> loadTopicBank() throws SQLException{
        List<Map<String, Object>> topics = loadTable(TableNames.getUserTopic());
        return ParserUtils.parseTopics(topics);
    }

    public List<Entry> loadEntryList(Map<Integer, Entry> myEntryMap) {
        List<Entry> entries = ParserUtils.parseEntries(myEntryMap);
        Collections.sort(entries, new Entry.EntryComparator());
        return entries;
    }

    //Saving:

    public int createEntry(Entry entry) throws SQLException, IndexOutOfBoundsException, ClassCastException{
        int entryID = addToEntryInfo(entry);
        addTopics(TableNames.getUserTopic(), entryID, entry.getMyTopicsAsMap());
        return entryID;
    }

    public void addToTopicBank(Map<String, String> topicToColor) throws SQLException{
        addTopics(TableNames.getUserTopic(), myUserID, topicToColor);
    }

    public void removeEntry(int entryID) throws SQLException{
        remove(entryID, TableNames.getEntryToTopic());
        remove(entryID, TableNames.getEntryInfo());
    }

    public void save(int entryID, Entry e) throws SQLException{
        addTopics(TableNames.getUserTopic(), entryID, e.getMyTopicsAsMap());

        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getEntryInfo());
        map.put(2, Integer.toString(myUserID));
        map.put(3, e.getmyTitleasString());
        map.put(4, e.getMyColorasString());
        map.put(5, e.getMyCreatedasString());
        map.put(6, e.getMyModfiedasString());
        map.put(7, Integer.toString(entryID));
        DBUtils.userAction(map, SQLQuery.modifyEntryInfo(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
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
        return DBUtils.userQuery(map, SQLQuery.loadTable(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
    }

    private int addToEntryInfo(Entry entry) throws SQLException, IndexOutOfBoundsException, ClassCastException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitleasString());
        map.put(3, entry.getMyColorasString());
        map.put(4, entry.getMyCreatedasString());
        map.put(5, entry.getMyModfiedasString());
        List<Map<String, Object>> ent = DBUtils.userQuery(map, SQLQuery.getEntryID(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
        return ParserUtils.getEntryID(ent);
    }

    private void addTopics(String tableName, int ID, Map<String, String> topicToColor) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        for(String topic : topicToColor.keySet()){
            String color = topicToColor.get(topic);
            map.put(1, tableName);
            map.put(2, Integer.toString(ID));
            map.put(3, topic);
            map.put(4, color);
            DBUtils.userAction(map, SQLQuery.addTopic(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
        }
    }

    private void remove(int entryID, String tableName) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, Integer.toString(myUserID));
        map.put(3, Integer.toString(entryID));
        DBUtils.userAction(map, SQLQuery.remove(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
    }

}
