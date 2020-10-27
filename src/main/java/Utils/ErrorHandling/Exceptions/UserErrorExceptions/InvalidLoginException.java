package Utils.ErrorHandling.Exceptions.UserErrorExceptions;

public class InvalidLoginException extends UserErrorException {
    private static final String MESSAGE = "Username or password incorrect";

    public String toString() {
        return MESSAGE;
    }
}
