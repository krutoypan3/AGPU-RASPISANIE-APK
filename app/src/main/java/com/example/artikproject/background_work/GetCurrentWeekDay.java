package com.example.artikproject.background_work;

import java.util.Date;

public class GetCurrentWeekDay {
    public static int get(){
        // Получение актуального текущего времени
        long date_ms = new Date().getTime();// Нужно ли це переменная?
        int week_day = new Date(date_ms).getDay() - 1; // дня недели
        if (week_day == -1){ // Если будет воскресенье, то будет показана суббота
            week_day = 5;
        }
        return week_day;
    }
}
