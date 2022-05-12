package ru.agpu.artikproject.background_work;

import android.app.Activity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.MainActivity;


public class Starter_MainActivity extends Thread {
    final Activity act;
    final ImageView loading_ico;

    /**
     * Показывает список всех факультетов и их групп
     * @param act Активити
     * @param v View
     */
    public Starter_MainActivity(Activity act, ImageView v){
        this.act = act;
        loading_ico = v;
    }

    @Override
    public void run() {
        try{
            Animation animRotate = AnimationUtils.loadAnimation(act.getApplicationContext(), R.anim.scale_long);
            loading_ico.startAnimation(animRotate);
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(act.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            act.startActivity(intent);
            act.finishAffinity();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

