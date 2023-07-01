package ru.agpu.artikproject.background_work.main_show.fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.FirstAppStartHelper
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.main_show.ChangeDay
import ru.agpu.artikproject.background_work.main_show.EditTextRaspSearchListener
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener
import ru.agpu.artikproject.background_work.main_show.UpdateDateInMainActivity
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.presentation.layout.MainActivity

class FragmentMainShow: Fragment(R.layout.fragment_main_activity_main_show) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = view.context as Activity

        // Прослушка нажатий на текущую дату (Ficha Today)
        FichaAchievements().playFichaToday(activity, view.findViewById(R.id.main_activity_text))

        // Отслеживание нажатий и зажатий на список групп и аудиторий
        ListViewGroupListener(activity, view.findViewById(R.id.listview))

        // Обновляем текущую неделю на главной странице
        UpdateDateInMainActivity(activity).start()

        // Отслеживание изменений текстового поля
        EditTextRaspSearchListener(activity, view.findViewById(R.id.rasp_search_edit))

        // Первичный вывод групп которые были открыты ранее
        WatchSaveGroupRasp(activity)

        // Отслеживание нажатий на смену даты
        view.findViewById<View>(R.id.subtitle).setOnClickListener {
            ChangeDay(activity).setDate()
            it.startAnimation(MainActivity.animScale)
        }
        if (MySharedPreferences.getPref(context, "IsFirstAppStart", true)) {
            MySharedPreferences.putPref(context, "IsFirstAppStart", false)
            FirstAppStartHelper(view.context as Activity)
        }
    }
}