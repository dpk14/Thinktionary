package Model.ErrorHandling.Exceptions.DBExceptions;

import java.sql.SQLException;

public class EmptyDatabaseError extends Throwable {
    final String MESSAGE = "Entries could not be properly sorted by topic";

    @Override
    public String toString() {
        return "EmptyDatabaseError{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
