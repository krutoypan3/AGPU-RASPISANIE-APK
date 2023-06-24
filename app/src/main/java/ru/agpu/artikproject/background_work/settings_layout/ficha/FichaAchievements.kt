package ru.agpu.artikproject.background_work.settings_layout.ficha

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Vibrator
import android.view.KeyEvent
import android.widget.Toast
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.main_show.FichaRicardo
import java.util.Random

// TODO В идеале класс сделать статикой
class FichaAchievements {
    companion object {
        const val FICHA_GO_TO_HOME = "ficha_go_to_home"
        const val FICHA_WINDOWS = "ficha_windows"
        const val FICHA_KEYS = "ficha_keys"
        const val FICHA_KEYS_TWO = "ficha_keys_two"
        const val FICHA_ADMIN = "ficha_admin"

        private val FICHA_LIST = arrayOf(
            "ficha_setting_logo", "ficha_setting_leonardo",
            "ficha_para_lapshin", "ficha_refresh", "ficha_god",
            "ficha_today", "ficha_building_ico", "ficha_building_main_text", "ficha_ricardo",
            FICHA_GO_TO_HOME, FICHA_WINDOWS, FICHA_KEYS, FICHA_KEYS_TWO, FICHA_ADMIN
        )

        private val FICHA_SECRET_LIST = arrayOf(
            "ficha_para_lapshin"
        )

        private const val fichaKey = "UpDownDownUpUpUpUpDown"
        private const val fichaKeyTwo = "UpDownUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUp"
        private var fichaKeyBuild = ""

        private var mp: MediaPlayer? = null

        val MAX_FICHA_COUNT = FICHA_LIST.size - FICHA_SECRET_LIST.size

        /**
         * Получить количество собранных пасхалок
         * @param context Контекст приложения
         */
        fun get(context: Context?): Int {
            var fichaCount = 0
            for (currentFicha in FICHA_LIST) {
                if (MySharedPreferences.get(context, currentFicha, false)) {
                    fichaCount++
                }
            }
            return fichaCount
        }

        /**
         * Добавить собранную пасхалку
         * @param context Контекст приложения
         * @param name Пасхалка
         */
        fun put(context: Context?, name: String?) {
            if (!MySharedPreferences.get(context, name, false)) {
                Toast.makeText(context, R.string.go_to_settings, Toast.LENGTH_LONG).show()
            }
            MySharedPreferences.put(context, name, true)
        }

        /**
         * Собрать все пасхалки (только для админа)
         * @param context Контекст приложения
         */
        fun putAll(context: Context?) {
            FICHA_LIST.forEach { fichaName ->
                MySharedPreferences.put(context, fichaName, true)
            }
            Toast.makeText(context, R.string.open_all_ficha, Toast.LENGTH_LONG).show()
        }

        fun removeAll(context: Context?) {
            FICHA_LIST.forEach { fichaName ->
                MySharedPreferences.remove(context, fichaName)
            }
            Toast.makeText(context, R.string.clear_all_ficha, Toast.LENGTH_LONG).show()
        }
    }

    fun playWindowsFicha(activity: Activity) {
        if (Random().nextInt(5) == 0) {
            put(activity, FICHA_WINDOWS)
            val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(activity, R.raw.windows)
            mp?.start()
            Toast.makeText(activity, R.string.touch_up_scam, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, R.string.touch_up, Toast.LENGTH_SHORT).show()
        }
    }

    fun playAdminFicha(activity: Activity) {
        put(activity, FICHA_ADMIN)
        FichaRicardo(activity)
    }

    fun playKeysFicha(activity: Activity, keyCode: Int) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            fichaKeyBuild += "Down"
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            fichaKeyBuild += "Up"
        }
        if (fichaKey == fichaKeyBuild) {
            put(activity, FICHA_KEYS)
            mp = MediaPlayer.create(activity, R.raw.vsegohoroshegpo)
            mp?.start()
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            activity.finishAffinity()
        }
        if (fichaKeyTwo == fichaKeyBuild) {
            put(activity, FICHA_KEYS_TWO)
            val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(activity, R.raw.nani)
            mp?.start()
        }
        if (!fichaKey.startsWith(fichaKeyBuild) && !fichaKeyTwo.startsWith(fichaKeyBuild)) {
            fichaKeyBuild = ""
        } else {
            val mills = 300L
            val vibrator = activity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(mills)
            }
        }
    }
}