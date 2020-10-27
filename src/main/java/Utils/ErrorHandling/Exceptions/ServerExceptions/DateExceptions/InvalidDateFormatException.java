package Utils.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

import Utils.ErrorHandling.Exceptions.ServerExceptions.ServerException;

public class InvalidDateFormatException extends ServerException {
    private final String TEXT = "Date is not of the form yyyy/MM/dd HH:mm:ss in Database";

    @Override
    public String toString() {
        return TEXT;
    }
}
