package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class EmailDeliveryFailure extends UserErrorException {
    private static final String MESSAGE = "The email failed to send. Try restarting the app. Error message: %s";
    private Exception e;

    public EmailDeliveryFailure(Exception e) {
        this.e = e;
    }

    @Override
    public String toString() {
        return String.format(MESSAGE, e.getMessage());
    }
}
