package Model.Data.API;

import Model.API.Journal.Entry;
import Model.Data.Lib.SQLStrings.ColumnInfo;
import Model.Data.Utils.DBUtils;
import Model.Data.Lib.SQLStrings.SQLQuery;
import Model.Data.Lib.SQLStrings.TableNames;
import Model.API.Journal.JournalDBParser;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.DBExceptions.ModifyEntryException;
import Model.ErrorHandling.Exceptions.DBExceptions.TopicBankAddException;
import Model.ErrorHandling.Exceptions.NoSuchEntryException;

import java.sql.*;
import java.util.*;

public class JournalDBAPI extends DBAPI {

    int myUserID;

    public JournalDBAPI(int userID, String dbUsername, String dbPassword, String dbUrl){
        super(dbUsername, dbPassword, dbUrl);
        myUserID = userID;
    }

    public JournalDBAPI(int userID){
        super();
        myUserID = userID;
    }

    /*
    ----------------------------
    Public API:
    ----------------------------
     */

    //Loading:

    public List<Map<String, Object>> loadEntryTable() {
        return loadTableByParamater(TableNames.getEntryInfo(), ColumnInfo.getUSERID(), Integer.toString(myUserID));
    }

    public List<Map<String, Object>> loadEntryTopicsTable() {
        return loadTableByParamater(TableNames.getEntryToTopic(), ColumnInfo.getUSERID(), Integer.toString(myUserID));
    }

    public List<Map<String, Object>> loadTopicBankTable() {
        return loadTableByParamater(TableNames.getUserTopic(), ColumnInfo.getUSERID(), Integer.toString(myUserID));
    }

    //Saving:

    public int createEntry(Entry entry) throws TopicBankAddException {
        int entryID = addToEntryInfo(entry);
        addTopics(TableNames.getUserTopic(), entryID, entry.getMyTopicsAsMap());
        return entryID;
    }

    public void addToTopicBank(Map<String, String> topicToColor) throws TopicBankAddException {
        addTopics(TableNames.getUserTopic(), myUserID, topicToColor);
    }

    public void removeEntry(int entryID) throws NoSuchEntryException {
        removeEntry(entryID, TableNames.getEntryToTopic());
        removeEntry(entryID, TableNames.getEntryInfo());
    }

    public void save(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException {
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
            throw new ModifyEntryException(e.toString());
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

    private void addTopics(String tableName, int ID, Map<String, String> topicToColor) throws TopicBankAddException {
        Map<Integer, String> map = new HashMap<>();
        for (String topic : topicToColor.keySet()) {
            String color = topicToColor.get(topic);
            map.put(1, tableName);
            map.put(2, Integer.toString(ID));
            map.put(3, topic);
            map.put(4, color);
            try {
                DBUtils.userAction(map, SQLQuery.addTopic(), myDBUrl, myDBUsername, myDBPassword);
            }
            catch(SQLException e){
                throw new TopicBankAddException(e.toString());
            }
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
