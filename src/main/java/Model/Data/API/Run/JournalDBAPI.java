package Model.Data.API.Run;

import Model.API.Journal.Entry;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Equals;
import Model.Data.SQL.QueryObjects.Parameter;
import Model.Data.Utils.DBUtils;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Model.API.Journal.JournalDBParser;
import Model.ErrorHandling.Errors.CorruptDBError;
import Model.ErrorHandling.Exceptions.DBExceptions.ModifyEntryException;
import Model.ErrorHandling.Exceptions.DBExceptions.TopicBankAddException;
import Model.ErrorHandling.Exceptions.EntryByTopicException;
import Model.ErrorHandling.Exceptions.NoSuchEntryException;
import Model.ErrorHandling.Exceptions.RemoveTopicException;

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

    //Communicators:

    public boolean usesTopic(String topicName) throws EntryByTopicException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), Integer.toString(myUserID)));
        conditions.add(new Equals(ColumnInfo.getTOPIC(), topicName));
        try {
            List<Map<String, Object>> entries = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.getEntryToTopic(), conditions), myDBUrl, myDBUsername, myDBPassword);
            return entries.size() > 0;
        }
        catch(SQLException e){
            throw new EntryByTopicException(e);
        }
    }

    public void removeTopicFromBank(String topicName) throws RemoveTopicException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), Integer.toString(myUserID)));
        conditions.add(new Equals(ColumnInfo.getTOPIC(), topicName));
        try {
            DBUtils.userAction(SQLQueryBuilder.remove(TableNames.getUserTopic(), conditions), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new RemoveTopicException(e);
        }
    }

    public void removeTopicFromEntry(int entryID, String topicName) throws RemoveTopicException {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getUSERID(), Integer.toString(myUserID)));
        conditions.add(new Equals(ColumnInfo.getEntryId(), Integer.toString(entryID)));
        conditions.add(new Equals(ColumnInfo.getTOPIC(), topicName));
        try {
            DBUtils.userAction(SQLQueryBuilder.remove(TableNames.getEntryToTopic(), conditions), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new RemoveTopicException(e);
        }
    }

    public void save(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERID(), Integer.toString(myUserID)));
        parameters.add(new Parameter(ColumnInfo.getTITLE(), entry.getmyTitle()));
        parameters.add(new Parameter(ColumnInfo.getTEXT(), entry.getmyText()));
        parameters.add(new Parameter(ColumnInfo.getCREATED(), entry.getMyCreated()));
        parameters.add(new Parameter(ColumnInfo.getMODIFIED(), entry.getMyModfied()));

        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Equals(ColumnInfo.getEntryId(), Integer.toString(entryID)));

        try {
            DBUtils.userAction(SQLQueryBuilder.modify(TableNames.getEntryInfo(), parameters, conditions), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            throw new ModifyEntryException(e.toString());
        }
    }

    public int addToEntryInfo(Entry entry) {
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(new Parameter(ColumnInfo.getUSERID(), Integer.toString(myUserID)));
        parameters.add(new Parameter(ColumnInfo.getTITLE(), entry.getmyTitle()));
        parameters.add(new Parameter(ColumnInfo.getTEXT(), entry.getmyText()));
        parameters.add(new Parameter(ColumnInfo.getCREATED(), entry.getMyCreated()));
        parameters.add(new Parameter(ColumnInfo.getMODIFIED(), entry.getMyModfied()));

        String query = SQLQueryBuilder.insert(TableNames.getEntryInfo(), parameters) + " " + SQLQueryBuilder.getLastInsertID();
        try{
            List<Map<String, Object>> ret = DBUtils.userQuery(query, myDBUrl, myDBUsername, myDBPassword);
            return JournalDBParser.getEntryID(ret);
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
                DBUtils.userAction(map, SQLQueryBuilder.addTopic(), myDBUrl, myDBUsername, myDBPassword);
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
                DBUtils.userAction(map, SQLQueryBuilder.addToEntryTopic(), myDBUrl, myDBUsername, myDBPassword);
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
            List<Map<String, Object>> toBeRemoved = DBUtils.userQuery(map, SQLQueryBuilder.getByEntryID(TableNames.getEntryInfo()), myDBUrl, myDBUsername, myDBPassword);
            if(toBeRemoved.size() == 0){
                throw new NoSuchEntryException(entryID);
            }
            DBUtils.userAction(map, SQLQueryBuilder.remove(TableNames.getEntryInfo()), myDBUrl, myDBUsername, myDBPassword);
            DBUtils.userAction(map, SQLQueryBuilder.remove(TableNames.getEntryToTopic()), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public int getMyUserID() {
        return myUserID;
    }
}

}