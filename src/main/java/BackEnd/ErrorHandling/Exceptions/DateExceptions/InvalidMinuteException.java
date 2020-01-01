package src.main.java.BackEnd.ErrorHandling.Exceptions.DateExceptions;

public class InvalidMinuteException extends InvalidDateException {
    public InvalidMinuteException(){
        super("minute");
    }
}
