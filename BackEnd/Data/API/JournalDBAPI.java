package BackEnd.Data.API;

import BackEnd.API.Journal.Entry;
import BackEnd.API.Journal.EntryComponents.Topic;
import BackEnd.Data.Lib.Paths.DBFileNames;
import BackEnd.Data.Utils.DBUtils;
import BackEnd.Data.Lib.Paths.DBNames;
import BackEnd.Data.Lib.Paths.DBUrls;
import BackEnd.Data.Lib.SQLStrings.SQLQuery;
import BackEnd.Data.Lib.SQLStrings.TableNames;
import BackEnd.Data.Utils.ParserUtils;
import BackEnd.ErrorHandling.Errors.CorruptDBError;
import BackEnd.ErrorHandling.Exceptions.DateExceptions.InvalidDateException;
import BackEnd.ErrorHandling.Exceptions.NoSuchEntryException;

import java.sql.*;
import java.util.*;

public class JournalDBAPI {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    private String myDBUser;
    private String myDBPassword;
    private String myDBUrl;
    int myUserID;

    public JournalDBAPI(int userID, String dbUser, String dbPassword, String dbUrl){
        myUserID = userID;
        myDBUser = dbUser;
        myDBPassword = dbPassword;
        if(dbUrl == null) {
            myDBUrl = DBUrls.getURL(DBNames.getSQLITE(), DBFileNames.getMainDbPath());
        }
        else myDBUrl = dbUrl;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    //Loading:

    public Map<Integer, Entry> loadEntryMap(List<Map<String, Object>> entryTopic) {
        List<Map<String, Object>> table = loadTable(TableNames.getEntryInfo());
        return ParserUtils.parseEntryMap(table, entryTopic);
    }

    public List<Map<String, Object>> loadEntryTopics() {
        return loadTable(TableNames.getEntryToTopic());
    }

    public Set<Topic> loadTopicBank() {
        List<Map<String, Object>> topics = loadTable(TableNames.getUserTopic());
        return ParserUtils.parseTopics(topics);
    }

    public List<Entry> loadEntryList(Map<Integer, Entry> myEntryMap) {
        List<Entry> entries = ParserUtils.parseEntries(myEntryMap);
        Collections.sort(entries, new Entry.EntryComparator());
        return entries;
    }

    //Saving:

    public int createEntry(Entry entry) {
        int entryID = addToEntryInfo(entry);
        addTopics(TableNames.getUserTopic(), entryID, entry.getMyTopicsAsMap());
        return entryID;
    }

    public void addToTopicBank(Map<String, String> topicToColor){
        addTopics(TableNames.getUserTopic(), myUserID, topicToColor);
    }

    public void removeEntry(int entryID) throws NoSuchEntryException {
        removeEntry(entryID, TableNames.getEntryToTopic());
        removeEntry(entryID, TableNames.getEntryInfo());
    }

    public void save(int entryID, Entry entry){
        addTopics(TableNames.getUserTopic(), entryID, entry.getMyTopicsAsMap());

        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getEntryInfo());
        map.put(2, Integer.toString(myUserID));
        map.put(3, entry.getmyTitleasString());
        map.put(4, entry.getMyColorasString());
        map.put(5, entry.getMyCreatedasString());
        map.put(6, entry.getMyModfiedasString());
        map.put(7, Integer.toString(entryID));
        try {
            DBUtils.userAction(map, SQLQuery.modifyEntryInfo(), myDBUrl, myDBUser, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    /*
    ----------------------------
    Private Helpers:
    ----------------------------
     */

    private List<Map<String, Object>> loadTable(String tableName) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, Integer.toString(myUserID));
        List<Map<String, Object>> ret = new ArrayList<>();
        try{
            return DBUtils.userQuery(map, SQLQuery.loadTable(), myDBUrl, myDBUser, myDBPassword);
        }
        catch (SQLException e){
            throw new CorruptDBError(e);
        }
    }

    private int addToEntryInfo(Entry entry) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitleasString());
        map.put(3, entry.getMyColorasString());
        map.put(4, entry.getMyCreatedasString());
        map.put(5, entry.getMyModfiedasString());
        try {
            DBUtils.userQuery(map, SQLQuery.addEntry(), myDBUrl, myDBUser, myDBPassword);
            List<Map<String, Object>> ent = DBUtils.userQuery(map, SQLQuery.getEntry(), myDBUrl, myDBUser, myDBPassword);
            return ParserUtils.getEntryID(ent);
        }
        catch(Exception e){
            throw new CorruptDBError(e);
        }
    }

    private void addTopics(String tableName, int ID, Map<String, String> topicToColor){
        try {
            Map<Integer, String> map = new HashMap<>();
            for (String topic : topicToColor.keySet()) {
                String color = topicToColor.get(topic);
                map.put(1, tableName);
                map.put(2, Integer.toString(ID));
                map.put(3, topic);
                map.put(4, color);
                DBUtils.userAction(map, SQLQuery.addTopic(), myDBUrl, myDBUser, myDBPassword);
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
            System.out.println(e.getStackTrace());
        }
    }

    private void removeEntry(int entryID, String tableName) throws NoSuchEntryException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, Integer.toString(myUserID));
        map.put(3, Integer.toString(entryID));
        try {
            List<Map<String, Object>> toBeRemoved = DBUtils.userQuery(map, SQLQuery.getByEntryID(), myDBUrl, myDBUser, myDBPassword);
            if(toBeRemoved.size() == 0){
                throw new NoSuchEntryException(entryID);
            }
            DBUtils.userAction(map, SQLQuery.remove(), myDBUrl, myDBUser, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

}
