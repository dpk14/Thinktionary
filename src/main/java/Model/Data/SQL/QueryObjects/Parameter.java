package Model.Data.SQL.QueryObjects;

public abstract class Parameter {
    String myParamName;
    String myValue;

    Parameter(String parameter, String value){
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
