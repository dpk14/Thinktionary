package Controller.Exceptions.Utils;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {
    public static String exceptionToJSON(Exception e){
        StackTraceElement elements[] = e.getStackTrace();
        for (int i = 0, n = elements.length; i < n; i++) {
            System.err.println(elements[i].getFileName()
                    + ":" + elements[i].getLineNumber()
                    + ">> "
                    + elements[i].getMethodName() + "()");
        }
        return "Exception";
    }
}
