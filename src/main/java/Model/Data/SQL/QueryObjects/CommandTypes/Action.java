package Model.Data.SQL.QueryObjects.CommandTypes;

public class Action extends Command{
        String myCommand;

        public Action(String command){
            super(command);
        }

        public String getMyCommand() {
            return myCommand;
        }
    }

