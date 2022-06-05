package ru.agpu.artikproject.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.debug.Device_info;
import ru.agpu.artikproject.background_work.settings_layout.ficha.BackgroundDarkerListener;
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaShow;
import ru.agpu.artikproject.background_work.settings_layout.theme_radio_group.ThemeRadioButtonSetting;
import ru.agpu.artikproject.background_work.settings_layout.theme_radio_group.ThemeRadioGroupListener;
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundCheckBoxListener;
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundCheckBoxSetting;
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundPictureSetting;
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundSelectorListener;
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserSeekBarListener;
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserSeekBarSetting;
import ru.agpu.artikproject.background_work.theme.CustomBackground;


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
        new UserBackgroundPictureSetting(this, userBackgroundLightSelector, userBackgroundDarkSelector);

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

        // Отслеживание нажатий на кнопку условий использования
        findViewById(R.id.terms_of_use_btn).setOnClickListener(view -> {
            CustomAlertDialog cdd = new CustomAlertDialog(Settings_layout.this, "about");
            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
            cdd.show();
        });

        // Отслеживание нажатий на кнопку оставления отзыва
        findViewById(R.id.leave_a_review).setOnClickListener(view -> {
            CustomAlertDialog cdd = new CustomAlertDialog(Settings_layout.this, "feedback");
            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
            cdd.show();
        });

        TextView about_version_text = findViewById(R.id.about_version_text);
        try {
            String new_version_text = getString(R.string.app_name) + " " + Device_info.getAppVersion(getApplicationContext());
            if (CheckAppUpdate.app_have_update)
                new_version_text += " " + getString(R.string.an_update_is_available);
            about_version_text.setText(new_version_text);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Отслеживание нажатий на кнопку гитхаба
        findViewById(R.id.GitHub_btn).setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases"))));

        findViewById(R.id.admin_btn).setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Admin_panel.class)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            new UserBackgroundPictureSetting(this, userBackgroundLightSelector, userBackgroundDarkSelector);
        }
    }
}
