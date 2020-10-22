package Model.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

public class InvalidMonthException extends InvalidDateException{
    public InvalidMonthException(){
        super("month");
    }
}
