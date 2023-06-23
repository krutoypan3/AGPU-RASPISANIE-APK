package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.bumptech.glide.Glide;

import java.io.IOException;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements;

public class FichaRicardo {
    static MediaPlayer mediaPlayer = new MediaPlayer();

    public FichaRicardo(Activity act){
        FichaAchievements.Companion.put(act.getApplicationContext(), "ficha_ricardo");
        CustomAlertDialog cdd = new CustomAlertDialog(act, "ricardo_pasha");
        cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        cdd.show();
        Glide.with(act).asGif().load("https://i.ibb.co/JHf4whr/ricardo-ricardo-milos.gif").into(cdd.para_info_photo);
        AudioManager audioManager = (AudioManager) act.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
        String url = "https://ruo.morsmusic.org/load/1602697468/Halogen_-_U_Got_That_(musmore.com).mp3"; // your URL here
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
            mediaPlayer.start();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
