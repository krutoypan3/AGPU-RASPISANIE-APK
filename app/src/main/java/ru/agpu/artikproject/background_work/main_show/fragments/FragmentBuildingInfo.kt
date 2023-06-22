package ru.agpu.artikproject.background_work.main_show.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.OnSwipeTouchListener
import ru.agpu.artikproject.background_work.debug.Device_info
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.ShowBuildingsOnTheMap
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements
import ru.agpu.artikproject.background_work.theme.CustomBackground
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.util.Random

class FragmentBuildingInfo : Fragment(R.layout.fragment_main_activity_building_info) {

    companion object {
        var itemPosition = 0
        var mainText: String? = null
        var subText: String? = null
        var pictureByteArrayOutputArray: ByteArray = ByteArray(0)
        var pictureUrl: String? = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка нового фона и затемнителя | Должно быть после setContentView
        val layout = view.findViewById<RelativeLayout>(R.id.building_info_layout)
        layout.background = CustomBackground.getBackground(view.context)
        view.findViewById<View>(R.id.cardBackgroundDarker)
            .setBackgroundColor(CustomBackground.getBackgroundDarker(view.context))

        // Настраиваем основной текст
        val mainTextView = view.findViewById<TextView>(R.id.cardViewAudMainText_second) // Находим TextEdit на слое
        mainTextView.text = mainText // Устанавливаем наш mainText

        // Настраиваем дополнительный текст
        val subTextView = view.findViewById<TextView>(R.id.cardViewAudSubText_second) // Находим TextEdit на слое
        subTextView.text = subText // Устанавливаем наш subText

        // Настраиваем картинку
        val bmp = BitmapFactory.decodeByteArray(pictureByteArrayOutputArray, 0, pictureByteArrayOutputArray.size)
        val bitmapDrawable: Drawable = BitmapDrawable(resources, bmp)
        val imageView = view.findViewById<ImageView>(R.id.cardViewAudImage_second) // Находим нашу вьюшку (ImageView) на слое
        Glide.with(view.context).load(pictureUrl)
            .apply(RequestOptions().override(Device_info.getDeviceWidth(view.context), 960))
            .dontTransform()
            .placeholder(bitmapDrawable)
            .into(imageView)


        // Обработка кнопки перехода на карту
        view.findViewById<View>(R.id.btn_building_info).setOnClickListener { // И при нажатии на кнопку
            ShowBuildingsOnTheMap(itemPosition, view.context as Activity)
        } // Открываем карты

        // Отслеживание нажатий на иконку университета в тулбаре (фича)
        mainTextView.setOnClickListener {
            mainTextView.startAnimation(MainActivity.animScale)
            val randomInt = Random().nextInt(30)
            if (randomInt == 0) {
                Ficha_achievements.put(view.context, "ficha_building_main_text")
                val audioManager = view.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
                val mp = MediaPlayer.create(view.context, R.raw.amogus)
                mp.start()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/HFbRBrx/3.jpg"))
                startActivity(intent)
            }
        }
        imageView.setOnTouchListener(object : OnSwipeTouchListener(view.context) {
            override fun onSwipeLeft() {
                imageView.startAnimation(MainActivity.animUehalVl)
                val randomInt = Random().nextInt(10)
                if (randomInt == 0) {
                    Ficha_achievements.put(view.context, "ficha_building_ico")
                    val audioManager = view.context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0)
                    val mp = MediaPlayer.create(view.context, R.raw.winx)
                    mp.start()
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/rZvW4Mj/yao-min-65474631-orig.jpg"))
                    startActivity(intent)
                }
            }
        })
    }
}