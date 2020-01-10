package Model.API.Journal.EntryComponents;

import Model.ErrorHandling.Exceptions.DateExceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Date {
    private int myMonth;
    private int myDay;
    private int myYear;
    private int myHour;
    private int myMinute;
    private int mySecond;
    private static final String FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(FORMAT);

    public Date()  {
        String current = getCurrentDate();
        try {
            parseDateString(current);
        }
        catch(Exception e){
            System.out.println("parseDateString method is incorrect, or FORMAT needs to be altered");
            System.out.println(e.getStackTrace());
        }
    }


    public static LocalDateTime getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public int compareTo(Date d2){
        int[] order1 = makeOrders(this);
        int[] order2 = makeOrders(d2);
        for(int i = 0; i<6; i++){
            int dif = order1[i] - order2[i];
            if(dif!=0){
            return dif;
        }
        }
        return -1;
    }

    private int[] makeOrders(Date d){
        return new int[]{d.getMyYear(), d.getMyMonth(), d.getMyMinute(), d.getMyHour(), d.getMyMinute(), d.getMySecond()};
    }


    public static boolean isValidFormat(String value) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(FORMAT);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }


}
