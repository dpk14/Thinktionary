package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class InvalidEmailException extends UserErrorException {
    private static final String MESSAGE = "The email entered is invalid";

    @Override
    public String toString() {
        return MESSAGE;
    }
}
