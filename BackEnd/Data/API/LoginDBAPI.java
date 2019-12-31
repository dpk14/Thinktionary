package BackEnd.Data.API;

import BackEnd.Data.Lib.Paths.DBNames;
import BackEnd.Data.Lib.Paths.DBUrls;
import BackEnd.Data.Lib.SQLStrings.SQLQuery;
import BackEnd.Data.Lib.SQLStrings.TableNames;
import BackEnd.Data.Utils.DBUtils;
import BackEnd.Data.Utils.ParserUtils;
import BackEnd.ErrorHandling.Errors.CorruptDBError;
import BackEnd.ErrorHandling.Exceptions.AccountExistsException;
import BackEnd.ErrorHandling.Exceptions.DateExceptions.InvalidDateException;
import BackEnd.ErrorHandling.Exceptions.InvalidLoginException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginDBAPI {

    private String myDBUser;
    private String myDBPassword;

    public LoginDBAPI(String dbUser, String dbPassword){
        myDBUser = dbUser;
        myDBPassword = dbPassword;
    }

    public int login(String userName, String passWord) throws InvalidLoginException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        try {
            List<Map<String, Object>> userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), DBUrls.getURL(DBNames.getSQLITE()), myDBUser, myDBPassword);
            if(userInfo.size() != 1){
                throw new InvalidLoginException();
            }
            return ParserUtils.getUserID(userInfo);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

    public void createAccount(String userName, String passWord) throws AccountExistsException {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, TableNames.getUserInfo());
        map.put(2, userName);
        map.put(3, passWord);
        List<Map<String, Object>> userInfo = new ArrayList<>();
        try {
            userInfo = DBUtils.userQuery(map, SQLQuery.getUser(), DBUrls.getURL(DBNames.getSQLITE()), myDBUser, myDBPassword);
            if(userInfo.size() != 0) {
                throw new AccountExistsException();
            }
            DBUtils.userQuery(map, SQLQuery.addUser(), DBUrls.getURL(DBNames.getSQLITE()), myDBUser, myDBPassword);
        }
        catch(SQLException e){
            throw new CorruptDBError(e);
        }
    }

}
