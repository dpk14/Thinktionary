package Utils.ErrorHandling.Exceptions.ServerExceptions.DateExceptions;

import Utils.ErrorHandling.Exceptions.ServerExceptions.ServerException;

public abstract class InvalidDateException extends ServerException {
    private final String EXCEPTION = "Choose a valid ";
    private String myType;

    public InvalidDateException(String string){
        myType = string;
    }
    public String toString(){
        return EXCEPTION + myType;
    }
}
