package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.graphics.Color;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;
import com.example.artikproject.background_work.theme.Theme;

public class UserSeekBarListener {
    /**
     * Слушатель изменения ползунка затемнителя
     * @param act Активити
     * @param seekBar SeekBar (ползунок)
     * @param lightSeekBar Светлый ползунок (true) или темный (false)
     */
    public UserSeekBarListener(Activity act, SeekBar seekBar, boolean lightSeekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int color;
                if (lightSeekBar) { // Если светлый ползунок
                    color = Color.argb((int) (seekBar.getProgress() * 2.5), 255, 255, 255); // Делаем затемнитель
                    act.findViewById(R.id.background_light_image_selector_darker).setBackgroundColor(color); // Затемняем мини-фон
                }
                else{ // Если темный ползунок
                    color = Color.argb((int) (seekBar.getProgress() * 2.5), 0, 0, 0); // Делаем засветлитель
                    act.findViewById(R.id.background_dark_image_selector_darker).setBackgroundColor(color); // Затемняем мини-фон
                }
                int theme = Theme.get(act.getApplicationContext());
                if (theme == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM){
                    theme = Theme.getCurrentSystemTheme(act.getApplicationContext());
                }
                // Если текущая тема совпадает с темой изменяемого ползунка
                if((theme == AppCompatDelegate.MODE_NIGHT_NO) == lightSeekBar)
                    act.findViewById(R.id.background_darker).setBackgroundColor(color); // Меняем цвет фона в настройках
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Это просто затычка
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // После того как пользователь отпустит ползунок, сохраняем новые значения затемнителя
                if (lightSeekBar) // Если светлый ползунок - сохраняем засветлитель
                    MySharedPreferences.put(act.getApplicationContext(), "light_darker_level", seekBar.getProgress());
                else // Если темный ползунок - сохраняем затемнитель
                    MySharedPreferences.put(act.getApplicationContext(), "dark_darker_level", seekBar.getProgress());
            }
        });
    }
}
