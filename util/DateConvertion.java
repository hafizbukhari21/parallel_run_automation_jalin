package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateConvertion {
    public static String SetDate(String CurrentDate, Boolean isNext){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(CurrentDate, formatter);

        // Subtract or addition one day
        LocalDate previousDate = isNext?date.plusDays(1):date.minusDays(1);

        // Format the previous date back to string format
        return previousDate.format(formatter);

    }
}
