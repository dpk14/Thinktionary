package BackEnd.ErrorHandling.Errors;

public class CorruptDBError extends Error{
    private static final String CORRUPT_DATA_ERROR = "Database contents corrupted: ";

    public CorruptDBError(Exception e){
        System.out.println(CORRUPT_DATA_ERROR);
        System.out.println(e.toString());
        System.out.println(e.getStackTrace());
    }

}
