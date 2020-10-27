package Utils.ErrorHandling.Exceptions.ServerExceptions.DBExceptions;

public class ModifyEntryException extends DBException{
        private static final String MORE_INFO = "Entry cannot be added";

        public ModifyEntryException(Exception e){
            super(e, e.toString(), MORE_INFO);
        }
}
