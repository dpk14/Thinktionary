package Model.Data.API;

import Model.Data.AWSCredentials;
import Model.Data.SQL.ColumnInfo;
import Model.Data.SQL.Queries.Query;
import Model.Data.SQL.Queries.Select;
import Model.Data.SQL.TableNames;
import Utils.ErrorHandling.Exceptions.UserErrorExceptions.QueryFailedException;
import Utils.PropertyUtils.PropertyManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBAPI {

    private static final int MAX_TRIES = 3;
    protected String myDBUrl;
    protected String myDBUsername;
    protected String myDBPassword;

    public DBAPI() {
        myDBUsername = PropertyManager.getDBUsername();
        myDBPassword = PropertyManager.getDBPwd();
        myDBUrl = PropertyManager.getDBUrl();
    }

    public AWSCredentials getAccessKeys() {
        List<Map<String, Object>> ret = userQuery(new Select(TableNames.AWS_ACCESS_KEYS, new ArrayList<>()));
        Map<String, Object> keyMap = ret.get(0);
        System.out.println(keyMap);
        return new AWSCredentials(keyMap.get(ColumnInfo.AWS_ACCESS_KEY).toString(), keyMap.get(ColumnInfo.AWS_SECRET_KEY).toString());
    }

    private Connection makeConnection() throws SQLException {
        if (myDBUsername == null || myDBPassword == null || myDBUsername.trim().length() == 0 || myDBPassword.trim().length() == 0) {
            return DriverManager.getConnection(myDBUrl);
        } else {
            return DriverManager.getConnection(myDBUrl, myDBUsername, myDBPassword);
        }
    }

    private static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
    }

    private static void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
    }

    private static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.print(e.getStackTrace());
        }
    }

    private static List<Map<String, Object>> map(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList();
        try {
            if (rs != null) {
                ResultSetMetaData rsmd = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int col = 1; col <= rsmd.getColumnCount(); col++) {
                        row.put(rsmd.getColumnName(col), rs.getObject(col));
                    }
                    result.add(row);
                }
            }
        } finally {
            close(rs);
        }
        return result;
    }

    protected void userAction(Query query) {
        for (int tries = 0; tries < MAX_TRIES; tries++) {
            try {
                try {
                    Connection con = makeConnection();
                    PreparedStatement preparedStatement = query.buildStatement(con);
                    try {
                        preparedStatement.execute();
                        close(preparedStatement);
                        close(con);
                        return;
                    } catch (SQLException e) {
                        throw new RuntimeException(String.format("The query %s failed", query.getQueryString()), e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Could not connect to db", e);
                }
            } catch (RuntimeException e) {
                throw e;
            }
        } throw new RuntimeException();
    }

    protected void userActionThrowsException(String command) throws QueryFailedException {
        try {
            Connection con = makeConnection();
            Statement st = con.createStatement();
            try {
                st.execute(command);
                close(st);
                close(con);
            } catch (SQLException e) {
                System.out.println("The query %s failed due to a user error");
                throw new QueryFailedException();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to db", e);
        }
    }

    public List<Map<String, Object>> userQuery(Query query) {
        for (int tries = 0; tries < MAX_TRIES; tries++) {
            try {
                try {
                    Connection con = makeConnection();
                    PreparedStatement st = query.buildStatement(con);
                    try {
                        ResultSet rs = st.executeQuery();
                        List<Map<String, Object>> ret = map(rs);
                        close(st);
                        close(rs);
                        close(con);
                        return ret;
                    } catch (SQLException e) {
                        throw new RuntimeException(String.format("The query %s failed", query), e);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Could not connect to db", e);
                }
            } catch (RuntimeException e) {
                throw e;
            }
        } throw new RuntimeException();
    }

    public List<Map<String, Object>> userQueryThrowsException(String query) throws QueryFailedException {
        for (int tries = 0; tries < MAX_TRIES; tries++) {
            try {
                try {
                    Connection con = makeConnection();
                    Statement st = con.createStatement();
                    try {
                        ResultSet rs = st.executeQuery(query);
                        List<Map<String, Object>> ret = map(rs);
                        close(st);
                        close(rs);
                        close(con);
                        return ret;
                    } catch (SQLException e) {
                        System.out.println("The query %s failed");
                        throw new QueryFailedException();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Could not connect to db", e);
                }
            } catch (Exception e) {
                throw e;
            }
        } throw new RuntimeException();
    }

}
