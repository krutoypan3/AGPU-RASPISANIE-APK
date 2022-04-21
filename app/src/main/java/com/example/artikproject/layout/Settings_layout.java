package com.example.artikproject.layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;
import com.example.artikproject.background_work.theme.Theme;

import java.util.Random;

public class Settings_layout extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        RadioGroup radioGroup_theme = findViewById(R.id.theme_radio_group);
        RadioButton radioButton_system = findViewById(R.id.theme_system);
        RadioButton radioButton_dark = findViewById(R.id.theme_dark);
        RadioButton radioButton_light = findViewById(R.id.theme_light);

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
        // Отслеживание нажати на фон в настройках (пасхалочки)
        ImageView animImage = findViewById(R.id.animImage);
        darker.setOnTouchListener((v, event) -> {
            animImage.setX(event.getX()-100);
            animImage.setY(event.getY()-100);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:{
                    animImage.setVisibility(View.VISIBLE);
                    if (new Random().nextInt(20) == 0){
                        animImage.setImageResource(R.drawable.ficha_leonardo);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP:{
                    animImage.setVisibility(View.INVISIBLE);
                    animImage.setImageResource(R.drawable.agpu_ico);
                    break;
                }
            }
            return true;
        });
    }
}
