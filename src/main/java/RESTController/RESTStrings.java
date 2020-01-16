package RESTController;

public class RESTStrings {
   private static final String JOURNAL_ATTRIBUTE = "journal";
   private static final String NO_SESSION_EXCEPTION = "Must login first to create session";

    public static String getJournalAttribute() {
        return JOURNAL_ATTRIBUTE;
    }

    public static String getNoSessionException() {
        return NO_SESSION_EXCEPTION;
    }
}
