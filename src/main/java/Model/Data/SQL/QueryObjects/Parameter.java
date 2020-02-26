package Model.Data.SQL.QueryObjects;

public class Parameter {
    String myParamName;
    String myValue;

    public Parameter(String parameter, String value){
        myParamName = parameter;
        myValue = value;
    }

    public String getMyParamName() {
        return myParamName;
    }

    public String getMyValue() {
        return myValue;
    }
}
