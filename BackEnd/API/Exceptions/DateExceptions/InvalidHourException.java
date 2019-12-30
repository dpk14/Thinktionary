package BackEnd.API.Exceptions.DateExceptions;

public class InvalidHourException extends InvalidDateException{
    public InvalidHourException(){
        super("hour");
    }
}
