package Model.ErrorHandling.Exceptions.DBExceptions;

public class DBException extends Exception{
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
