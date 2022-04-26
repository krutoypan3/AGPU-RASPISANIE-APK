package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class UserBackgroundSelectorListener {
    public UserBackgroundSelectorListener(Activity act, ImageView userBackgroundLightSelector, String dark_ot_light){
        // Обработчик нажатия на картинку с выбором светлого фонового изображения
        userBackgroundLightSelector.setOnClickListener(v -> {
            Intent ImageSelecter = new Intent(act.getApplicationContext(), com.example.artikproject.background_work.ImageSelecter.class);
            ImageSelecter.putExtra("background", dark_ot_light);
            act.startActivityForResult(ImageSelecter, 1);
        });
    }
}
