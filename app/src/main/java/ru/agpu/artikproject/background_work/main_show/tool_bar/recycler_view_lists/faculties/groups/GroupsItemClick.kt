package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups

import android.app.Activity
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckInternetConnection
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.agpu.artikproject.presentation.layout.MainActivity
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

        MainActivity.selectedItem = groupsListItems[itemPosition].groupName
        MainActivity.selectedItemType = "Group"
        MainActivity.selectedItemId = groupsListItems[itemPosition].groupId

        if (CheckInternetConnection.getState(act.applicationContext)) {
            GetRasp(
                MainActivity.selectedItemId ?: "",
                MainActivity.selectedItemType ?: "",
                MainActivity.selectedItem ?: "",
                MainActivity.weekId,
                act.applicationContext,
                null
            ).start()
        }

        MainActivity.myFragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.fragment_container_view, FragmentScheduleShow::class.java, null)
            ?.commit()
        MainActivity.IS_MAIN_SHOWED = false
        MainActivity.FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW

        val bottomNavigationView = act.findViewById<BottomNavigationView>(R.id.bottom_navigatin_view)
        bottomNavigationView.selectedItemId = R.id.details_page_Schedule
    }
}