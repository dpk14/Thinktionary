package Model.ErrorHandling.Exceptions.ServerExceptions.DBExceptions;

public class TopicBankAddException extends DBException{

    private static final String MORE_INFO = "Topic cannot be added to bank";

    public TopicBankAddException(Exception e){
        super(e, e.toString(), MORE_INFO);
    }

}
