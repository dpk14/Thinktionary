package Model.Data.SQL.QueryObjects.CommandTypes;

public class Query extends Command{
    String myCommand;

    public Query(String command){
        super(command);
    }

    public String getMyCommand() {
        return myCommand;
    }
}
