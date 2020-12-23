package Model.Data.SQL.QueryObjects;

public class Parameter {
    String myParamName;
    String myValue;

    public Parameter(String parameter, Object value){
        myParamName = parameter;
        myValue = value.toString();
    }

    public String getMyParamName() {
        return myParamName;
    }

    public String getMyValue() {
        return myValue;
    }
}
