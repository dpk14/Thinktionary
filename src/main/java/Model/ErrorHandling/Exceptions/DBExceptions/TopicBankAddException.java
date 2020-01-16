package Model.ErrorHandling.Exceptions.DBExceptions;

public class TopicBankAddException extends DBException{

    private static final String MORE_INFO = "Topic cannot be added to bank";

    public TopicBankAddException(String baseInfo){
        super(baseInfo, MORE_INFO);
    }
}
