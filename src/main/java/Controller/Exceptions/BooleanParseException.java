package Controller.Exceptions;

public class BooleanParseException extends Exception{
    final String MESSAGE = "Testmode parameter format must be 'true' or 'false'";

    @Override
    public String toString() {
        return "BooleanParseException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
