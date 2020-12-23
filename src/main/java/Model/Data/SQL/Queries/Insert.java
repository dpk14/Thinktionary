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

        int index = 1;
        for(Parameter param : parameters){
            commandSt.setString(index, param.getMyValue());
            index++;
        }

        return commandSt;
    }

    @Override
    public String getQueryString() {
        String header = INSERT + this.tableName;
        String params = " (";
        String values = " (";

        for(Parameter param : parameters){
            params += param.getMyParamName();
            values += (param.getMyValue() instanceof String ? "'?'" : "?");
            if(!param.equals(parameters.get(parameters.size()-1))) {
                params+=", ";
                values+=", ";
            } else {
                params+=")";
                values+=")";
            }
        }

        String statement = header + params + " VALUES " + values + ";";
        return statement;
    }
}
