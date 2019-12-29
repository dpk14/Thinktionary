package BackEnd.Main.Components;

import BackEnd.Exceptions.DateExceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Date {
    private int myMonth;
    private int myDay;
    private int myYear;
    private int myHour;
    private int myMinute;
    private int mySecond;
    private final String FORMAT = "yyyy/MM/dd HH:mm:ss";

    public Date(){
        String current = getCurrentDate();
        String[] splt = current.split("/");
        String[] splt2 = current.substring(1).split(":");
        trySetDate(Integer.parseInt(splt[1]), Integer.parseInt(splt[2]), Integer.parseInt(splt[0]),
                    Integer.parseInt(splt2[0]), Integer.parseInt(splt2[1]), Integer.parseInt(splt2[2]));
    }

    public Date(int month, int day, int year, int hour, int min, int second){
        trySetDate(month, day, year, hour, min, second);
    }

    public Date(int month, int day, int year){
        trySetDate(month, day, year, 0, 0, 0);
    }

    private void trySetDate(int month, int day, int year, int hour, int min, int second){
        try{
            setDate(month, day, year, hour, min, second);
        }
        catch(InvalidDateException e){
            System.out.println(e); //TODO: change to display window
        }
    }

    private void setDate(int month, int day, int year, int hour, int min, int second) throws InvalidDateException {
        Map<Integer, Integer> monthToDays = makeMap();
        if(myMonth <= 0 || myMonth > 12) throw new InvalidMonthException();
        myMonth = month;
        if(myDay <= 0 || myDay > 31 || (monthToDays.containsKey(month) && myDay > monthToDays.get(month))){
            throw new InvalidDayException();
        }
        myDay = day;
        myYear = year;
        if(myHour <= 0 || myHour > 12) throw new InvalidHourException();
        myHour = hour;
        if(myMinute < 0 || myMinute >= 60) throw new InvalidMinuteException();
        myMinute = min;
        if(myHour < 0 || myHour >= 60) throw new InvalidSecondException();
        mySecond = second;
    }

    private Map<Integer, Integer> makeMap() {
        Map<Integer, Integer> monthToDays = new HashMap<>();
        monthToDays.put(2, 29);
        monthToDays.put(4, 30);
        monthToDays.put(6, 30);
        monthToDays.put(9, 30);
        monthToDays.put(11, 30);
        return monthToDays;
    }

    private String getCurrentDate(){
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

    public int getMyDay() {
        return myDay;
    }

    public int getMyHour() {
        return myHour;
    }

    public int getMyMinute() {
        return myMinute;
    }

    public int getMyMonth() {
        return myMonth;
    }

    public int getMySecond() {
        return mySecond;
    }

    public int getMyYear() {
        return myYear;
    }

}
