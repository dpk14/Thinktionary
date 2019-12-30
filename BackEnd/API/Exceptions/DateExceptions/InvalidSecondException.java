package BackEnd.API.Exceptions.DateExceptions;

public class InvalidSecondException extends InvalidDateException {
    public InvalidSecondException(){
        super("second");
    }
}
