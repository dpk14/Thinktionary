package Model.ErrorHandling.Exceptions.ServerExceptions;

import java.sql.SQLException;

public class EntryByTopicException extends ServerException {
    final String MESSAGE = "Entries could not be properly sorted by topic";

    public EntryByTopicException(SQLException e) {
        setStackTrace(e.getStackTrace());
    }
    @Override
    public String toString() {
        return "EntryByTopicException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
