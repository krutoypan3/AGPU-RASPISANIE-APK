package ru.agpu.artikproject.background_work.settings_layout.ficha

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Vibrator
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CustomDialog
import ru.agpu.artikproject.background_work.CustomDialogType
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.io.IOException
import java.util.Random

// TODO В идеале класс сделать статикой
class FichaAchievements {
    companion object {

        private val FICHA_LIST = Ficha.values()

        private const val fichaKey = "UpDownDownUpUpUpUpDown"
        private const val fichaKeyTwo = "UpDownUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUpUp"
        private var fichaKeyBuild = ""

        var mp: MediaPlayer? = null

        val MAX_FICHA_COUNT = Ficha.filterByLevel(FichaLevel.PUBLIC).size

        /**
         * Получить количество собранных пасхалок
         * @param context Контекст приложения
         */
        fun getFicha(context: Context?): Int {
            var fichaCount = 0
            for (currentFicha in FICHA_LIST) {
                if (MySharedPreferences.getPref(context, currentFicha.prefName, false)) {
                    fichaCount++
                }
            }
            return fichaCount
        }

        /**
         * Добавить собранную пасхалку
         * @param context Контекст приложения
         * @param ficha Пасхалка
         */
        fun putFicha(context: Context?, ficha: Ficha) {
            if (!MySharedPreferences.getPref(context, ficha.prefName, false)) {
                Toast.makeText(context, R.string.go_to_settings, Toast.LENGTH_LONG).show()
            }
            MySharedPreferences.putPref(context, ficha.prefName, true)
        }

        /**
         * Собрать все пасхалки (только для админа)
         * @param context Контекст приложения
         */
        fun putAllFicha(context: Context?) {
            FICHA_LIST.forEach { ficha ->
                MySharedPreferences.putPref(context, ficha.prefName, true)
            }
            Toast.makeText(context, R.string.open_all_ficha, Toast.LENGTH_LONG).show()
        }

        fun removeAllFicha(context: Context?) {
            FICHA_LIST.forEach { ficha ->
                MySharedPreferences.removePref(context, ficha.prefName)
            }
            Toast.makeText(context, R.string.clear_all_ficha, Toast.LENGTH_LONG).show()
        }
    }

    fun playWindowsFicha(activity: Activity) {
        if (Random().nextInt(5) == 0) {
            putFicha(activity, Ficha.FICHA_WINDOWS)
            val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(activity, R.raw.windows)
            mp?.start()
            Toast.makeText(activity, R.string.touch_up_scam, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, R.string.touch_up, Toast.LENGTH_SHORT).show()
        }
    }

    fun playKeysFicha(activity: Activity, keyCode: Int) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            fichaKeyBuild += "Down"
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            fichaKeyBuild += "Up"
        } else {
            return
        }
        if (fichaKey == fichaKeyBuild) {
            putFicha(activity, Ficha.FICHA_KEYS)
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
            putFicha(activity, Ficha.FICHA_KEYS_TWO)
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

    /**
     * Слушатель нажатий на фон в настройках (лого или леонардо)
     * @param act Активити
     */
    @SuppressLint("ClickableViewAccessibility")
    fun playSettingsLogoFicha(act: Activity){
        val updateLayout = act.findViewById<RelativeLayout>(R.id.update_layout) // Находим сам фон
        val animImage = act.findViewById<ImageView>(R.id.animImage) // Находим изначально невидимое пустое изображение
        updateLayout.setOnTouchListener { _: View?, event: MotionEvent ->  // При нажатии на фон
            animImage.x = event.x - 100 // Определяем позицию нажатия по X
            animImage.y = event.y - 100 // Определяем позицию нажатия по Y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    putFicha(act.applicationContext, Ficha.FICHA_SETTING_LOGO) // Засчитываем фичу с плавающим логотипом
                    FichaShow(act) // Обновляем список найденных фич, т.к. находимся на слое настроек
                    animImage.visibility = View.VISIBLE // Делаем невидимую картинку видимой
                    if (Random().nextInt(20) == 0) { // С шансом 1 к 20 устанавливаем другое фото вместо логотипа
                        animImage.setImageResource(R.drawable.ficha_leonardo) // Ставим фото Леонардо
                        putFicha(act.applicationContext, Ficha.FICHA_SETTING_LEONARDO) // Засчитываем фичу
                    }
                }
                MotionEvent.ACTION_UP -> {
                    animImage.visibility = View.INVISIBLE // Скрываем плавающий логотип
                    animImage.setImageResource(R.drawable.agpu_ico) // И возвращаем его (если была активирована фича с Леонардо)
                }
            }
            true
        }
    }

    fun playFichaAdmin(act: Activity) {
        putFicha(act, Ficha.FICHA_ADMIN)
        val cdd = CustomDialog(act, CustomDialogType.RICARDO_PASHA)
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
        cdd.paraInfoPhotoIV.let {
        Glide.with(act)
            .asGif()
            .load("https://i.ibb.co/5hTJ4fQ/image.gif")
            .into(it!!)
        }
        val audioManager = act.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
        val url = "https://ruo.morsmusic.org/load/185629960/INSTASAMKA_-_LIPSI_HA_(musmore.com).mp3" // your URL here
        try {
            mp = MediaPlayer()
            mp?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mp?.setDataSource(url)
            mp?.prepare()
            mp?.start()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    fun playFichaRicardo(act: Activity) {
        putFicha(act.applicationContext, Ficha.FICHA_RICARDO)
        val cdd = CustomDialog(act, CustomDialogType.RICARDO_PASHA)
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
        cdd.paraInfoPhotoIV.let {
        Glide.with(act)
            .asGif()
            .load("https://i.ibb.co/JHf4whr/ricardo-ricardo-milos.gif")
            .into(it!!)
        }
        val audioManager = act.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
        val url = "https://ruo.morsmusic.org/load/1602697468/Halogen_-_U_Got_That_(musmore.com).mp3" // your URL here
        try {
            mp = MediaPlayer()
            mp?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mp?.setDataSource(url)
            mp?.prepare() // might take long! (for buffering, etc)
            mp?.start()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
    }

    /**
     * Фича отвечающая за прослушку нажатий на текущую дату
     * @param act Активити
     * @param today TextView
     */
    fun playFichaToday(act: Activity, today: TextView) {
        today.setOnClickListener {
            today.startAnimation(MainActivity.animScale)
            if (Random().nextInt(30) == 0) {
                putFicha(act.applicationContext, Ficha.FICHA_TODAY)
                val audioManager = act.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
                mp = MediaPlayer.create(act, R.raw.povezlo_povezlo)
                mp?.start()
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://i.ibb.co/9b73CLy/2022-04-27-183814.png")
                )
                act.startActivity(intent)
            }
        }
    }

    fun playFichaGod(act: Activity, shape: GradientDrawable) {
        putFicha(act.applicationContext, Ficha.FICHA_GOD)
        val cdd2 = CustomDialog(act, CustomDialogType.PARA_PASHA)
        cdd2.show()
        cdd2.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        cdd2.findViewById<View>(R.id.scrollViewCustom).background = shape
        val molitva = ArrayList<ListViewItems>()
        molitva.add(ListViewItems("Да восвятится имя твое"))
        molitva.add(ListViewItems("Да покаешься ты в грехах своих"))
        molitva.add(ListViewItems("Да закроешь ты сессию эту"))
        val adapter2 = ListViewAdapter(act.applicationContext, molitva)
        cdd2.listViewLV?.adapter = adapter2
        cdd2.paraInfoPhotoIV.let {
        Glide.with(act).load("https://i.ibb.co/4pqtKcY/ficha-god.png")
            .into(it!!)
        }
        val audioManager = act.applicationContext
            .getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
        mp = MediaPlayer.create(act, R.raw.ficha_god)
        mp?.start()
    }

    fun playFichaLapshin(act: Activity) {
        if (Random().nextInt(5) == 0) { putFicha(act.applicationContext, Ficha.FICHA_PARA_LAPSHIN)
            val audioManager: AudioManager = act.applicationContext
                .getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(act, R.raw.povezlo_povezlo)
            mp?.start()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/jLRpZ1B/2022-04-29-203438.png"))
            act.startActivity(intent)
        }
    }

    fun playFichaRefresh(context: Context, layout: RelativeLayout) {
        when (Random().nextInt(4)) {
            0 -> {
                layout.startAnimation(MainActivity.animRotate_ok)
                putFicha(context, Ficha.FICHA_REFRESH)
            }
            1 -> layout.startAnimation(MainActivity.animScale)
            2 -> layout.startAnimation(MainActivity.animUehalVl)
            3 -> layout.startAnimation(MainActivity.animUehalVp)
        }
    }

    fun playFichaBuildingIco(context: Context) {
        val randomInt = Random().nextInt(10)
        if (randomInt == 0) {
            putFicha(context, Ficha.FICHA_BUILDING_ICO)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(context, R.raw.winx)
            mp?.start()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/rZvW4Mj/yao-min-65474631-orig.jpg"))
            context.startActivity(intent)
        }
    }

    fun playFichaBuildingMainText(context: Context) {
        val randomInt = Random().nextInt(30)
        if (randomInt == 0) {
            putFicha(context, Ficha.FICHA_BUILDING_MAIN_TEXT)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(context, R.raw.amogus)
            mp?.start()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/HFbRBrx/3.jpg"))
            context.startActivity(intent)
        }
    }

    fun playFichaGoToHome(context: Context) {
        if (Random().nextInt(30) == 0) {
            putFicha(context, Ficha.FICHA_GO_TO_HOME)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
            mp = MediaPlayer.create(context, R.raw.luntik_i_kloun)
            mp?.start()
        }
    }
}