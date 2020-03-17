package Model.Data.SQL.QueryObjects.CommandTypes;

public abstract class Command {
    String myCommand;

    public Command(String command){
        myCommand = command;
    }

    public String getMyCommand() {
        return myCommand;
    }
}
