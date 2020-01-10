package src.main.java.Model.Data.Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
    public static Connection makeConnection(String url, String user, String password) throws SQLException {
        //Class.forName(driver);
        if (user == null || password == null || user.trim().length() == 0 || password.trim().length() == 0) {
            return DriverManager.getConnection(url);
        }
        else{
            return DriverManager.getConnection(url, user, password);
        }
    }

    public static void close(Connection connection){
        try{
            if(connection!=null) {
                connection.close();
            }
        }
        catch(SQLException e){
            System.out.print(e.getStackTrace());
        }
    }

    public static void close(Statement st){
        try{
            if(st!=null) {
                st.close();
            }
        }
        catch(SQLException e){
            System.out.print(e.getStackTrace());
        }
    }

    public static void close(ResultSet rs){
        try{
            if(rs!=null) {
                rs.close();
            }
        }
        catch(SQLException e){
            System.out.print(e.getStackTrace());
        }
    }

    public static List<Map<String, Object>> map(ResultSet rs) throws SQLException{
        List<Map<String, Object>> result = new ArrayList();
        try{
            if(rs!=null){
                ResultSetMetaData rsmd = rs.getMetaData();
                while(rs.next()){
                    Map<String, Object> row = new HashMap<>();
                    for(int col = 1; col <= rsmd.getColumnCount(); col++){
                        row.put(rsmd.getColumnName(col), rs.getObject(col));
                    }
                    result.add(row);
                }
            }
        }
        finally {
            close(rs);
        }
        return result;
    }

    public static PreparedStatement buildPreparedStatement(Map<Integer, String> fillers, Connection con, String statement)
            throws SQLException{

        PreparedStatement pst = con.prepareStatement(statement);
        for(int i : fillers.keySet()){
            pst.setString(i, fillers.get(i));
        }
        return pst;
    }

    public static void userAction(Map<Integer, String> fillers, String command, String url,
                           String user, String password) throws SQLException{

        Connection con = DBUtils.makeConnection(url, user, password);
        PreparedStatement pst = DBUtils.buildPreparedStatement(fillers, con, command);
        pst.executeQuery(command);
        DBUtils.close(pst);
        DBUtils.close(con);
    }

    public static void userAction(String command, String url,
                                  String user, String password) throws SQLException{

        Connection con = DBUtils.makeConnection(url, user, password);
        Statement st = con.createStatement();
        st.execute(command);
        DBUtils.close(st);
        DBUtils.close(con);
    }

    public static List<Map<String, Object>> userQuery(Map<Integer, String> fillers, String query, String url,
                                                String user, String password) throws SQLException{

        Connection con = DBUtils.makeConnection(url, user, password);
        PreparedStatement pst = DBUtils.buildPreparedStatement(fillers, con, query);
        ResultSet rs = pst.executeQuery();
        DBUtils.close(pst);
        List<Map<String, Object>> ret = DBUtils.map(rs);
        DBUtils.close(rs);
        DBUtils.close(con);
        return ret;
    }

    public static List<Map<String, Object>> userQuery(String query, String url,
                                                      String user, String password) throws SQLException{

        Connection con = DBUtils.makeConnection(url, user, password);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        DBUtils.close(st);
        List<Map<String, Object>> ret = DBUtils.map(rs);
        DBUtils.close(rs);
        DBUtils.close(con);
        return ret;
    }
}
