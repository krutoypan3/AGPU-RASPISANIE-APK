package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class ImageBackgroundPictureSetting {
    public ImageBackgroundPictureSetting(Activity act, ImageView userBackgroundLightSelector, ImageView userBackgroundDarkSelector){
        String background_light = MySharedPreferences.get(act.getApplicationContext(), "background_light", "");
        if (!background_light.equals("")){ // Если светлая картинка есть
            userBackgroundLightSelector.setImageURI(null); // Без обнуления новая картинка не встанет
            userBackgroundLightSelector.setImageURI(Uri.parse(background_light)); // Установка новой картинки
        }
        String background_dark = MySharedPreferences.get(act.getApplicationContext(), "background_dark", "");
        if (!background_dark.equals("")){ // Если темная картинка есть
            userBackgroundDarkSelector.setImageURI(null); // Без обнуления новая картинка не встанет
            userBackgroundDarkSelector.setImageURI(Uri.parse(background_dark)); // Установка новой картинки
        }
    }
}
