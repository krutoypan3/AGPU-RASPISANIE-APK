package com.example.artikproject.layout;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;
import com.example.artikproject.background_work.theme.Theme;

public class Settings_layout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        RadioGroup radioGroup_theme = findViewById(R.id.theme_radio_group);
        RadioButton radioButton_system = findViewById(R.id.theme_system);
        RadioButton radioButton_dark = findViewById(R.id.theme_dark);
        RadioButton radioButton_light = findViewById(R.id.theme_light);

        switch (Theme.current_theme){
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

        radioGroup_theme.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case(R.id.theme_system):
                    Theme.set(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case(R.id.theme_dark):
                    Theme.set(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case(R.id.theme_light):
                    Theme.set(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
        });


    }
}
