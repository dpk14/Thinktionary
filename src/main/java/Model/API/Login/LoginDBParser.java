package Model.API.Login;

import Model.Data.SQL.ColumnInfo;

import java.util.List;
import java.util.Map;

public class LoginDBParser {

    public static int getUserID (List<Map<String, Object>> userInfo) throws ClassCastException{
        return (int) userInfo.get(0).get(ColumnInfo.getUSERID());
    }

    public static String getEmail(List<Map<String, Object>> userInfo) {
        return userInfo.get(0).get(ColumnInfo.getEMAIL()).toString();
    }
}
