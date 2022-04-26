package com.example.artikproject.layout;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.artikproject.R;
import com.example.artikproject.background_work.Ficha_achievements;
import com.example.artikproject.background_work.GalleryUtil;
import com.example.artikproject.background_work.ImageSelecter;
import com.example.artikproject.background_work.SetNewBackground;
import com.example.artikproject.background_work.datebase.MySharedPreferences;
import com.example.artikproject.background_work.theme.Theme;
import com.mikepenz.iconics.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Settings_layout extends AppCompatActivity {
    MediaPlayer mp;
    ImageView background_image_selector;
    private Uri selected_pic_uri;

    @Override
    public void onBackPressed(){
        mp.stop();
        super.onBackPressed();
    }

    public void ficha_show(){
        TextView ficha_count_text = findViewById(R.id.ficha_count_text);
        TextView ficha_count = findViewById(R.id.ficha_count);
        ImageView ficha_nyan = findViewById(R.id.ficha_nyan);
        int fic_count = Ficha_achievements.get(getApplicationContext());
        if (fic_count > 0){
            ficha_count_text.setVisibility(View.VISIBLE);
            ficha_count.setVisibility(View.VISIBLE);
            String newText = fic_count + " / " + Ficha_achievements.MAX_FICHA_COUNT;
            ficha_count.setText(newText);
        }
        if (fic_count == 8){
            ficha_count_text.setText("Пасхалки?.Ты собрал их все: ");
            mp.start();
            ficha_nyan.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load("https://www.nyan.cat/cats/original.gif").into(new DrawableImageViewTarget(ficha_nyan));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        background_image_selector = findViewById(R.id.background_image_selector);
        SetNewBackground.setting(findViewById(R.id.settings_relative_layout)); // Установка нового фона | Должно быть после setContentView
        mp = MediaPlayer.create(getApplicationContext(), R.raw.nyan_cat);
        RadioGroup radioGroup_theme = findViewById(R.id.theme_radio_group);
        RadioButton radioButton_system = findViewById(R.id.theme_system);
        RadioButton radioButton_dark = findViewById(R.id.theme_dark);
        RadioButton radioButton_light = findViewById(R.id.theme_light);
        ficha_show();

        switch (Theme.get(getApplicationContext())){
            case(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM):
                radioButton_system.setChecked(true);
                break;
            case(AppCompatDelegate.MODE_NIGHT_YES):
                radioButton_dark.setChecked(true);
                break;
            case(AppCompatDelegate.MODE_NIGHT_NO):
                radioButton_light.setChecked(true);
                break;
        }

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

        final RelativeLayout darker = findViewById(R.id.darker);
        // Отслеживание нажатия на фон в настройках (пасхалочки)
        ImageView animImage = findViewById(R.id.animImage);
        darker.setOnTouchListener((v, event) -> {
            animImage.setX(event.getX()-100);
            animImage.setY(event.getY()-100);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Ficha_achievements.put(getApplicationContext(), "ficha_setting_logo");
                    ficha_show();
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

        // Обработчик нажатия на картинку с выбором фонового изображения
        findViewById(R.id.background_image_selector).setOnClickListener(v -> {

            Intent ImageSelecter = new Intent(getApplicationContext(), ImageSelecter.class);
            startActivity(ImageSelecter);
        });

    }
}
