package Utils.ErrorHandling.Exceptions.ServerExceptions.TableExceptions;

public class RemoveTableException extends Throwable {
    private final String MESSAGE = "Cannot create table";
    public RemoveTableException(Exception e){
        super(e);
    }

    @Override
    public String toString() {
        return "RemoveTableException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
