package Model.ErrorHandling.Exceptions.DateExceptions;

public class InvalidDBDateException extends Exception{
    String myDate;
    String myRightFormat;
    public InvalidDBDateException(String date, String rightFormat){
        myDate = date;
        myRightFormat = rightFormat;
    }

    @Override
    public String toString() {
        return "Date " + myDate + " is of incorrect format in database. Must be of format " + myRightFormat;
    }
}
