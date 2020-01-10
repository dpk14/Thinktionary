package Model.ErrorHandling.Exceptions;

public class AccountExistsException extends Exception{

        private static final String MESSAGE = "Username and password combo already taken.";

        public String toString() {
            return MESSAGE;
        }
    }

