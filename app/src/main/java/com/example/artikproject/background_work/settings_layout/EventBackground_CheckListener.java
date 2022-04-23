package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.background_work.SetNewBackground;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class EventBackground_CheckListener {
    /**
     * Слушатель изменения поля ивентовых фонов
     * @param checkBox Чекбокс
     * @param act Активити
     */
    public EventBackground_CheckListener(Activity act, CheckBox checkBox){
        // Если параметр is_event_background , то ставим в чекбоксе галочку
        checkBox.setChecked(MySharedPreferences.get(act.getApplicationContext(), "is_event_background", false));

        // Запускаем прослушку нажатий на чекбокс
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> { // При нажатии
            // Выводим сообщение о необходимости перезапустить приложение
            Toast.makeText(act.getApplicationContext(), R.string.theme_apply, Toast.LENGTH_SHORT).show();
            if (isChecked){ // Если поставлена галочка, включаем параметр is_event_background и сохраняем фон
                MySharedPreferences.put(act.getApplicationContext(), "is_event_background", true);
                SetNewBackground.set(act.getApplicationContext(), R.drawable.ivent_background);
            }
            else{ // Если галочка убрана, отключаем параметр и сохраняем стандартный фон
                MySharedPreferences.put(act.getApplicationContext(), "is_event_background", false);
                SetNewBackground.set(act.getApplicationContext(), R.drawable.background);
            }
        });
    }
}
