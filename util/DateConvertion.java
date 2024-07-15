package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConvertion {
    public static String SetDate(String CurrentDate, Boolean isNext){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(CurrentDate, formatter);

        // Subtract or addition one day
        LocalDate previousDate = isNext?date.plusDays(1):date.minusDays(1);

        // Format the previous date back to string format
        return previousDate.format(formatter);

    }

    public static String NewDateFormat(String dateStr){
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            // Parse the source date string into a Date object
            Date date = sourceFormat.parse(dateStr);
            
            // Format the Date object into the target date string
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String NewDateFormatPrev(String Dates){
        String NewDateFormat = NewDateFormat(Dates);
        DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(NewDateFormat,targetFormat);
            LocalDate previousDate = date.minusDays(1);
            return targetFormat.format(previousDate);
        } catch (Exception e) {
            return null;
        }
    
    }
}
