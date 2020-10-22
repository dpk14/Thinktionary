package Model.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

public class InvalidMinuteException extends InvalidDateException {
    public InvalidMinuteException(){
        super("minute");
    }
}
