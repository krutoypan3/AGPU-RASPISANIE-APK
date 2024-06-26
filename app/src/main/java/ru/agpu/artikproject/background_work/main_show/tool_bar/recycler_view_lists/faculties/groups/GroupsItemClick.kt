package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.IS_MAIN_SHOWED
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemType
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_MAIN_SHOW
import ru.agpu.artikproject.presentation.layout.fragment.ScheduleShowFragment
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl
import ru.oganesyanartem.core.domain.repository.GroupsListRepository
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetUseCase
import java.util.stream.Collectors

/**
 * Обрабатывает нажатия на список групп
 *
 * @param recyclerView RecyclerView
 * @param itemView     Выбранный элемент (View)
 * @param act          Активити
 */
class GroupsItemClick(recyclerView: RecyclerView, itemView: View, act: Activity) {
    init {
        val itemPosition = recyclerView.getChildLayoutPosition(itemView) // Получаем позицию нажатого элемента

        val groupsListRepository: GroupsListRepository = GroupsListImpl(act.applicationContext)
        var groupsListItems = GroupsListGetUseCase(groupsListRepository).execute()
        groupsListItems = groupsListItems
            .stream()
            .filter { it.facultiesName == RecyclerViewAdapter.selected_faculties_position }
            .collect(Collectors.toList())

        selectedItem = groupsListItems[itemPosition].groupName
        selectedItemType = "Group"
        selectedItemId = groupsListItems[itemPosition].groupId

        if (CheckInternetConnection.getState(act.applicationContext)) {
            GetRasp(
                selectedItemId ?: "",
                selectedItemType ?: "",
                selectedItem ?: "",
                weekId,
                act.applicationContext
            ).start()
        }

        myFragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.fragment_container_view, ScheduleShowFragment::class.java, null)
            ?.commit()
        IS_MAIN_SHOWED = false
        FRAGMENT = BACK_TO_MAIN_SHOW

        val bottomNavigationView = act.findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigationView.selectedItemId = R.id.details_page_Schedule
    }
}