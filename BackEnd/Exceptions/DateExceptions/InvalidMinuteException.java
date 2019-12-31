package BackEnd.Exceptions.DateExceptions;

public class InvalidMinuteException extends InvalidDateException {
    public InvalidMinuteException(){
        super("minute");
    }
}
