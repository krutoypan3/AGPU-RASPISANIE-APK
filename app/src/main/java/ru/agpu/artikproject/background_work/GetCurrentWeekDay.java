package ru.agpu.artikproject.background_work;

import java.util.Calendar;
import java.util.Date;

public class GetCurrentWeekDay {
    /**
     * Получить текущий день недели
     * @return День недели 0..5(пн-сб), -1(вс \ заменяется на субботу)
     */
    public static int get(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int week_day = cal.get(Calendar.DAY_OF_WEEK) - 2;
        if (week_day == -1){ // Если будет воскресенье, то будет показана суббота
            week_day = 5;
        }
        return week_day;
    }
}
