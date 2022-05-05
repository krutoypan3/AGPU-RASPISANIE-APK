package com.example.artikproject.background_work.settings_layout.user_background;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.example.artikproject.background_work.image_select_from_gallery.ImageSelector;

public class UserBackgroundSelectorListener {
    /**
     * Прослушивает нажатия на картинку с выбором фонового изображения
     * @param act Активити
     * @param userBackgroundLightSelector Изменяемый ImageView
     * @param dark_ot_light Светлый фон(background_light) или темный(background_dark)
     */
    public UserBackgroundSelectorListener(Activity act, ImageView userBackgroundLightSelector, String dark_ot_light){
        userBackgroundLightSelector.setOnClickListener(v -> {
            Intent ImageSelector = new Intent(act.getApplicationContext(), ImageSelector.class);
            ImageSelector.putExtra("background", dark_ot_light);
            act.startActivityForResult(ImageSelector, 1);
        });
    }
}
