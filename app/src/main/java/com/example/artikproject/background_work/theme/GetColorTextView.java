package com.example.artikproject.background_work.theme;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;


public class GetColorTextView {
    /**
     * Получаем основной цвет текста для текущей темы
     * @return R.color.(цвет)
     */
    public static int get(Context context){
        if (Theme.current_theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
            int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    return R.color.black;
                case Configuration.UI_MODE_NIGHT_YES:
                    return R.color.gold;
            }
        }
        else{
            if (Theme.current_theme == AppCompatDelegate.MODE_NIGHT_YES){
                return R.color.gold;
            }
            else{
                return R.color.black;
            }
        }
        return R.color.black; // TODO заглушка
    }
}
