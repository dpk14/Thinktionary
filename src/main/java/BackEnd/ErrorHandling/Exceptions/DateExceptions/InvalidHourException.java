package src.main.java.BackEnd.ErrorHandling.Exceptions.DateExceptions;

public class InvalidHourException extends InvalidDateException{
    public InvalidHourException(){
        super("hour");
    }
}
