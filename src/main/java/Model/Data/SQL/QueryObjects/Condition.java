package Model.Data.SQL.QueryObjects;

public abstract class Condition {
    String myParamName;
    String myValue;
    String myComparator;

    Condition(String parameter, String value, String comparator){
        myParamName = parameter;
        myValue = value;
        myComparator = comparator;
    }

    @Override
    public String toString(){
        return " " + myParamName + " " + myComparator + " " + myValue + " ";
    }
}
