package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.bumptech.glide.Glide
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomAlertDialog
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements.Companion.put
import java.io.IOException

class FichaRicardo(act: Activity) {
    var mediaPlayer = MediaPlayer()

    init {
        put(act.applicationContext, "ficha_ricardo")
        val cdd = CustomAlertDialog(act, "ricardo_pasha")
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
        Glide.with(act)
            .asGif()
            .load("https://i.ibb.co/JHf4whr/ricardo-ricardo-milos.gif")
            .into(cdd.para_info_photo)
        val audioManager = act.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
        val url = "https://ruo.morsmusic.org/load/1602697468/Halogen_-_U_Got_That_(musmore.com).mp3" // your URL here
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare() // might take long! (for buffering, etc)
            mediaPlayer.start()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }
}