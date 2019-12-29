package Data.Communication.Utils;

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
}
