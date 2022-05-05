package com.example.artikproject.background_work.settings_layout.ficha;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.artikproject.R;

import java.util.Random;

public class BackgroundDarkerListener {
    /**
     * Слкшатель нажатий на фон в настройках (пасхалочки)
     * @param act Активити
     */
    @SuppressLint("ClickableViewAccessibility")
    public BackgroundDarkerListener(Activity act){
        RelativeLayout backgroundDarker = act.findViewById(R.id.background_darker);
        ImageView animImage = act.findViewById(R.id.animImage);
        backgroundDarker.setOnTouchListener((v, event) -> {
            animImage.setX(event.getX()-100);
            animImage.setY(event.getY()-100);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    Ficha_achievements.put(act.getApplicationContext(), "ficha_setting_logo");
                    new FichaShow(act);
                    animImage.setVisibility(View.VISIBLE);
                    if (new Random().nextInt(20) == 0){
                        animImage.setImageResource(R.drawable.ficha_leonardo);
                        Ficha_achievements.put(act.getApplicationContext(), "ficha_setting_leonardo");
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    animImage.setVisibility(View.INVISIBLE);
                    animImage.setImageResource(R.drawable.agpu_ico);
                    break;
            }
            return true;
        });
    }
}
