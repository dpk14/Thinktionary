package Model.ErrorHandling.Exceptions.DateExceptions;

public class InvalidDayException extends InvalidDateException {
    public InvalidDayException(){
        super("day");
    }
}
