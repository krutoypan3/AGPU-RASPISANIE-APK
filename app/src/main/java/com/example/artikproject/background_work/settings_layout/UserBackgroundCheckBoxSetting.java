package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class UserBackgroundCheckBoxSetting{

    public UserBackgroundCheckBoxSetting(Activity act, CheckBox userBackgroundCheckBox) {
        if (MySharedPreferences.get(act.getApplicationContext(), "enable_background_user", false)){
            userBackgroundCheckBox.setChecked(true);
            act.findViewById(R.id.layout_change_background).setVisibility(View.VISIBLE);
            MySharedPreferences.put(act.getApplicationContext(), "enable_background_user", true);
        }
    }
}
