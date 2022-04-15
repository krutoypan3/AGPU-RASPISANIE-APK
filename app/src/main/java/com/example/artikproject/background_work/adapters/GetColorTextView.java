package com.example.artikproject.background_work.adapters;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;


public class GetColorTextView {
    /**
     * Получаем основной цвет текста для текущей темы
     * @return R.color.(цвет)
     */
    public static int get(){
        int a = AppCompatDelegate.getDefaultNightMode();
        if (a == 2){
            return R.color.gold;
        }
        else{
            return R.color.black;
        }
    }
}
