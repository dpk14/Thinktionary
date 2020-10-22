package Model.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

public class InvalidSecondException extends InvalidDateException {
    public InvalidSecondException(){
        super("second");
    }
}
