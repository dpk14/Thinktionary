package BackEnd.API.Exceptions.DateExceptions;

public class InvalidDayException extends InvalidDateException {
    public InvalidDayException(){
        super("day");
    }
}
