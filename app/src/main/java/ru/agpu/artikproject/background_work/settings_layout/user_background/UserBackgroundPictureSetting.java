package ru.agpu.artikproject.background_work.settings_layout.user_background;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;

public class UserBackgroundPictureSetting {
    /**
     * Устанавливает картинку на задний фон в настройках фона
     * @param act Активити
     * @param userBackgroundLightSelector Светлый фон
     * @param userBackgroundDarkSelector Темный фон
     */
    public UserBackgroundPictureSetting(Activity act, ImageView userBackgroundLightSelector, ImageView userBackgroundDarkSelector){
        String background_light = MySharedPreferences.INSTANCE.get(act.getApplicationContext(), "background_light", "");
        if (!background_light.equals("")){ // Если светлая картинка есть
            userBackgroundLightSelector.setImageURI(null); // Без обнуления новая картинка не встанет
            userBackgroundLightSelector.setImageURI(Uri.parse(background_light)); // Установка новой картинки
        }
        String background_dark = MySharedPreferences.INSTANCE.get(act.getApplicationContext(), "background_dark", "");
        if (!background_dark.equals("")){ // Если темная картинка есть
            userBackgroundDarkSelector.setImageURI(null); // Без обнуления новая картинка не встанет
            userBackgroundDarkSelector.setImageURI(Uri.parse(background_dark)); // Установка новой картинки
        }
    }
}
