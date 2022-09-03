package ru.agpu.artikproject.background_work.main_show.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.debug.Device_info;
import ru.agpu.artikproject.background_work.debug.LeaveReview;
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
import ru.agpu.artikproject.presentation.layout.Admin_panel;

public class FragmentSettingsShow extends Fragment {
    ImageView userBackgroundLightSelector;
    ImageView userBackgroundDarkSelector;

    public FragmentSettingsShow() {
        super(R.layout.fragment_main_activity_settings_show);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CheckBox userBackgroundCheckBox = view.findViewById(R.id.my_app_background_checkbox);
        userBackgroundLightSelector = view.findViewById(R.id.background_light_image_selector);
        userBackgroundDarkSelector = view.findViewById(R.id.background_dark_image_selector);

        Activity activity = (Activity) view.getContext();

        // Установка фона для картинок с фонами
        new UserBackgroundPictureSetting(activity, userBackgroundLightSelector, userBackgroundDarkSelector);

        // Проверка количества собранных пасхалок
        new FichaShow(activity);

        // Установка позиции themeRadioButton
        new ThemeRadioButtonSetting(activity);

        // Прослушка нажатий на themeRadioGroup
        new ThemeRadioGroupListener(activity);

        // Установка позиции в userBackgroundCheckBox
        new UserBackgroundCheckBoxSetting(activity, userBackgroundCheckBox);

        // Прослушка нажатий на userBackgroundCheckBox
        new UserBackgroundCheckBoxListener(activity, userBackgroundCheckBox);

        // Установка ползунка светлого затемнителя в нужное положение
        new UserSeekBarSetting(activity, view.findViewById(R.id.seekBarLight), true);

        // Установка ползунка темного затемнителя в нужное положение
        new UserSeekBarSetting(activity, view.findViewById(R.id.seekBarDark), false);

        // Прослушка изменения ползунка светлого затемнителя
        new UserSeekBarListener(activity, view.findViewById(R.id.seekBarLight), true);

        // Прослушка изменения ползунка темного затемнителя
        new UserSeekBarListener(activity, view.findViewById(R.id.seekBarDark), false);

        // Прослушка нажатий на BackgroundDarker
        new BackgroundDarkerListener(activity);

        // Прослушка нажатий на userBackgroundLightSelector
        new UserBackgroundSelectorListener(activity, userBackgroundLightSelector, "background_light");

        // Прослушка нажатий на userBackgroundDarkSelector
        new UserBackgroundSelectorListener(activity, userBackgroundDarkSelector, "background_dark");

        // Отслеживание нажатий на кнопку условий использования
        view.findViewById(R.id.terms_of_use_btn).setOnClickListener(view2 -> {
            CustomAlertDialog cdd = new CustomAlertDialog(activity, "about");
            cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
            cdd.show();
        });

        // Отслеживание нажатий на кнопку оставления отзыва
        view.findViewById(R.id.leave_a_review).setOnClickListener(view2 -> new LeaveReview(activity));

        TextView about_version_text = view.findViewById(R.id.about_version_text);
        try {
            String new_version_text = getString(R.string.app_name) + " " + Device_info.getAppVersion(view.getContext());
            if (CheckAppUpdate.app_have_update)
                new_version_text += " " + getString(R.string.an_update_is_available);
            about_version_text.setText(new_version_text);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Отслеживание нажатий на кнопку гитхаба
        view.findViewById(R.id.GitHub_btn).setOnClickListener(view2 -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases"))));

        view.findViewById(R.id.admin_btn).setOnClickListener(view2 -> startActivity(new Intent(view.getContext(), Admin_panel.class)));

    }
}
