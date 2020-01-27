package Model.Data.Lib.Paths;

public class DBFileNames {
    private static final String MAIN_DB_PATH = "myDB.db";
    private static final String TEST_DB_PATH = "testDB.db";

    public static String getMainDbName() {
        return String.valueOf(MAIN_DB_PATH);
    }
    public static String getTestDbName() {
        return String.valueOf(TEST_DB_PATH);
    }
}
