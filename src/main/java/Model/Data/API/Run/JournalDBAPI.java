package Model.Data.API.Run;

import Model.API.Journal.Entry;
import Model.Data.SQL.ColumnInfo;
import Model.Data.Utils.DBUtils;
import Model.Data.SQL.SQLQuery;
import Model.Data.SQL.TableNames;
import Model.API.Journal.JournalDBParser;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.DBExceptions.ModifyEntryException;
import Model.ErrorHandling.Exceptions.DBExceptions.TopicBankAddException;
import Model.ErrorHandling.Exceptions.NoSuchEntryException;

import java.sql.*;
import java.util.*;

public class
JournalDBAPI extends RunDBAPI {

    int myUserID;

    public JournalDBAPI(){

    }

    public JournalDBAPI(int userID){
        super();
        myUserID = userID;
    }

    /*
    ----------------------------
    Public Run:
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

    public void save(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getEntryInfo());
        map.put(2, Integer.toString(myUserID));
        map.put(3, entry.getmyTitle());
        map.put(4, entry.getmyText());
        map.put(5, entry.getMyCreated());
        map.put(6, entry.getMyModfied());
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

    public int addToEntryInfo(Entry entry) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitle());
        map.put(3, entry.getmyText());
        map.put(4, entry.getMyCreated());
        map.put(5, entry.getMyModfied());
        try {
            DBUtils.userAction(map, SQLQuery.addEntry(), myDBUrl, myDBUsername, myDBPassword);
            List<Map<String, Object>> ent = DBUtils.userQuery(map, SQLQuery.getEntry(), myDBUrl, myDBUsername, myDBPassword);
            return JournalDBParser.getEntryID(ent);
        }
        catch(Exception e){
            throw new CorruptDBError(e);
        }
    }

    public void addToTopicBank(Map<String, String> topicToColor) throws TopicBankAddException {
        for (String topic : topicToColor.keySet()) {
            Map<Integer, String> map = new HashMap<>();
            String color = topicToColor.get(topic);
            map.put(1, Integer.toString(myUserID));
            map.put(2, topic);
            map.put(3, color);
            try {
                DBUtils.userAction(map, SQLQuery.addTopic(), myDBUrl, myDBUsername, myDBPassword);
            }
            catch(SQLException e){
                throw new TopicBankAddException(e.toString());
            }
            }
    }

    public void addToEntryTopic(int entryID, int userID, String topic, String color) throws TopicBankAddException {
        Map<Integer, String> map = new HashMap<>();
            map.put(1, Integer.toString(entryID));
            map.put(2, Integer.toString(userID));
            map.put(3, topic);
            map.put(4, color);
            try {
                DBUtils.userAction(map, SQLQuery.addToEntryTopic(), myDBUrl, myDBUsername, myDBPassword);
            }
            catch(SQLException e){
                throw new TopicBankAddException(e.toString());
            }
    }

    public void removeEntry(int entryID) throws NoSuchEntryException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, Integer.toString(entryID));
        try {
            List<Map<String, Object>> toBeRemoved = DBUtils.userQuery(map, SQLQuery.getByEntryID(TableNames.getEntryInfo()), myDBUrl, myDBUsername, myDBPassword);
            if(toBeRemoved.size() == 0){
                throw new NoSuchEntryException(entryID);
            }
            DBUtils.userAction(map, SQLQuery.remove(TableNames.getEntryInfo()), myDBUrl, myDBUsername, myDBPassword);
            DBUtils.userAction(map, SQLQuery.remove(TableNames.getEntryToTopic()), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public int getMyUserID() {
        return myUserID;
    }
}
