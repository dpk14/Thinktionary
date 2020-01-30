package Model.Data.Exceptions;

import java.io.IOException;

public class LoadPropertiesException extends Throwable {
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
