package Model.Data.SQL.Queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class CreateTable extends Query {

    Map<String, String> columnToType;

    public CreateTable(String tableName, Map<String, String> columnToType) {
        super(tableName);
        this.columnToType = columnToType;
    }

    @Override
    public PreparedStatement buildStatement(Connection con) throws SQLException {
        String command = getQueryString();
        return con.prepareStatement(command);
    }

    @Override
    public String getQueryString() {
        String command = "CREATE TABLE IF NOT EXISTS " + tableName + "(";
        int count = 0;
        for (String columnName : columnToType.keySet()) {
            String type = columnToType.get(columnName);
            command += columnName + " " + type + " ";
            count++;
            if (count!=columnToType.keySet().size()) {
                command += ", ";
            }
        }
        command+=")";
        return command;
    }
}
