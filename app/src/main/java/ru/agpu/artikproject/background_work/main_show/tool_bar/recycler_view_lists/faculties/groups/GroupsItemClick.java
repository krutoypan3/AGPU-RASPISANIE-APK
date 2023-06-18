package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.stream.Collectors;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl;
import ru.oganesyanartem.core.domain.models.GroupsListItem;
import ru.oganesyanartem.core.domain.repository.GroupsListRepository;
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetUseCase;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class GroupsItemClick {
    /**
     * Обрабатывает нажатия на список групп
     *
     * @param recyclerView RecyclerView
     * @param itemView     Выбранный элемент (View)
     * @param act          Активити
     */
    public GroupsItemClick(RecyclerView recyclerView, View itemView, Activity act) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента

        GroupsListRepository groupsListRepository = new GroupsListImpl(act.getApplicationContext());
        List<GroupsListItem> groupsListItems = new GroupsListGetUseCase(groupsListRepository).execute();
        groupsListItems = groupsListItems.stream()
                .filter(a -> a.getFacultiesName()
                        .equals(RecyclerViewAdapter.selected_faculties_position))
                .collect(Collectors.toList());

        MainActivity.selectedItem = groupsListItems.get(itemPosition).getGroupName();
        MainActivity.selectedItem_type = "Group";
        MainActivity.selectedItem_id = groupsListItems.get(itemPosition).getGroupId();

        if (CheckInternetConnection.getState(act.getApplicationContext())) {
            new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, act.getApplicationContext(), null).start();
        }

        MainActivity.fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, FragmentScheduleShow.class, null).commit();
        MainActivity.IS_MAIN_SHOWED = false;
        MainActivity.FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW;
        BottomNavigationView bottomNavigationView = act.findViewById(R.id.bottom_navigatin_view);
        bottomNavigationView.setSelectedItemId(R.id.details_page_Schedule);
    }
}
