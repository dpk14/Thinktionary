package Model.Data.API;

import Model.ConfigUtils.PropertyUtils.PropertyManager;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.SQLQueryBuilder;
import Model.Data.SQL.TableNames;
import Model.Data.Utils.AWSCredentials;
import Model.Data.Utils.DBUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBAPI {

    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;

    public DBAPI() {
        myDBUsername = PropertyManager.getDBUsername();
        myDBPassword = PropertyManager.getDBPwd();
        myDBUrl = PropertyManager.getDBUrl();
    }

    public AWSCredentials getAccessKeys() throws SQLException {
        List<Map<String, Object>> ret = DBUtils.userQuery(SQLQueryBuilder.select(TableNames.AWS_ACCESS_KEYS, new ArrayList<>()), myDBUrl, myDBUsername, myDBPassword);
        Map<String, Object> keyMap = ret.get(0);
        System.out.println(keyMap);
        return new AWSCredentials(keyMap.get(ColumnInfo.AWS_ACCESS_KEY).toString(), keyMap.get(ColumnInfo.AWS_SECRET_KEY).toString());
    }

}
