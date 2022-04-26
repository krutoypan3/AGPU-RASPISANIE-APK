package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;
import com.example.artikproject.background_work.theme.Theme;

public class ThemeRadioButtonSetting{
    /**
     * Установка активного RadioButton
     * @param act Активити
     */
    public ThemeRadioButtonSetting(Activity act){
        RadioButton radioButton_system = act.findViewById(R.id.theme_system);
        RadioButton radioButton_dark = act.findViewById(R.id.theme_dark);
        RadioButton radioButton_light = act.findViewById(R.id.theme_light);
        switch (Theme.get(act.getApplicationContext())){
            case(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM):
                radioButton_system.setChecked(true);
                break;
            case(AppCompatDelegate.MODE_NIGHT_YES):
                radioButton_dark.setChecked(true);
                break;
            case(AppCompatDelegate.MODE_NIGHT_NO):
                radioButton_light.setChecked(true);
                break;
        }
    }
}

