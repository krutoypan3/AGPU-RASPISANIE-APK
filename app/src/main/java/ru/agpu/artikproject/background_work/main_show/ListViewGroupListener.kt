package ru.agpu.artikproject.background_work.main_show

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ListView
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.CustomAlertDialog
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.agpu.artikproject.presentation.layout.MainActivity

/**
 * Слушатель нажатия на список групп
 * @param listView Главный список с группами и аудиториями
 * @param act Активити
 */
class ListViewGroupListener(act: Activity, listView: ListView) {
    companion object {
        var position = 0
    }
    
    init {
        // Отслеживание нажатий на элемент в списке(группа\ауд\препод)
        listView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                shortClick(act, position)
            }
        listView.onItemLongClickListener =
            OnItemLongClickListener { _: AdapterView<*>?, _: View?, position: Int, _: Long ->
                longClick(act, position)
            }
    }

    /**
     * Слушатель нажатия на список групп
     * @param position Позиция выбранного элелемента
     * @param act Активити
     */
    private fun shortClick(act: Activity, position: Int) {
        MainActivity.selectedItem = MainActivity.group_listed[position].item
        MainActivity.selectedItem_type = MainActivity.group_listed_type[position]
        MainActivity.selectedItem_id = MainActivity.group_listed_id[position]
        if (CheckInternetConnection.getState(act.applicationContext)) {
            GetRasp(
                MainActivity.selectedItem_id,
                MainActivity.selectedItem_type,
                MainActivity.selectedItem,
                MainActivity.week_id,
                act.applicationContext,
                null
            ).start()
        }
        MainActivity.IS_MAIN_SHOWED = false
        MainActivity.FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW
        val bottomNavigationView = act.findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigationView.selectedItemId = R.id.details_page_Schedule
        MainActivity.fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment_container_view, FragmentScheduleShow::class.java, null)
            .commit()
    }

    /**
     * Отслеживает долгое нажатие на элемент списка с аудиториями
     * @param position Позиция зажатого элемента
     * @param act Активити
     */
    private fun longClick(act: Activity, position: Int): Boolean {
        ListViewGroupListener.position = position
        val cdd = CustomAlertDialog(act, "delete_one_saved_group")
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
        return true
    }
}