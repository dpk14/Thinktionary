package Data.Communication;

import BackEnd.Main.Components.Topic;
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
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TabelNames.getEntryInfo());
        map.put(2, Integer.toString(myUserID));
        return userQuery(map, SQLQuery.getEntryMap());
    }

    public List<Map<String, Object>> loadEntryTopics() throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TabelNames.getEntryToTopic());
        map.put(2, Integer.toString(myUserID));
        return userQuery(map, SQLQuery.getEntryToTopic());
    }

    public List<Map<String, Object>> loadTopics() throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TabelNames.getUserTopic());
        map.put(2, Integer.toString(myUserID));
        return userQuery(map, SQLQuery.getTopics());
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
        userAction(map, SQLQuery.modifyEntry());
    }

    /*
    ----------------------------
    Private Helpers:
    ----------------------------
     */

    private void userAction(Map<Integer, String> fillers, String command) throws SQLException{
        Connection con = DBUtils.makeConnection(URL, USER, PASSWORD);
        PreparedStatement pst = DBUtils.buildPreparedStatement(fillers, con, command);
        pst.executeQuery(command);
        DBUtils.close(pst);
        DBUtils.close(con);
    }

    private List<Map<String, Object>> userQuery(Map<Integer, String> fillers, String query) throws SQLException{
        Connection con = DBUtils.makeConnection(URL, USER, PASSWORD);
        PreparedStatement pst = DBUtils.buildPreparedStatement(fillers, con, query);
        ResultSet rs = pst.executeQuery();
        DBUtils.close(pst);
        List<Map<String, Object>> ret = DBUtils.map(rs);
        DBUtils.close(rs);
        DBUtils.close(con);
        return ret;
    }

    private void remove(int entryID, String tableName) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, tableName);
        map.put(2, Integer.toString(myUserID));
        map.put(3, Integer.toString(entryID));
        userAction(map, SQLQuery.remove());
    }

    private int addToEntryInfo(Entry entry) throws SQLException, IndexOutOfBoundsException, ClassCastException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TabelNames.getEntryInfo());
        map.put(2, Integer.toString(myUserID));
        map.put(3, entry.getmyTitleasString());
        map.put(4, entry.getMyColorasString());
        map.put(5, entry.getMyCreatedasString());
        map.put(6, entry.getMyModfiedasString());
        userAction(map, SQLQuery.addEntry());
        List<Map<String, Object>> ent = userQuery(map, SQLQuery.getEntryID());
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
            userAction(map, SQLQuery.addTopic());
        }
    }


}
