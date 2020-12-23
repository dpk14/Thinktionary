package Model.Data.SQL.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveTable extends Query {

    public RemoveTable(String tableName) {
        super(tableName);
    }

    @Override
    public PreparedStatement buildStatement(Connection con) throws SQLException {
        return con.prepareStatement(String.format(REMOVE_TABLE, this.tableName));
    }

    @Override
    public String getQueryString() {
        return String.format(REMOVE_TABLE, this.tableName);
    }
}
