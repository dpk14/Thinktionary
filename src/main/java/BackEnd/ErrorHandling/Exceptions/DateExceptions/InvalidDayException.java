package src.main.java.BackEnd.ErrorHandling.Exceptions.DateExceptions;

public class InvalidDayException extends InvalidDateException {
    public InvalidDayException(){
        super("day");
    }
}
