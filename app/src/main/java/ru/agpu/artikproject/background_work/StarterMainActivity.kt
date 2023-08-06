package ru.agpu.artikproject.background_work

import android.app.Activity
import android.content.Intent
import android.view.animation.AnimationUtils
import android.widget.ImageView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.util.concurrent.TimeUnit

/**
 * Показывает список всех факультетов и их групп
 * @param act Активити
 * @param loadingIco ImageView
 */
class StarterMainActivity(val act: Activity, private val loadingIco: ImageView) : Thread() {
    override fun run() {
        try {
            val animRotate = AnimationUtils.loadAnimation(act.applicationContext, R.anim.scale_long)
            loadingIco.startAnimation(animRotate)
            try {
                TimeUnit.MILLISECONDS.sleep(800)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            val intent = Intent(act.applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            act.startActivity(intent)
            act.finishAffinity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

