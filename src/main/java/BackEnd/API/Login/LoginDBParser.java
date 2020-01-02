package src.main.java.BackEnd.API.Login;

import src.main.java.BackEnd.Data.Lib.SQLStrings.ColumnLabels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBParser {
    protected static Map<Integer, User> parseUserInfoMap(List<Map<String, Object>> table) throws ClassCastException, RuntimeException {
        Map<Integer, User> ret = new HashMap();
        for(Map<String, Object> row : table){
            int id = Integer.parseInt((String) row.get(ColumnLabels.getUSERID()));
            String userName = (String) row.get(ColumnLabels.getUSERNAME());
            String password = (String) row.get(ColumnLabels.getPASSWORD());
            User user = new User(id, userName, password);
            ret.put(id, user);
        }
        return ret;
    }

    protected static int getUserID(List<Map<String, Object>> userInfo) throws ClassCastException{
        return (int) userInfo.get(0).get(ColumnLabels.getUSERID());
    }
}
