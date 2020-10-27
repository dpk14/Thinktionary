package Utils.ErrorHandling.Exceptions.ServerExceptions.TableExceptions;

public class CreateTableException extends Exception {
    private final String MESSAGE = "Cannot create table";
    public CreateTableException(Exception e){
        super(e);
    }

    @Override
    public String toString() {
        return "CreateTableException{" +
                "MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
