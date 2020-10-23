package Model.ErrorHandling.Exceptions.UserErrorExceptions;

public class QueryFailedException extends UserErrorException {

    @Override
    public String toString() {
        return "The query has failed.";
    }
}
