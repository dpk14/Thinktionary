package Model.ErrorHandling.Exceptions.DBExceptions;

import java.sql.SQLException;

public class EmptyDatabaseError extends Throwable {
    final String MESSAGE = "Database does not exist";

    @Override
    public String toString() {
        return "EmptyDatabaseError{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
