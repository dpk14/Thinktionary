package Model.Data.SQL.QueryObjects;

public class Equals extends Condition{
    public Equals(String paramName, Object val){
        super(paramName, val, "=");
    }
}
