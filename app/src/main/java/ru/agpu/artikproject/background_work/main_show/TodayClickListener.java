package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.TextView;

import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class TodayClickListener {
    /**
     * Фича отвечающая за прослушку нажатий на текущую дату
     * @param act Активити
     * @param today TextView
     */
    public TodayClickListener(Activity act, TextView today){
        today.setOnClickListener(v -> {
            today.startAnimation(MainActivity.animScale);
            if (new Random().nextInt(30) == 0) {
                Ficha_achievements.put(act.getApplicationContext(), "ficha_today");
                AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                MediaPlayer mp = MediaPlayer.create(act, R.raw.povezlo_povezlo);
                mp.start();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/9b73CLy/2022-04-27-183814.png"));
                act.startActivity(intent);
            }
        });
    }
}
