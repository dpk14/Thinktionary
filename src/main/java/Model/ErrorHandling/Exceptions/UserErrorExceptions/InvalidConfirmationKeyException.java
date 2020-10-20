package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class InvalidConfirmationKeyException extends UserErrorException {
    private static final String MESSAGE = "The confirmation key is not valid";

    @Override
    public String toString() {
        return MESSAGE;
    }
}
