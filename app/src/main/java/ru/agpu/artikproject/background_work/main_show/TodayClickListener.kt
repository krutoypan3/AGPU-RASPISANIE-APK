package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.widget.TextView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements.Companion.put
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.util.Random

/**
 * Фича отвечающая за прослушку нажатий на текущую дату
 * @param act Активити
 * @param today TextView
 */
class TodayClickListener(act: Activity, today: TextView) {
    init {
        today.setOnClickListener {
            today.startAnimation(MainActivity.animScale)
            if (Random().nextInt(30) == 0) {
                put(act.applicationContext, "ficha_today")
                val audioManager = act.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
                val mp: MediaPlayer = MediaPlayer.create(act, R.raw.povezlo_povezlo)
                mp.start()
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://i.ibb.co/9b73CLy/2022-04-27-183814.png")
                )
                act.startActivity(intent)
            }
        }
    }
}