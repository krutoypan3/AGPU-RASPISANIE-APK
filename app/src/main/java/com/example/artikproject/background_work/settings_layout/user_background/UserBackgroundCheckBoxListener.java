package com.example.artikproject.background_work.settings_layout.user_background;

import android.app.Activity;
import android.widget.CheckBox;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class UserBackgroundCheckBoxListener {
    /**
     * Слушатель нажатий на userBackgroundCheckBox
     * @param act Активити
     * @param userBackgroundCheckBox Чекбокс
     */
    public UserBackgroundCheckBoxListener(Activity act, CheckBox userBackgroundCheckBox){
        userBackgroundCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                MySharedPreferences.put(act.getApplicationContext(), "enable_background_user", true);
                new UserBackgroundCheckboxSetVisible(act, true);
            }
            else {
                MySharedPreferences.put(act.getApplicationContext(), "enable_background_user", false);
                new UserBackgroundCheckboxSetVisible(act, false);
            }
        });
    }
}
