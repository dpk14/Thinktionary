package Model.Data.API.Run;

import Model.API.Journal.Entry;
import Model.Data.SQL.ColumnInfo;
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
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, topicName);
        try {
            List<Map<String, Object>> entries = DBUtils.userQuery(map, SQLQueryBuilder.getEntryByTopic(), myDBUrl, myDBUsername, myDBPassword);
            return entries.size() > 0;
        }
        catch(SQLException e){
            throw new EntryByTopicException(e);
        }
    }

    public void removeTopicFromBank(String topicName) throws RemoveTopicException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, topicName);
        try {
            DBUtils.userAction(map, SQLQueryBuilder.removeTopicFromBank(), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new RemoveTopicException(e);
        }
    }

    public void removeTopicFromEntry(int entryID, String topicName) throws RemoveTopicException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, Integer.toString(entryID));
        map.put(3, topicName);
        try {
            DBUtils.userAction(map, SQLQueryBuilder.removeTopicFromEntry(), myDBUrl, myDBUsername, myDBPassword);
        } catch (SQLException e) {
            throw new RemoveTopicException(e);
        }
    }

    public void save(int entryID, Entry entry) throws TopicBankAddException, ModifyEntryException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitle());
        map.put(3, entry.getmyText());
        map.put(4, entry.getMyCreated());
        map.put(5, entry.getMyModfied());
        map.put(6, Integer.toString(entryID));
        try {
            DBUtils.userAction(map, SQLQueryBuilder.modifyEntryInfo(), myDBUrl, myDBUsername, myDBPassword);
        }
        catch(SQLException e){
            throw new ModifyEntryException(e.toString());
        }
    }

    public int addToEntryInfo(Entry entry) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, Integer.toString(myUserID));
        map.put(2, entry.getmyTitle());
        map.put(3, entry.getmyText());
        map.put(4, entry.getMyCreated());
        map.put(5, entry.getMyModfied());
        try {
            Connection con = DBUtils.makeConnection(myDBUrl, myDBUsername, myDBPassword);
            PreparedStatement pst = DBUtils.buildPreparedStatement(map, con, SQLQueryBuilder.addEntry());
            pst.execute();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(SQLQueryBuilder.getLastInsertID());
            List<Map<String, Object>> ret = DBUtils.map(rs);
            DBUtils.close(pst);
            DBUtils.close(rs);
            DBUtils.close(con);
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
