package src.main.java.BackEnd.ErrorHandling.Exceptions.DateExceptions;

public class InvalidMonthException extends InvalidDateException{
    public InvalidMonthException(){
        super("month");
    }
}