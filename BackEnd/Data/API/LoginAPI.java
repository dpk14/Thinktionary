package BackEnd.Data.API;

import BackEnd.Data.Lib.Paths.DBNames;
import BackEnd.Data.Lib.Paths.DBUrls;
import BackEnd.Data.Lib.SQLStrings.SQLQuery;
import BackEnd.Data.Lib.SQLStrings.TableNames;
import BackEnd.Data.Utils.DBUtils;
import BackEnd.Data.Utils.ParserUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginAPI {
    public int login(String userName, String passWord){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        List<Map<String, Object>> userInfo = DBUtils.userQuery(map, SQLQuery.loadUser(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
        return ParserUtils.getUserID(userInfo);
    }

    private void createAccount(String userName, String passWord){
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        DBUtils.userQuery(map, SQLQuery.addUser(), DBUrls.getURL(DBNames.getSQLITE()), USER, PASSWORD);
    }


}
