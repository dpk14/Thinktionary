package Model.Data.SQL.Queries;

import Model.Data.SQL.QueryObjects.Parameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Insert extends Query {

    private List<Parameter> parameters;

    public Insert(String tableName, List<Parameter> parameters) {
        super(tableName);
        this.parameters = parameters;
    }

    @Override
    public PreparedStatement buildStatement(Connection con) throws SQLException {
        String statement = getQueryString();
        PreparedStatement commandSt = con.prepareStatement(statement);
        commandSt.setString(1, tableName);

        int index = 2;
        for(Parameter param : parameters){
            commandSt.setString(index, param.getMyParamName());
            index++;
        }
        for(Parameter param : parameters){
            commandSt.setString(index, param.getMyValue());
            index++;
        }

        return commandSt;
    }

    @Override
    public String getQueryString() {
        String header = INSERT + "?";
        String string = " (";

        for(Parameter param : parameters){
            string += "?";
            if(!param.equals(parameters.get(parameters.size()-1))) {
                string+=", ";
            } else {
                string+=")";
            }
        }

        String statement = header + string + " VALUES " + string + ";";
        return statement;
    }
}
