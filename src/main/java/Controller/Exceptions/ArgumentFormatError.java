package Controller.Exceptions;

public class ArgumentFormatError extends Error {
    final String MESSAGE = "Arguments are incorrectly formatted";

    @Override
    public String toString() {
        return "ArgumentFormatError{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
