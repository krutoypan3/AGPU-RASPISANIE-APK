package com.example.artikproject.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.artikproject.R;
import com.example.artikproject.background_work.Ficha_achievements;
import com.example.artikproject.background_work.ImageSelecter;
import com.example.artikproject.background_work.SetNewBackground;
import com.example.artikproject.background_work.datebase.MySharedPreferences;
import com.example.artikproject.background_work.settings_layout.FichaShow;
import com.example.artikproject.background_work.settings_layout.ThemeRadioButtonSetting;
import com.example.artikproject.background_work.theme.Theme;

import java.util.Random;

public class Settings_layout extends Activity {
    ImageView background_light_image_selector;
    ImageView background_dark_image_selector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        SetNewBackground.setting(findViewById(R.id.settings_relative_layout)); // Установка нового фона | Должно быть после setContentView
        RadioGroup radioGroup_theme = findViewById(R.id.theme_radio_group);
        RadioButton radioButton_system = findViewById(R.id.theme_system);
        RadioButton radioButton_dark = findViewById(R.id.theme_dark);
        RadioButton radioButton_light = findViewById(R.id.theme_light);
        background_light_image_selector = findViewById(R.id.background_light_image_selector);
        background_dark_image_selector = findViewById(R.id.background_dark_image_selector);
        setImageBackgroundPicture(); // Установка фона для картинок с фонами
        new FichaShow(this).start(); // Проверка количества собранных фич
        // Установка позиции в ThemeRadioButton
        new ThemeRadioButtonSetting(this, radioButton_system, radioButton_dark, radioButton_light);

        radioGroup_theme.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case(R.id.theme_system):
                    Theme.set(getApplicationContext(), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case(R.id.theme_dark):
                    Theme.set(getApplicationContext(), AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case(R.id.theme_light):
                    Theme.set(getApplicationContext(), AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
            Toast.makeText(getApplicationContext(), R.string.theme_apply, Toast.LENGTH_SHORT).show();
        });

        RelativeLayout darker = findViewById(R.id.darker);
        // Отслеживание нажатия на фон в настройках (пасхалочки)
        ImageView animImage = findViewById(R.id.animImage);
        darker.setOnTouchListener((v, event) -> {
            animImage.setX(event.getX()-100);
            animImage.setY(event.getY()-100);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Ficha_achievements.put(getApplicationContext(), "ficha_setting_logo");
                    new FichaShow(this).start();
                    animImage.setVisibility(View.VISIBLE);
                    if (new Random().nextInt(20) == 0){
                        animImage.setImageResource(R.drawable.ficha_leonardo);
                        Ficha_achievements.put(getApplicationContext(), "ficha_setting_leonardo");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    animImage.setVisibility(View.INVISIBLE);
                    animImage.setImageResource(R.drawable.agpu_ico);
                    break;
            }
            return true;
        });


        // Обработчик нажатия на картинку с выбором светлого фонового изображения
        CheckBox checkBox = findViewById(R.id.my_app_background_checkbox);
        if (MySharedPreferences.get(getApplicationContext(), "enable_background_user", false)){
            checkBox.setChecked(true);
            findViewById(R.id.layout_change_background).setVisibility(View.VISIBLE);
            MySharedPreferences.put(getApplicationContext(), "enable_background_user", true);
        }

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                findViewById(R.id.layout_change_background).setVisibility(View.VISIBLE);
                MySharedPreferences.put(getApplicationContext(), "enable_background_user", true);
            }
            else {
                findViewById(R.id.layout_change_background).setVisibility(View.INVISIBLE);
                MySharedPreferences.put(getApplicationContext(), "enable_background_user", false);
            }
        });

        // Обработчик нажатия на картинку с выбором светлого фонового изображения
        background_light_image_selector.setOnClickListener(v -> {
            Intent ImageSelecter = new Intent(getApplicationContext(), ImageSelecter.class);
            ImageSelecter.putExtra("background", "background_light");
            startActivityForResult(ImageSelecter, 1);
        });


        // Обработчик нажатия на картинку с выбором темного фонового изображения
        background_dark_image_selector.setOnClickListener(v -> {
            Intent ImageSelecter = new Intent(getApplicationContext(), ImageSelecter.class);
            ImageSelecter.putExtra("background", "background_dark");
            startActivityForResult(ImageSelecter, 1);
        });

    }

    private void setImageBackgroundPicture(){
        String background_light = MySharedPreferences.get(getApplicationContext(), "background_light", "");
        if (!background_light.equals("")){ // Если светлая картинка есть
            background_light_image_selector.setImageURI(null); // Без обнуления новая картинка не встанет
            background_light_image_selector.setImageURI(Uri.parse(background_light)); // Установка новой картинки
        }
        String background_dark = MySharedPreferences.get(getApplicationContext(), "background_dark", "");
        if (!background_dark.equals("")){ // Если темная картинка есть
            background_dark_image_selector.setImageURI(null); // Без обнуления новая картинка не встанет
            background_dark_image_selector.setImageURI(Uri.parse(background_dark)); // Установка новой картинки
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            setImageBackgroundPicture();
        }
    }

}
