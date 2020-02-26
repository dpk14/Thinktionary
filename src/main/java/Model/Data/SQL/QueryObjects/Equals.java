package Model.Data.SQL.QueryObjects;

public class Equals extends Condition{
    public Equals(String paramName, String val){
        super(paramName, val, "=");
    }
}
