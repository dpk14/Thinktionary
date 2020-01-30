package Controller.Exceptions;

public class ModeParseError extends Error{
    final String MESSAGE = "Testmode parameter format must be 'true' or 'false'";

    @Override
    public String toString() {
        return "ModeParseError{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
