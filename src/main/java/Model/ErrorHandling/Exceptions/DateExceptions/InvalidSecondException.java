package Model.ErrorHandling.Exceptions.DateExceptions;

public class InvalidSecondException extends InvalidDateException {
    public InvalidSecondException(){
        super("second");
    }
}
