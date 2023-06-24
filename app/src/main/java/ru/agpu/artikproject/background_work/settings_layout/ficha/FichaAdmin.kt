package ru.agpu.artikproject.background_work.settings_layout.ficha

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import com.bumptech.glide.Glide
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomAlertDialog
import java.io.IOException

class FichaAdmin(act: Activity) {
    var mediaPlayer = MediaPlayer()
    init {
        FichaAchievements.put(act.applicationContext, "ficha_ricardo")
        val cdd = CustomAlertDialog(act, "ricardo_pasha")
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
        Glide.with(act)
            .asGif()
            .load("https://i.ibb.co/5hTJ4fQ/image.gif")
            .into(cdd.para_info_photo)
        val audioManager = act.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
        val url = "https://ruo.morsmusic.org/load/185629960/INSTASAMKA_-_LIPSI_HA_(musmore.com).mp3" // your URL here
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