package ru.agpu.artikproject.background_work.settings_layout.user_background;

import android.app.Activity;
import android.graphics.Color;
import android.widget.SeekBar;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;

public class UserSeekBarSetting {
    /**
     * Устанавливает ползунки в сохраненное состояние
     * @param act Активити
     * @param seekBar Ползунок
     * @param lightSeekBar Светлый ползунок(true) или темный(false)
     */
    public UserSeekBarSetting(Activity act, SeekBar seekBar, boolean lightSeekBar) {
        if(lightSeekBar) { // Если ползунок светлый
            int level = MySharedPreferences.INSTANCE.get(act.getApplicationContext(), "light_darker_level", 30); // Получаем сохраненный уровень ползунка (если его нет, то ставим 30%)
            seekBar.setProgress(level); // Устанавливаем полученый уровень
            act.findViewById(R.id.background_light_image_selector_darker).setBackgroundColor(Color.argb((int) (level * 2.5), 255, 255, 255)); // Затемняем мини-фон
        }
        else { // Если ползунок темный
            int level = MySharedPreferences.INSTANCE.get(act.getApplicationContext(), "dark_darker_level", 30); // Получаем сохраненный уровень ползунка (если его нет, то ставим 30%)
            seekBar.setProgress(level); // Устанавливаем полученый уровень
            act.findViewById(R.id.background_dark_image_selector_darker).setBackgroundColor(Color.argb((int) (level * 2.5), 0, 0, 0)); // Затемняем мини-фон
        }
    }
}
