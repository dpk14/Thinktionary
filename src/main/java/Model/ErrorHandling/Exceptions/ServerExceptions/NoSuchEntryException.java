package Model.ErrorHandling.Exceptions.ServerExceptions;

public class NoSuchEntryException extends ServerException {
    private static final String MESSAGE = "No entry exists with the following ID: ";
    private int myID;

    public NoSuchEntryException(int id){
        myID = id;
    }

    public String toString() {
        return MESSAGE + Integer.toString(myID);
    }
}
