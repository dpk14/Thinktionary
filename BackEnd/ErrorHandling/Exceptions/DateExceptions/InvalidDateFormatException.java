package BackEnd.ErrorHandling.Exceptions.DateExceptions;

public class InvalidDateFormatException extends Exception{
    private final String TEXT = "Date is not of the form yyyy/MM/dd HH:mm:ss in Database";

    @Override
    public String toString() {
        return TEXT;
    }
}
