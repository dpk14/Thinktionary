package Model.ErrorHandling.Exceptions.ServerExceptions;

public abstract class ServerException extends Exception {

    public ServerException (Exception e) {
        super(e);
    }

    public ServerException () {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
