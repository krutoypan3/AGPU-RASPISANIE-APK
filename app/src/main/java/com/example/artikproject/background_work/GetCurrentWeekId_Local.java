package com.example.artikproject.background_work;

import android.content.Context;

import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class GetCurrentWeekId_Local {
    /**
     * Получение последнего номера недели сохраненного в локальной базе данных
     * @return Номер недели
     */
    public static int get(Context context){
        return MySharedPreferences.get(context, "week_id", 0);
    }
}
