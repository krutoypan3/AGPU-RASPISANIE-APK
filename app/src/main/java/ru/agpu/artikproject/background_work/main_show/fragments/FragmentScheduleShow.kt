package ru.agpu.artikproject.background_work.main_show.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.OnSwipeTouchListener
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.rasp_show.RaspUpdateCheckBoxListener
import ru.agpu.artikproject.background_work.rasp_show.RefreshRaspWeekOrDayStarter
import ru.agpu.artikproject.background_work.rasp_show.SwipeRasp
import ru.agpu.artikproject.background_work.rasp_show.WeekDayChange
import ru.agpu.artikproject.background_work.rasp_show.WeekShowResize
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.presentation.layout.MainActivity
import java.util.Random

class FragmentScheduleShow: Fragment(R.layout.fragment_main_activity_schedule_show) {
    companion object {
        var refresh_on_off = false
        var week_day_on_off = false
        var refresh_successful = true
    }

    override fun onResume() {
        super.onResume()
        RaspUpdateCheckBoxListener(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Сохраняем последнее открытое расписание
        MySharedPreferences.putPref(view.context, "selectedItem", MainActivity.selectedItem)
        MySharedPreferences.putPref(view.context, "selectedItem_type", MainActivity.selectedItem_type)
        MySharedPreferences.putPref(view.context, "selectedItem_id", MainActivity.selectedItem_id)

        val weekDayBt1 = view.findViewById<Button>(R.id.week_day_bt1) // Кнопка перехода к предыдущему дню
        val weekDayBt2 = view.findViewById<Button>(R.id.week_day_bt2) // Кнопка перехода к следующему дню
        val weekDayChangeBtn = view.findViewById<ImageView>(R.id.week_day_change_btn) // Кнопка смены режима просмотра расписания [День / Неделя]
        val refreshBtnFicha = view.findViewById<ImageView>(R.id.refresh_btn_all) // Кнопка перекручивания расп (фича)
        val refreshBtn = view.findViewById<ImageView>(R.id.refresh_btn) // Кнопка обновления расписания
        val weekDayChangeBtnSizeUp = view.findViewById<ImageView>(R.id.week_day_change_btn_size_up) // Кнопка увеличения размера текста в недельном режиме
        val weekDayChangeBtnSizeDown = view.findViewById<ImageView>(R.id.week_day_change_btn_size_down) // Кнопка уменьшения размера текста в недельном режиме
        val gestureLayout = view.findViewById<ConstraintLayout>(R.id.raspisanie_day) // Слой для отслеживания жестов
        val raspisanieShowLayout = view.findViewById<RelativeLayout>(R.id.raspisanie_show) // Основной слой

        RefreshRaspWeekOrDayStarter(view).start() // Обновляем расписание

        // Кнопка увеличивающая размер текста в режиме недели
        weekDayChangeBtnSizeUp.setOnClickListener { WeekShowResize().sizeAdd() }
        // Кнопка уменьшающая размер текста в режиме недели
        weekDayChangeBtnSizeDown.setOnClickListener { WeekShowResize().sizeDec() }

        refreshBtn.startAnimation(MainActivity.animRotate)
        refreshBtn.setBackgroundResource(R.drawable.refresh_1)

        // Первичный вывод расписания
        SwipeRasp("Bottom", view)

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        val raspSite = view.findViewById<ImageView>(R.id.rasp_site)
        raspSite.setOnClickListener {
            raspSite.animation = MainActivity.animRotate_ok
            startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?" +
                            "OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&" +
                            "SearchString=" + MainActivity.selectedItem + "&" +
                            "Type=" + MainActivity.selectedItem_type + "&" +
                            "WeekId=" + MainActivity.week_id
                ))
            )
        }

        // Переход к предыдущему дню
        weekDayBt1.setOnClickListener { SwipeRasp("Left", view) }
        // Переход к следующему дню
        weekDayBt2.setOnClickListener { SwipeRasp("Right", view) }
        // Обновить расписание
        refreshBtn.setOnClickListener { SwipeRasp("Bottom", view) }

        refreshBtnFicha.setOnClickListener {
            FichaAchievements().playFichaRefresh(view.context, raspisanieShowLayout)
        }

        // Смена недельного режима и дневного
        weekDayChangeBtn.setOnClickListener { WeekDayChange(view) }

        // Отслеживание жестов по расписанию
        view.findViewById<View>(R.id.day_para_view_rec)
            .setOnTouchListener(object : OnSwipeTouchListener(view.context) {
                override fun onSwipeRight() { SwipeRasp("Left", view) }
                override fun onSwipeLeft() { SwipeRasp("Right", view) }
            })


        // Отслеживание жестов под дневным расписанием
        gestureLayout.setOnTouchListener(object : OnSwipeTouchListener(view.context) {
            override fun onSwipeRight() { SwipeRasp("Left", view) }
            override fun onSwipeLeft() { SwipeRasp("Right", view) }
            override fun onSwipeBottom() { SwipeRasp("Bottom", view) }
        })
    }
}