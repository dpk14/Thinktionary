package Model.Data.SQL.QueryObjects;

public abstract class Condition {
    String myParamName;
    Object myValue;
    String myComparator;

    Condition(String parameter, Object value, String comparator){
        myParamName = parameter;
        myValue = value.toString();
        myComparator = comparator;
    }

    @Override
    public String toString(){
        return " " + myParamName + " " + myComparator + " ?";
    }

    public Object getMyValue() {
        return myValue;
    }
}
