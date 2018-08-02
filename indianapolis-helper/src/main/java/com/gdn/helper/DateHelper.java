package com.gdn.helper;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    public static Date setDay(int amountOfDayAddedFromToday){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, amountOfDayAddedFromToday);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
