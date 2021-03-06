package ru.agpu.artikproject.background_work.settings_layout.theme_radio_group;

import android.app.Activity;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import ru.agpu.artikproject.background_work.theme.Theme;
import ru.agpu.artikproject.R;

public class ThemeRadioGroupListener {
    /**
     * Прослушивает нажатия на элементы ThemeRadioGroup
     * @param act Активити
     */
    public ThemeRadioGroupListener(Activity act){
        RadioGroup themeRadioGroup = act.findViewById(R.id.theme_radio_group);
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case(R.id.theme_system):
                    Theme.set(act.getApplicationContext(), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case(R.id.theme_dark):
                    Theme.set(act.getApplicationContext(), AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case(R.id.theme_light):
                    Theme.set(act.getApplicationContext(), AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
            Toast.makeText(act.getApplicationContext(), R.string.theme_apply, Toast.LENGTH_SHORT).show();
        });
    }
}
