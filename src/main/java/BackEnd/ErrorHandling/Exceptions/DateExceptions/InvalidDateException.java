package src.main.java.BackEnd.ErrorHandling.Exceptions.DateExceptions;

public abstract class InvalidDateException extends Exception {
    private final String EXCEPTION = "Choose a valid ";
    private String myType;

    public InvalidDateException(String string){
        myType = string;
    }
    public String toString(){
        return EXCEPTION + myType;
    }
}
