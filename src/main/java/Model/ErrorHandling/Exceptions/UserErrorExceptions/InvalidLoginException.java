package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class InvalidLoginException extends Exception {
    private static final String MESSAGE = "Username or password incorrect";

    public String toString() {
        return MESSAGE;
    }
}
