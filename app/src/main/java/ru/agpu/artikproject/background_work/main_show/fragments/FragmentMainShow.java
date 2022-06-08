package ru.agpu.artikproject.background_work.main_show.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.main_show.ChangeDay;
import ru.agpu.artikproject.background_work.main_show.EditTextRaspSearch_Listener;
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener;
import ru.agpu.artikproject.background_work.main_show.TodayClickListener;
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;
import ru.agpu.artikproject.background_work.site_parse.GetCurrentWeekId;
import ru.agpu.artikproject.layout.MainActivity;

public class FragmentMainShow extends Fragment {

    public FragmentMainShow() {
        super(R.layout.fragment_main_activity_main_show);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = (Activity) view.getContext();

        new CheckAppUpdate(activity, false).start(); // Запуск проверки обновлений при входе в приложение
        new LoadBuildingsList(activity).start(); // Загрузка данных об строениях в адаптер

        new GetCurrentWeekId(activity).start(); // Получение номера текущей недели и закидывание списка недель в адаптер

        new TodayClickListener(activity, view.findViewById(R.id.main_activity_text)); // Прослушка нажатий на текущую дату

        // Отслеживание нажатий и зажатий на список групп и аудиторий
        new ListViewGroupListener(activity, view.findViewById(R.id.listview));

        // Отслеживание изменений текстового поля
        new EditTextRaspSearch_Listener(activity, view.findViewById(R.id.rasp_search_edit));

        new WatchSaveGroupRasp(activity); // Первичный вывод групп которые были открыты ранее

        // Отслеживание нажатий на смену даты
        view.findViewById(R.id.subtitle).setOnClickListener(view2 -> {
            new ChangeDay(activity).setDate();
            view2.startAnimation(MainActivity.animScale);
        });
    }
}
