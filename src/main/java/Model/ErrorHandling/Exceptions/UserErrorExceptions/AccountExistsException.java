package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class AccountExistsException extends UserErrorException{

        private static final String MESSAGE = "Username or email already taken.";

        public String toString() {
            return MESSAGE;
        }
    }

