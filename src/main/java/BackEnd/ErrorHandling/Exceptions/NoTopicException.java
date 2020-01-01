package src.main.java.BackEnd.ErrorHandling.Exceptions;

public class NoTopicException extends Exception{
    private final String EXCEPTION = "Topic does not exist";
    public String toString(){
        return EXCEPTION;
    }
}
