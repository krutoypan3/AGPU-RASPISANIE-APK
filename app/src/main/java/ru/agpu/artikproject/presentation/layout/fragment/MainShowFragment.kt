package ru.agpu.artikproject.presentation.layout.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.FirstAppStartHelper
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter
import ru.agpu.artikproject.background_work.datebase.AppData
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animScale
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_IF_FIRST_APP_START
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.main_show.ChangeDay
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener
import ru.agpu.artikproject.background_work.main_show.UpdateDateInMainActivity
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.background_work.site_parse.GetGroupListSearch
import ru.agpu.artikproject.databinding.FragmentMainActivityMainShowBinding

/**
 * Главный экран приложения с поиском
 */
class MainShowFragment: Fragment(R.layout.fragment_main_activity_main_show) {
    private var binding: FragmentMainActivityMainShowBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainActivityMainShowBinding.inflate(inflater, container, false)

        // Прослушка нажатий на текущую дату (Ficha Today)
        FichaAchievements().playFichaToday(requireActivity(), binding?.mainActivityText!!)

        // Отслеживание нажатий и зажатий на список групп и аудиторий
        ListViewGroupListener(requireActivity(), binding?.listview!!)

        // Обновляем текущую неделю на главной странице
        UpdateDateInMainActivity(requireActivity()).start()

        // Первичный вывод групп которые были открыты ранее
        binding?.raspSearchEdit?.addTextChangedListener(etListener)
        binding?.raspSearchEdit?.text = Editable.Factory.getInstance().newEditable("")

        // Отслеживание нажатий на смену даты
        binding?.subtitle?.setOnClickListener {
            ChangeDay(requireActivity()).setDate()
            it.startAnimation(animScale)
        }
        if (MySharedPreferences.getPref(context, PREF_IF_FIRST_APP_START, true)) {
            MySharedPreferences.putPref(context, PREF_IF_FIRST_APP_START, false)
            FirstAppStartHelper(requireActivity())
        }

        return binding?.root
    }

    private val etListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {} // До изменения поля
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {} // После изменения поля
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { // Во время изменения поля
            if (s.isNotBlank()) { // Если строка поиска не пустая
                if (!CheckInternetConnection.getState(requireContext())) {
                    WatchSaveGroupRasp()
                } // Если нет доступа к интернету, то выводить список из бд
                else {
                    val urlq = "https://www.it-institut.ru/SearchString/KeySearch?Id=118&SearchProductName=$s"
                    GetGroupListSearch(urlq, requireActivity()).start() // Отправляем запрос на сервер и выводим получившийся список
                }
                if (s.toString().equals("рикардо", ignoreCase = true)) {
                    FichaAchievements().playFichaRicardo(requireActivity())
                }
            } else {
                val sap = WatchSaveGroupRasp()
                if (AppData.Groups.groupListed == null || sap.raspisanie.isEmpty()) {
                    binding?.result?.setText(R.string.no_saved_group)
                } else {
                    val adapter = ListViewAdapter(requireContext(), AppData.Groups.groupListed ?: emptyList())
                    binding?.listview?.adapter = adapter
                    binding?.result?.text = ""
                }
            }
        }
    }
}