package Model.ErrorHandling.Exceptions;

import java.sql.SQLException;

public class RemoveTopicException extends Exception {
        final String MESSAGE = "Topic could not be deleted by topic name";

        public RemoveTopicException(SQLException e) {
            setStackTrace(e.getStackTrace());
        }
        @Override
        public String toString() {
            return "RemoveTopicException{" +
                    "MESSAGE='" + MESSAGE + '\'' +
                    '}';
        }
}
