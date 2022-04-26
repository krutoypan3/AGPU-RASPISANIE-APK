package com.example.artikproject.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.artikproject.R;
import com.example.artikproject.background_work.SetNewBackground;
import com.example.artikproject.background_work.settings_layout.BackgroundDarkerListener;
import com.example.artikproject.background_work.settings_layout.FichaShow;
import com.example.artikproject.background_work.settings_layout.ImageBackgroundPictureSetting;
import com.example.artikproject.background_work.settings_layout.ThemeRadioButtonSetting;
import com.example.artikproject.background_work.settings_layout.ThemeRadioGroupListener;
import com.example.artikproject.background_work.settings_layout.UserBackgroundCheckBoxListener;
import com.example.artikproject.background_work.settings_layout.UserBackgroundCheckBoxSetting;
import com.example.artikproject.background_work.settings_layout.UserBackgroundSelectorListener;


public class Settings_layout extends Activity {
    ImageView userBackgroundLightSelector;
    ImageView userBackgroundDarkSelector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        SetNewBackground.setting(findViewById(R.id.settings_relative_layout)); // Установка нового фона | Должно быть после setContentView
        CheckBox userBackgroundCheckBox = findViewById(R.id.my_app_background_checkbox);
        userBackgroundLightSelector = findViewById(R.id.background_light_image_selector);
        userBackgroundDarkSelector = findViewById(R.id.background_dark_image_selector);

        // Установка фона для картинок с фонами
        new ImageBackgroundPictureSetting(this, userBackgroundLightSelector, userBackgroundDarkSelector);

        // Проверка количества собранных пасхалок
        new FichaShow(this).start();

        // Установка позиции themeRadioButton
        new ThemeRadioButtonSetting(this);

        // Прослушка нажатий на themeRadioGroup
        new ThemeRadioGroupListener(this);

        // Установка позиции в userBackgroundCheckBox
        new UserBackgroundCheckBoxSetting(this, userBackgroundCheckBox);

        // Прослушка нажатий на userBackgroundCheckBox
        new UserBackgroundCheckBoxListener(this, userBackgroundCheckBox);

        // Прослушка нажатий на BackgroundDarker
        new BackgroundDarkerListener(this);

        // Прослушка нажатий на userBackgroundLightSelector
        new UserBackgroundSelectorListener(this, userBackgroundLightSelector, "background_light");

        // Прослушка нажатий на userBackgroundDarkSelector
        new UserBackgroundSelectorListener(this, userBackgroundDarkSelector, "background_dark");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            new ImageBackgroundPictureSetting(this, userBackgroundLightSelector, userBackgroundDarkSelector);
        }
    }

}
