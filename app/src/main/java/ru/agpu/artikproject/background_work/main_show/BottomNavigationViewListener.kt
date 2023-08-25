package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.view.MenuItem
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentMainShow
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentNoHaveFavoriteSchedule
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow.Companion.SELECTED_LIST
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSelectGroupDirectionFaculty
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSettingsShow
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.presentation.layout.MainActivity

class BottomNavigationViewListener(activity: Activity) {
    init {
        val bottomNavigationView: BottomNavigationView = activity.findViewById(R.id.bottom_navigatin_view)

        bottomNavigationView.selectedItemId = R.id.details_page_Home_page
        bottomNavigationView.setOnItemReselectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.details_page_Home_page -> if (MainActivity.IS_MAIN_SHOWED) {
                    FichaAchievements().playFichaGoToHome(activity)
                }
            }
        }
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.details_page_Faculties_list -> MainActivity.myFragmentManager?.beginTransaction()
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.replace(R.id.fragment_container_view, FragmentSelectGroupDirectionFaculty::class.java, null)
                    ?.commit()

                R.id.details_page_Home_page -> {
                    MainActivity.IS_MAIN_SHOWED = true
                    MainActivity.myFragmentManager?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.replace(R.id.fragment_container_view, FragmentMainShow::class.java, null)
                        ?.commit()
                    return@setOnItemSelectedListener true
                }

                R.id.details_page_Schedule -> { // Если данные не пустые - показываем расписание по ним
                    if (MainActivity.selectedItem != "") {
                        MainActivity.myFragmentManager?.beginTransaction()
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.replace(R.id.fragment_container_view, FragmentScheduleShow::class.java, null)
                            ?.commit()
                    } else { // Иначе - выводим сообщение о том, что тут будет показано расписание
                        MainActivity.myFragmentManager?.beginTransaction()
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.replace(
                                R.id.fragment_container_view,
                                FragmentNoHaveFavoriteSchedule::class.java,
                                null
                            )?.commit()
                    }
                }

                R.id.details_page_Audiences -> {
                    SELECTED_LIST = 1
                    MainActivity.myFragmentManager?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.replace(R.id.fragment_container_view, FragmentRecyclerviewShow::class.java, null)
                        ?.commit()
                }

                R.id.details_page_Settings -> // Кнопка 'Настройки'
                    MainActivity.myFragmentManager?.beginTransaction()
                        ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        ?.replace(R.id.fragment_container_view, FragmentSettingsShow::class.java, null)
                        ?.commit()

                else -> return@setOnItemSelectedListener false
            }
            MainActivity.IS_MAIN_SHOWED = false
            MainActivity.FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW
            true
        }
    }
}