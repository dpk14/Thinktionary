package Utils.ErrorHandling.Exceptions.ServerExceptions.DBExceptions;

import Utils.ErrorHandling.Exceptions.ServerExceptions.ServerException;

public class EmptyDatabaseError extends ServerException {
    final String MESSAGE = "Database does not exist";

    @Override
    public String toString() {
        return "EmptyDatabaseError{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
