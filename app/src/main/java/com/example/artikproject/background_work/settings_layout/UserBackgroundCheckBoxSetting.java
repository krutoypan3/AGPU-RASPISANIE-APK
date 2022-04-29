package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridLayout;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class UserBackgroundCheckBoxSetting{
    /**
     * Настраивает ЧекБокс, отвечающий за смену пользовательского фона
     * @param act Активити
     * @param userBackgroundCheckBox ЧекБокс
     */
    public UserBackgroundCheckBoxSetting(Activity act, CheckBox userBackgroundCheckBox) {
        if (MySharedPreferences.get(act.getApplicationContext(), "enable_background_user", false)){
            userBackgroundCheckBox.setChecked(true);
            new UserBackgroundCheckboxSetVisible(act, true);
        }
    }
}
