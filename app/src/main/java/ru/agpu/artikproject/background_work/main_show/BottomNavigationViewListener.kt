package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.IS_MAIN_SHOWED
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_MAIN_SHOW
import ru.agpu.artikproject.presentation.layout.fragment.MainShowFragment
import ru.agpu.artikproject.presentation.layout.fragment.NoHaveFavoriteScheduleFragment
import ru.agpu.artikproject.presentation.layout.fragment.RecyclerviewShowFragment
import ru.agpu.artikproject.presentation.layout.fragment.RecyclerviewShowFragment.Companion.SELECTED_LIST
import ru.agpu.artikproject.presentation.layout.fragment.ScheduleShowFragment
import ru.agpu.artikproject.presentation.layout.fragment.SelectGroupDirectionFacultyFragment
import ru.agpu.artikproject.presentation.layout.fragment.SettingsShowFragment
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements

class BottomNavigationViewListener(activity: Activity) {
    init {
        val bottomNavigationView: BottomNavigationView = activity.findViewById(R.id.bottom_navigatin_view)

        bottomNavigationView.selectedItemId = R.id.details_page_Home_page
        bottomNavigationView.setOnItemReselectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.details_page_Home_page -> if (IS_MAIN_SHOWED) {
                    FichaAchievements().playFichaGoToHome(activity)
                }
            }
        }
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.details_page_Faculties_list -> myFragmentManager?.beginTransaction()
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.replace(R.id.fragment_container_view, SelectGroupDirectionFacultyFragment::class.java, null)
                    ?.commit()

                R.id.details_page_Home_page -> {
                    IS_MAIN_SHOWED = true
                    myFragmentManager?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.replace(R.id.fragment_container_view, MainShowFragment::class.java, null)
                        ?.commit()
                    return@setOnItemSelectedListener true
                }

                R.id.details_page_Schedule -> { // Если данные не пустые - показываем расписание по ним
                    if (selectedItem != "") {
                        myFragmentManager?.beginTransaction()
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.replace(R.id.fragment_container_view, ScheduleShowFragment::class.java, null)
                            ?.commit()
                    } else { // Иначе - выводим сообщение о том, что тут будет показано расписание
                        myFragmentManager?.beginTransaction()
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.replace(
                                R.id.fragment_container_view,
                                NoHaveFavoriteScheduleFragment::class.java,
                                null
                            )?.commit()
                    }
                }

                R.id.details_page_Audiences -> {
                    SELECTED_LIST = 1
                    myFragmentManager?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.replace(R.id.fragment_container_view, RecyclerviewShowFragment::class.java, null)
                        ?.commit()
                }

                R.id.details_page_Settings -> // Кнопка 'Настройки'
                    myFragmentManager?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.replace(R.id.fragment_container_view, SettingsShowFragment::class.java, null)
                        ?.commit()

                else -> return@setOnItemSelectedListener false
            }
            IS_MAIN_SHOWED = false
            FRAGMENT = BACK_TO_MAIN_SHOW
            true
        }
    }
}