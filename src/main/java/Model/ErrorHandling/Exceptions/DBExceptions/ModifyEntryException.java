package Model.ErrorHandling.Exceptions.DBExceptions;

public class ModifyEntryException extends DBException{
        private static final String MORE_INFO = "Entry cannot be added";

        public ModifyEntryException(String baseInfo){
            super(baseInfo, MORE_INFO);
        }
}
