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


    public static String getFormat() {
        return String.valueOf(FORMAT);
    }

    public static LocalDateTime makeDate(String createdDate) throws InvalidDBDateException{
        if(!Date.isValidFormat(createdDate)) throw new InvalidDBDateException(createdDate, Date.getFormat());
        return LocalDateTime.parse(createdDate, FORMATTER);

    }

    public static int compare(LocalDateTime d1, LocalDateTime d2){
        int[] order1 = makeOrders(d1);
        int[] order2 = makeOrders(d2);
        for(int i = 0; i<6; i++){
            int dif = order1[i] - order2[i];
            if(dif!=0){
            return dif;
        }
        }
        return -1;
    }

    private static int[] makeOrders(LocalDateTime d){
        return new int[]{d.getYear(), d.getMonth().getValue(), d.getDayOfMonth(), d.getHour(), d.getMinute(), d.getSecond()};
    }

    private static boolean isValidFormat(String value) {
        LocalDateTime ldt = null;

        try {
            ldt = LocalDateTime.parse(value, FORMATTER);
            String result = ldt.format(FORMATTER);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, FORMATTER);
                String result = ld.format(FORMATTER);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, FORMATTER);
                    String result = lt.format(FORMATTER);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }


    public static String LocalDateTimetoString(LocalDateTime date) {
        String month = Integer.toString(date.getMonthValue());
        month = month.length() == 1 ? "0" + month : month;
        String day = Integer.toString(date.getDayOfMonth());
        day = day.length() == 1 ? "0" + day : day;
        String hour = Integer.toString(date.getHour());
        hour = hour.length() == 1 ? "0" + hour : hour;
        String minute = Integer.toString(date.getMinute());
        minute = minute.length() == 1 ? "0" + minute : minute;
        String second = Integer.toString(date.getSecond());
        second = second.length() == 1 ? "0" + second : second;

        return date.getYear() + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second;
    }
}
