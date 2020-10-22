package Model.ErrorHandling.Exceptions.ServerExceptions.DBExceptions;

import Model.ErrorHandling.Exceptions.ServerExceptions.ServerException;

public class DBException extends ServerException {
    private String ADDITIONAL_INFO;
    private String BASE_INFO;

    public DBException(Exception e, String baseInfo, String additionalInfo){
        super(e);
        BASE_INFO = baseInfo;
        ADDITIONAL_INFO = additionalInfo;

    }

    @Override
    public String toString() {
        return BASE_INFO + "\n\n\tAdditonal Info: " + ADDITIONAL_INFO;
    }
}
