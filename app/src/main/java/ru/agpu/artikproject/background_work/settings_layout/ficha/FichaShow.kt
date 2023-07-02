package ru.agpu.artikproject.background_work.settings_layout.ficha

import android.app.Activity
import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import ru.agpu.artikproject.R

/**
 * Класс отвечающий за вывод информации о пасхалках на экран настроек
 * @param act Активити
 */
class FichaShow(act: Activity) {
    var mp: MediaPlayer? = null

    init {
        val fichaCountTextTV = act.findViewById<TextView>(R.id.ficha_count_text)
        val fichaCountTV = act.findViewById<TextView>(R.id.ficha_count)
        val fichaNyanIV = act.findViewById<ImageView>(R.id.ficha_nyan)
        val fichaCount = FichaAchievements.getFicha(act.applicationContext)
        val fichaProgress = "$fichaCount / ${FichaAchievements.MAX_FICHA_COUNT}"
        if (fichaCount > 0) { // Если найдена хоть одна фича, делаем информацию о фичах видимой
            fichaCountTextTV.visibility = View.VISIBLE // Основной текст
            fichaCountTV.visibility = View.VISIBLE // Количество фич
            fichaCountTV.text = fichaProgress
        }
        if (fichaCount >= FichaAchievements.MAX_FICHA_COUNT) { // Если собранны все фичи
            val newText = "Пасхалки?.Ты собрал их все: $fichaProgress"
            fichaCountTextTV.text = newText
            if (mp == null)
                mp = MediaPlayer.create(act.applicationContext, R.raw.nyan_cat)
            else if (!mp!!.isPlaying) {
                mp = MediaPlayer.create(act.applicationContext, R.raw.nyan_cat)
                mp!!.start()
            }
            fichaNyanIV.visibility = View.VISIBLE
            Glide.with(act.applicationContext)
                .load("https://www.nyan.cat/cats/original.gif")
                .into(DrawableImageViewTarget(fichaNyanIV))
        }
        if (fichaCount > FichaAchievements.MAX_FICHA_COUNT) { // Если собраны все фичи + хоть одна секретка
            val newText = "Ого, да ты собрал все пасхалки и даже больше: $fichaProgress"
            fichaCountTextTV.text = newText
        }
    }
}