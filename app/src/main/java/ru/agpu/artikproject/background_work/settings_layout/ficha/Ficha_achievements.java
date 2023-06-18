package ru.agpu.artikproject.background_work.settings_layout.ficha;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;

/**
 * Класс отвечающий за подсчет и добавление собранных пасхалок
 */
public class Ficha_achievements {
    private static MediaPlayer mp;
    public static final String FICHA_GO_TO_HOME = "ficha_go_to_home";
    public static final String FICHA_WINDOWS = "ficha_windows";
    public static final String FICHA_KEYS = "ficha_keys";
    public static final String FICHA_KEYS_TWO = "ficha_keys_two";
    private static final String[] FICHA_LIST = new String[]{
            "ficha_setting_logo", "ficha_setting_leonardo",
            "ficha_para_lapshin", "ficha_refresh", "ficha_god",
            "ficha_today", "ficha_building_ico", "ficha_building_main_text", "ficha_ricardo",
            FICHA_GO_TO_HOME, FICHA_WINDOWS, FICHA_KEYS, FICHA_KEYS_TWO};

    private static final String[] FICHA_SECRET_LIST = new String[]{
            "ficha_para_lapshin"};

    public static final int MAX_FICHA_COUNT = FICHA_LIST.length - FICHA_SECRET_LIST.length;

    /**
     * Получить количество собранных пасхалок
     * @param context Контекст приложения
     */
    public static int get(Context context){
        int ficha_count = 0;
        for (String current_ficha : FICHA_LIST){
            if (MySharedPreferences.get(context, current_ficha, false)){
                ficha_count++;
            }
        }
        return ficha_count;
    }

    /**
     * Добавить собранную пасхалку
     * @param context Контекст приложения
     * @param name Пасхалка
     */
    public static void put(Context context, String name){
        if (!MySharedPreferences.get(context, name, false)){
            Toast.makeText(context, R.string.go_to_settings, Toast.LENGTH_LONG).show();
        }
        MySharedPreferences.put(context, name, true);
    }

    public void playWindowsFicha(Activity activity) {
        if (new Random().nextInt(5) == 0) {
            put(activity, Ficha_achievements.FICHA_KEYS);
            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
            mp = MediaPlayer.create(activity, R.raw.windows);
            mp.start();
            Toast.makeText(activity, R.string.touch_up_scam, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(activity, R.string.touch_up, Toast.LENGTH_SHORT).show();
        }
    }

    private static final String fichaKey = "UpDownDownUpUpUpUpDown";
    private static final String fichaKeyTwo = "UpDownUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUp";
    private static String fichaKeyBuild = "";
    public void playKeysFicha(Activity activity, int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            fichaKeyBuild += "Down";
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            fichaKeyBuild += "Up";
        }
        if (fichaKey.equals(fichaKeyBuild)) {
            put(activity, Ficha_achievements.FICHA_KEYS);
            mp = MediaPlayer.create(activity, R.raw.vsegohoroshegpo);
            mp.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            activity.finishAffinity();
        }
        if (fichaKeyTwo.equals(fichaKeyBuild)) {
            put(activity, Ficha_achievements.FICHA_KEYS_TWO);
            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
            mp = MediaPlayer.create(activity, R.raw.nani);
            mp.start();
        }
        if (!fichaKey.startsWith(fichaKeyBuild) && !fichaKeyTwo.startsWith(fichaKeyBuild)) {
            fichaKeyBuild = "";
        } else {
            long mills = 300L;
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

            if (vibrator.hasVibrator()) {
                vibrator.vibrate(mills);
            }
        }
    }
}
