package ru.agpu.artikproject.background_work.settings_layout.user_background;

import android.app.Activity;
import android.widget.CheckBox;

import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;

public class UserBackgroundCheckBoxListener {
    /**
     * Слушатель нажатий на userBackgroundCheckBox
     *
     * @param act                    Активити
     * @param userBackgroundCheckBox Чекбокс
     */
    public UserBackgroundCheckBoxListener(Activity act, CheckBox userBackgroundCheckBox) {
        userBackgroundCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            MySharedPreferences.put(act.getApplicationContext(), "enable_background_user", isChecked);
            new UserBackgroundCheckboxSetVisible(act, isChecked);
        });
    }
}