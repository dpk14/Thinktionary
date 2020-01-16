package Model.ErrorHandling.Exceptions.DBExceptions;

public class DBException extends Exception{
    private String ADDITIONAL_INFO;
    private String BASE_INFO;

    public DBException(String baseInfo, String additionalInfo){
        ADDITIONAL_INFO = additionalInfo;
        BASE_INFO = baseInfo;
    }

    @Override
    public String toString() {
        return ADDITIONAL_INFO + "\n" + "Additonal Info: " + ADDITIONAL_INFO;
    }
}
