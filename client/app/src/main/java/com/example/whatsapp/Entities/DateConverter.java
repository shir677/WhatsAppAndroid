package com.example.whatsapp.Entities;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    public static String getCreatedDate(String created) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat todayFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        SimpleDateFormat otherFormat = new SimpleDateFormat("MM-dd-yy", Locale.getDefault());

        try {
            Date date = inputFormat.parse(created);

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if (year == currentYear && month == currentMonth && day == currentDay) {
                return todayFormat.format(date);
            } else {
                return otherFormat.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @TypeConverter
    public static Date toDate(Long dateLong){
        SimpleDateFormat sdf = new SimpleDateFormat("\"yyyy'-'MM'-'dd'T'HH':'mm':'ss.fffffffK\"");
        return dateLong == null? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
