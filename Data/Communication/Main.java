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

    public int createEntry(Entry entry) throws SQLException, IndexOutOfBoundsException, ClassCastException{
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

    public void addTopics(Map<String, String> topicToColor) throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        for(String topic : topicToColor.keySet()){
            String color = topicToColor.get(topic);
            map.put(1, TabelNames.getEntryToTopic();
            map.put(2, Integer.toString(myUserID));
            map.put(3, topic);
            map.put(4, color);
            userAction(map, SQLQuery.addTopic());
        }
    }

    public static void removeEntry(int entryID) {
    }

    public static void save(Entry e, Set<Topic> myTopics) {
    }

    private void userAction(Map<Integer, String> fillers, String command) throws SQLException{
        Connection con = DBUtils.makeConnection(URL, USER, PASSWORD);
        PreparedStatement pst = buildPreparedStatement(fillers, con, command);
        pst.executeQuery(command);
        DBUtils.close(pst);
        DBUtils.close(con);
    }

    private List<Map<String, Object>> userQuery(Map<Integer, String> fillers, String query) throws SQLException{
        Connection con = DBUtils.makeConnection(URL, USER, PASSWORD);
        PreparedStatement pst = buildPreparedStatement(fillers, con, query);
        ResultSet rs = pst.executeQuery();
        DBUtils.close(pst);
        List<Map<String, Object>> ret = DBUtils.map(rs);
        DBUtils.close(rs);
        DBUtils.close(con);
        return ret;
    }

    private PreparedStatement buildPreparedStatement(Map<Integer, String> fillers, Connection con, String statement)
        throws SQLException{

        PreparedStatement pst = con.prepareStatement(statement);
        for(int i : fillers.keySet()){
            pst.setString(i, fillers.get(i));
        }
        return pst;
    }

}
