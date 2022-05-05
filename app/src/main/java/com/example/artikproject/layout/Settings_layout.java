package com.example.artikproject.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.artikproject.R;
import com.example.artikproject.background_work.CustomBackground;
import com.example.artikproject.background_work.settings_layout.ficha.BackgroundDarkerListener;
import com.example.artikproject.background_work.settings_layout.ficha.FichaShow;
import com.example.artikproject.background_work.settings_layout.user_background.ImageBackgroundPictureSetting;
import com.example.artikproject.background_work.settings_layout.theme_radio_group.ThemeRadioButtonSetting;
import com.example.artikproject.background_work.settings_layout.theme_radio_group.ThemeRadioGroupListener;
import com.example.artikproject.background_work.settings_layout.user_background.UserBackgroundCheckBoxListener;
import com.example.artikproject.background_work.settings_layout.user_background.UserBackgroundCheckBoxSetting;
import com.example.artikproject.background_work.settings_layout.user_background.UserBackgroundSelectorListener;
import com.example.artikproject.background_work.settings_layout.user_background.UserSeekBarListener;
import com.example.artikproject.background_work.settings_layout.user_background.UserSeekBarSetting;


public class Settings_layout extends AppCompatActivity {
    ImageView userBackgroundLightSelector;
    ImageView userBackgroundDarkSelector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        findViewById(R.id.settings_relative_layout).setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.background_darker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

        CheckBox userBackgroundCheckBox = findViewById(R.id.my_app_background_checkbox);
        userBackgroundLightSelector = findViewById(R.id.background_light_image_selector);
        userBackgroundDarkSelector = findViewById(R.id.background_dark_image_selector);

        // Установка фона для картинок с фонами
        new ImageBackgroundPictureSetting(this, userBackgroundLightSelector, userBackgroundDarkSelector);

        // Проверка количества собранных пасхалок
        new FichaShow(this);

        // Установка позиции themeRadioButton
        new ThemeRadioButtonSetting(this);

        // Прослушка нажатий на themeRadioGroup
        new ThemeRadioGroupListener(this);

        // Установка позиции в userBackgroundCheckBox
        new UserBackgroundCheckBoxSetting(this, userBackgroundCheckBox);

        // Прослушка нажатий на userBackgroundCheckBox
        new UserBackgroundCheckBoxListener(this, userBackgroundCheckBox);

        // Установка ползунка светлого затемнителя в нужное положение
        new UserSeekBarSetting(this, findViewById(R.id.seekBarLight), true);

        // Установка ползунка темного затемнителя в нужное положение
        new UserSeekBarSetting(this, findViewById(R.id.seekBarDark), false);

        // Прослушка изменения ползунка светлого затемнителя
        new UserSeekBarListener(this, findViewById(R.id.seekBarLight), true);

        // Прослушка изменения ползунка темного затемнителя
        new UserSeekBarListener(this, findViewById(R.id.seekBarDark), false);

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
