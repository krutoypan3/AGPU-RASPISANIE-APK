package ru.agpu.artikproject.background_work.main_show;

import android.app.Activity;
import android.widget.ListView;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.layout.MainActivity;

public class ListViewGroupListener {
    public static int position;
    /**
     * Слушатель нажатия на список групп
     * @param listView Главный список с группами и аудиториями
     * @param act Активити
     */
    public ListViewGroupListener(Activity act, ListView listView){
        // Отслеживание нажатий на элемент в списке(группа\ауд\препод)
        listView.setOnItemClickListener((parent, v, position, id) ->
                short_click(act, position));
        listView.setOnItemLongClickListener((parent, view, position, id) -> long_click(act, position));
    }
    /**
     * Слушатель нажатия на список групп
     * @param position Позиция выбранного элелемента
     * @param act Активити
     */
    private void short_click(Activity act, int position){
        MainActivity.selectedItem = MainActivity.group_listed.get(position).item;
        MainActivity.selectedItem_type = MainActivity.group_listed_type[position];
        MainActivity.selectedItem_id = MainActivity.group_listed_id[position];
        if (CheckInternetConnection.getState(act.getApplicationContext())){
            new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, act.getApplicationContext()).start();
        }
        MainActivity.IS_MAIN_SHOWED = false;
        MainActivity.FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW;
        BottomNavigationView bottomNavigationView = act.findViewById(R.id.bottom_navigatin_view);
        bottomNavigationView.setSelectedItemId(R.id.details_page_Schedule);
        MainActivity.fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, FragmentScheduleShow.class, null).commit();
    }
    /**
     * Отслеживает долгое нажатие на элемент списка с аудиториями
     * @param position Позиция зажатого элемента
     * @param act Активити
     */
    @SuppressWarnings("SameReturnValue")
    private boolean long_click(Activity act, int position){
        ListViewGroupListener.position = position;
        CustomAlertDialog cdd = new CustomAlertDialog(act,"delete_one_saved_group");
        cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        cdd.show();
        return true;
    }
}
