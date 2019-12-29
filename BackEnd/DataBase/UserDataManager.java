package BackEnd.DataBase;

import BackEnd.Main.Components.Topic;
import BackEnd.Main.Entry;
import BackEnd.Main.Journal;

import java.sql.*;
import java.util.*;

public class UserDataManager {

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

    public UserDataManager(int userID){
        myUserID = userID;
    }

    public List<Map<String, Object>> loadEntryMap() throws SQLException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, ENTRY_INFO);
        map.put(2, GET_ENTRY_MAP);
        return userQuery(map);
    }

    public List<Map<String, Object>> loadEntryTopics() throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, ENTRY_TOPIC);
        map.put(2, GET_ENTRY_TOPICS);
        return userQuery(map);
    }

    public List<Map<String, Object>> loadTopics() throws SQLException{
        Map<Integer, String> map = new HashMap<>();
        map.put(1, USER_TOPIC);
        map.put(2, GET_TOPICS);
        return userQuery(map);
    }

    public static void addTopics(Set<Topic> topics) {

    }

    public static int createEntry(Entry entry) {
    }

    public static void addEntry(Entry entry) {
    }

    public static void removeEntry(int entryID) {
    }

    public static void save(Entry e, Set<Topic> myTopics) {
    }

    private List<Map<String, Object>> userQuery(Map<Integer, String> fillers) throws SQLException{
        Connection con = DBUtils.makeConnection(URL, USER, PASSWORD);
        PreparedStatement pst = con.prepareStatement(query);
        for(int i : fillers.keySet()){
            pst.setString(i, fillers.get(i));
        }
        ResultSet rs = pst.executeQuery();
        List<Map<String, Object>> ret = DBUtils.map(rs);
        DBUtils.close(pst);
        DBUtils.close(rs);
        DBUtils.close(con);
        return ret;
    }

}
