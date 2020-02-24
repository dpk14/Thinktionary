package Controller.Exceptions;

public class NotLoggedInException extends Exception {
    final String MESSAGE = "User not logged in. Cannot retrieve journal information";

    @Override
    public String toString() {
        return "NotLoggedInException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
