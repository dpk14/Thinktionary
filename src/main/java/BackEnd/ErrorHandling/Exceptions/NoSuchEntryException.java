package src.main.java.BackEnd.ErrorHandling.Exceptions;

public class NoSuchEntryException extends Exception {
    private static final String MESSAGE = "No entry exists with the following ID: ";
    private int myID;

    public NoSuchEntryException(int id){
        myID = id;
    }

    public String toString() {
        return MESSAGE + Integer.toString(myID);
    }
}
