package src.main.java.BackEnd.Data.API;

import src.main.java.BackEnd.API.Journal.Entry;
import src.main.java.BackEnd.Data.Lib.SQLStrings.ColumnLabels;
import src.main.java.BackEnd.Data.Utils.DBUtils;
import src.main.java.BackEnd.Data.Lib.SQLStrings.SQLQuery;
import src.main.java.BackEnd.Data.Lib.SQLStrings.TableNames;
import src.main.java.BackEnd.API.Journal.JournalDBParser;
import src.main.java.BackEnd.ErrorHandling.Errors.CorruptDBError;
import src.main.java.BackEnd.ErrorHandling.Exceptions.NoSuchEntryException;

import java.sql.*;
import java.util.*;

public class JournalDBAPI extends DBAPI{

    /*
    UserID | UserName | Password      name: login

    UserID | Topic | Color           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic | Color         entryTopic

     */

    int myUserID;

    public JournalDBAPI(int userID, String dbUsername, String dbPassword, String dbUrl){
        super(dbUsername, dbPassword, dbUrl);
        myUserID = userID;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    //Loading:

    public List<Map<String, Object>> loadEntryTable() {
        return loadTableByParamater(TableNames.getEntryInfo(), ColumnLabels.getUSERID(), Integer.toString(myUserID));
    }

    public List<Map<String, Object>> loadEntryTopicsTable() {
        return loadTableByParamater(TableNames.getEntryToTopic(), ColumnLabels.getUSERID(), Integer.toString(myUserID));
    }

    public List<Map<String, Object>> loadTopicBankTable() {
        return loadTableByParamater(TableNames.getUserTopic(), ColumnLabels.getUSERID(), Integer.toString(myUserID));
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
        map.put(3, entry.getmyTitle());
        map.put(4, entry.getmyText());
        map.put(5, entry.getMyCreatedasString());
        map.put(6, entry.getMyModfiedasString());
        map.put(7, Integer.toString(entryID));
        try {
            DBUtils.userAction(map, SQLQuery.modifyEntryInfo(), myDBUrl, myDBUsername, myDBPassword);
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

    private int addToEntryInfo(Entry entry) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitle());
        map.put(3, entry.getmyText());
        map.put(4, entry.getMyCreatedasString());
        map.put(5, entry.getMyModfiedasString());
        try {
            DBUtils.userQuery(map, SQLQuery.addEntry(), myDBUrl, myDBUsername, myDBPassword);
            List<Map<String, Object>> ent = DBUtils.userQuery(map, SQLQuery.getEntry(), myDBUrl, myDBUsername, myDBPassword);
            return JournalDBParser.getEntryID(ent);
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
                DBUtils.userAction(map, SQLQuery.addTopic(), myDBUrl, myDBUsername, myDBPassword);
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
            List<Map<String, Object>> toBeRemoved = DBUtils.userQuery(map, SQLQuery.getByEntryID(), myDBUrl, myDBUsername, myDBPassword);
            if(toBeRemoved.size() == 0){
                throw new NoSuchEntryException(entryID);
            }
            DBUtils.userAction(map, SQLQuery.remove(), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

}
