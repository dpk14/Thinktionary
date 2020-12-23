package Model.Data.SQL.Queries;

import Model.Data.SQL.QueryObjects.Condition;
import Model.Data.SQL.QueryObjects.Parameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Modify extends Query {

    private List<Parameter> parameters;
    private List<Condition> conditions;

    public Modify(String tableName, List<Parameter> parameters, List<Condition> conditions) {
        super(tableName);
        this.parameters = parameters;
        this.conditions = conditions;
    }

    @Override
    public PreparedStatement buildStatement(Connection con) throws SQLException {
        String query = getQueryString();
        PreparedStatement commandSt = con.prepareStatement(query);
        int index = 1;
        for (Parameter param : parameters){
            commandSt.setString(index, param.getMyParamName());
            index++;
            commandSt.setString(index, param.getMyValue());
            index++;
        }

        fillConditional(index, commandSt, conditions);

        return commandSt;
    }

    @Override
    public String getQueryString() {
        String command = MODIFY + this.tableName + " SET ";
        for(Parameter param : parameters){
            command += "? = ?";
            if(!param.equals(parameters.get(parameters.size()-1))) {
                command+=",";
            }
            command+=" ";
        }

        String query = command + buildConditional(conditions) + ";";
        return query;
    }
}

