package Model.Data.SQL.QueryObjects;

public abstract class Condition {
    String myParamName;
    String myValue;
    String myComparator;

    Condition(String parameter, Object value, String comparator){
        myParamName = parameter;
        if (value instanceof String){
            myValue = "'" + value + "'";
        }
        else myValue = value.toString();
        myComparator = comparator;
    }

    @Override
    public String toString(){
        return " " + myParamName + " " + myComparator + " " + myValue + " ";
    }
}
