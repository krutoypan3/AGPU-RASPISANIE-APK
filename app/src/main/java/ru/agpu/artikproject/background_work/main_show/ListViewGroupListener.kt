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
import ru.agpu.artikproject.background_work.CustomDialog
import ru.agpu.artikproject.background_work.CustomDialogType
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.IS_MAIN_SHOWED
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListed
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListedId
import ru.agpu.artikproject.background_work.datebase.AppData.Groups.groupListedType
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemType
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_MAIN_SHOW
import ru.agpu.artikproject.presentation.layout.fragment.ScheduleShowFragment
import ru.agpu.artikproject.background_work.site_parse.GetRasp

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
        selectedItem = groupListed?.get(position)?.item
        selectedItemType = groupListedType[position]
        selectedItemId = groupListedId[position]
        if (CheckInternetConnection.getState(act.applicationContext)) {
            GetRasp(
                selectedItemId ?: "",
                selectedItemType ?: "",
                selectedItem ?: "",
                weekId,
                act.applicationContext
            ).start()
        }
        IS_MAIN_SHOWED = false
        FRAGMENT = BACK_TO_MAIN_SHOW
        val bottomNavigationView = act.findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigationView.selectedItemId = R.id.details_page_Schedule
        myFragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.fragment_container_view, ScheduleShowFragment::class.java, null)
            ?.commit()
    }

    /**
     * Отслеживает долгое нажатие на элемент списка с аудиториями
     * @param position Позиция зажатого элемента
     * @param act Активити
     */
    private fun longClick(act: Activity, position: Int): Boolean {
        ListViewGroupListener.position = position
        val cdd = CustomDialog(act, CustomDialogType.DELETE_SAVED_GROUP)
        cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
        cdd.show()
        return true
    }
}