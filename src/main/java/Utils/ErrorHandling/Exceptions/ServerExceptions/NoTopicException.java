package Utils.ErrorHandling.Exceptions.ServerExceptions;

public class NoTopicException extends ServerException {
    private final String EXCEPTION = "Topic does not exist";
    public String toString(){
        return EXCEPTION;
    }
}
