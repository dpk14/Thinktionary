package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class AccountNotRegisteredException extends UserErrorException {
    private static final String MESSAGE = "No account with this email and username has been registered.";

    @Override
    public String toString() {
        return MESSAGE;
    }
}
