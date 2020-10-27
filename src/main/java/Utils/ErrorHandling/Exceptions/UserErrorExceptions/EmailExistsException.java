package Utils.ErrorHandling.Exceptions.UserErrorExceptions;

public class EmailExistsException extends UserErrorException {

    private static final String MESSAGE = "Email already taken.";

    public String toString() {
        return MESSAGE;
    }
}
