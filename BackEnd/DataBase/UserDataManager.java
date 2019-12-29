package BackEnd.DataBase;

import BackEnd.Main.Components.Topic;
import BackEnd.Main.Entry;
import BackEnd.Main.Journal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDataManager {

    /*
    UserID | UserName | Password      name: login

    UserID | Topic           userTopic

    EntryID | UserID | Title | DateCreated | DateModified      EntryInfo

    EntryID | Topic          entryTopic

     */

    private final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false";
    private final String USER = "testuser";
    private final String PASSWORD = "test623";
    int myUserID;

    public UserDataManager(int userID){
        myUserID = userID;
    }

    public List<Map<String, Object>> loadEntryMap() throws SQLException {
        return userQuery(ENTRY_INFO, GET_ENTRY_MAP);
    }

    public List<Map<String, Object>> loadEntryTopics(){
        return userQuery(ENTRY_TOPIC, GET_ENTRY_TOPICS);
    }

    public Set<Topic> loadTopics() {
        return userQuery(USER_TOPIC, GET_TOPICS);
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

    private Object userQuery(String table, String query) throws SQLException{
        Connection con = DBUtils.makeConnection(URL, USER, PASSWORD);
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, table);
        pst.setString(2, myUserName);
        ResultSet rs = pst.executeQuery();
        List<Map<String, Object>> ret = DBUtils.map(rs);
        DBUtils.close(pst);
        DBUtils.close(rs);
        DBUtils.close(con);
        return ret;
    }

}
