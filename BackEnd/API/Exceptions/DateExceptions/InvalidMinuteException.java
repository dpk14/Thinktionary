package BackEnd.API.Exceptions.DateExceptions;

public class InvalidMinuteException extends InvalidDateException {
    public InvalidMinuteException(){
        super("minute");
    }
}
