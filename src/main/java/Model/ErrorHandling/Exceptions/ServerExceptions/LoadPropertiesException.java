package Model.ErrorHandling.Exceptions.ServerExceptions;

import java.io.IOException;

public class LoadPropertiesException extends ServerException {
    final String MESSAGE = "Database properties could not be loaded";
    private String myStackMessage;
    public LoadPropertiesException(IOException e) {
        setStackTrace(e.getStackTrace());
    }
    @Override
    public String toString() {
        return "LoadPropertiesException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
