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
        PreparedStatement commandSt = con.prepareStatement(command);
        commandSt.setString(1, tableName);
        int count = 2;
        for (String columnName : columnToType.keySet()) {
            String type = columnToType.get(columnName);
            commandSt.setString(count, columnName);
            count++;
            commandSt.setString(count, type);
            count++;
        }
        return commandSt;
    }

    @Override
    public String getQueryString() {
        String command = "CREATE TABLE IF NOT EXISTS ? (";
        for (int count = 0; count < columnToType.keySet().size(); count++) {
            command += "? ? ";
            if (count!=columnToType.keySet().size()-1) {
                command += ", ";
            }
        }
        command+=")";
        return command;
    }
}
