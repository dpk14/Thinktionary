package src.main.java.BackEnd.ErrorHandling.Exceptions;

public class InvalidLoginException extends Exception {
    private static final String MESSAGE = "Username or password incorrect";

    public String toString() {
        return MESSAGE;
    }
}
