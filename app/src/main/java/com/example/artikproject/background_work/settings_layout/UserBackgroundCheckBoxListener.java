package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

import com.example.artikproject.R;
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
                act.findViewById(R.id.layout_change_background).setVisibility(View.VISIBLE);
                MySharedPreferences.put(act.getApplicationContext(), "enable_background_user", true);
            }
            else {
                act.findViewById(R.id.layout_change_background).setVisibility(View.INVISIBLE);
                MySharedPreferences.put(act.getApplicationContext(), "enable_background_user", false);
            }
        });
    }
}
