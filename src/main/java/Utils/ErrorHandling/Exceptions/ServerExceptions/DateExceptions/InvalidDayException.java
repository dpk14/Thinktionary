package Utils.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

public class InvalidDayException extends InvalidDateException {
    public InvalidDayException(){
        super("day");
    }
}
