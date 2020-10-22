package Model.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

public class InvalidHourException extends InvalidDateException{
    public InvalidHourException(){
        super("hour");
    }
}
