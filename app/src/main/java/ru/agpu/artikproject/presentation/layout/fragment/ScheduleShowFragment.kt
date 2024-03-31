package ru.agpu.artikproject.presentation.layout.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.OnSwipeTouchListener
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animRotate
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animRotate_ok
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemType
import ru.agpu.artikproject.background_work.datebase.Const
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM_ID
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM_TYPE
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.rasp_show.RaspUpdateCheckBoxListener
import ru.agpu.artikproject.background_work.rasp_show.RefreshRaspWeekOrDayStarter
import ru.agpu.artikproject.background_work.rasp_show.SwipeRasp
import ru.agpu.artikproject.background_work.rasp_show.WeekDayChange
import ru.agpu.artikproject.background_work.rasp_show.WeekShowResize
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.databinding.FragmentMainActivityScheduleShowBinding

/**
 * Фрагмент на котором отображается основное расписание
 */
class ScheduleShowFragment: Fragment(R.layout.fragment_main_activity_schedule_show) {
    private var binding: FragmentMainActivityScheduleShowBinding? = null

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainActivityScheduleShowBinding.inflate(inflater, container, false)

        // Сохраняем последнее открытое расписание
        MySharedPreferences.putPref(context, PREF_SELECTED_ITEM, selectedItem)
        MySharedPreferences.putPref(context, PREF_SELECTED_ITEM_TYPE, selectedItemType)
        MySharedPreferences.putPref(context, PREF_SELECTED_ITEM_ID, selectedItemId)

        RefreshRaspWeekOrDayStarter(binding?.root!!).start() // Обновляем расписание

        // Кнопка увеличивающая размер текста в режиме недели
        binding?.weekDayChangeBtnSizeUp?.setOnClickListener { WeekShowResize().sizeAdd() }
        // Кнопка уменьшающая размер текста в режиме недели
        binding?.weekDayChangeBtnSizeDown?.setOnClickListener { WeekShowResize().sizeDec() }

        binding?.refreshBtn?.startAnimation(animRotate)
        binding?.refreshBtn?.setBackgroundResource(R.drawable.refresh_1)

        // Первичный вывод расписания
        SwipeRasp(Const.SwipeDirections.BOTTOM, binding?.root!!)

        // Функция перехода на сайт с расписанием при нажатии на кнопку
        binding?.raspSite?.setOnClickListener {
            binding?.raspSite?.animation = animRotate_ok
            startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?" +
                            "OwnerId=118&SearchId=" + selectedItemId + "&" +
                            "SearchString=" + selectedItem + "&" +
                            "Type=" + selectedItemType + "&" +
                            "WeekId=" + weekId
                ))
            )
        }

        // Переход к предыдущему дню
        binding?.weekDayBt1?.setOnClickListener { SwipeRasp(Const.SwipeDirections.LEFT, binding?.root!!) }
        // Переход к следующему дню
        binding?.weekDayBt2?.setOnClickListener { SwipeRasp(Const.SwipeDirections.RIGHT, binding?.root!!) }
        // Обновить расписание
        binding?.refreshBtn?.setOnClickListener { SwipeRasp(Const.SwipeDirections.BOTTOM, binding?.root!!) }

        binding?.refreshBtnAll?.setOnClickListener {
            FichaAchievements().playFichaRefresh(requireContext(), binding?.raspisanieShow!!)
        }

        // Смена недельного режима и дневного
        binding?.weekDayChangeBtn?.setOnClickListener { WeekDayChange(binding?.root!!) }

        // Отслеживание жестов по расписанию
        binding?.dayParaViewRec
            ?.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
                override fun onSwipeRight() { SwipeRasp(Const.SwipeDirections.LEFT, binding?.root!!) }
                override fun onSwipeLeft() { SwipeRasp(Const.SwipeDirections.RIGHT, binding?.root!!) }
            })


        // Отслеживание жестов под дневным расписанием
        binding?.raspisanieDay?.setOnTouchListener(
        object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeRight() { SwipeRasp(Const.SwipeDirections.LEFT, binding?.root!!) }
            override fun onSwipeLeft() { SwipeRasp(Const.SwipeDirections.RIGHT, binding?.root!!) }
            override fun onSwipeBottom() { SwipeRasp(Const.SwipeDirections.BOTTOM, binding?.root!!) }
        })

        return binding?.root
    }
}