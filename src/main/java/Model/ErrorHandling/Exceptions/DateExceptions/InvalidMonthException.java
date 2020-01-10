package Model.ErrorHandling.Exceptions.DateExceptions;

public class InvalidMonthException extends InvalidDateException{
    public InvalidMonthException(){
        super("month");
    }
}
