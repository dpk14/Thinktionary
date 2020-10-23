package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class InvalidConfirmationKeyException extends UserErrorException {
    private static final String MESSAGE = "The confirmation key is invalid or expired";

    @Override
    public String toString() {
        return MESSAGE;
    }
}
