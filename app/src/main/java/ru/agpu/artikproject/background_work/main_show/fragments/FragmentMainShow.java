package ru.agpu.artikproject.background_work.main_show.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.FirstAppStartHelper;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.main_show.ChangeDay;
import ru.agpu.artikproject.background_work.main_show.EditTextRaspSearch_Listener;
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener;
import ru.agpu.artikproject.background_work.main_show.TodayClickListener;
import ru.agpu.artikproject.background_work.main_show.UpdateDateInMainActivity;
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp;
import ru.agpu.artikproject.layout.MainActivity;

public class FragmentMainShow extends Fragment {

    public FragmentMainShow() {
        super(R.layout.fragment_main_activity_main_show);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = (Activity) view.getContext();

        // Прослушка нажатий на текущую дату
        new TodayClickListener(activity, view.findViewById(R.id.main_activity_text));

        // Отслеживание нажатий и зажатий на список групп и аудиторий
        new ListViewGroupListener(activity, view.findViewById(R.id.listview));

        // Обновляем текущую неделю на главной странице
        new UpdateDateInMainActivity(activity).start();

        // Отслеживание изменений текстового поля
        new EditTextRaspSearch_Listener(activity, view.findViewById(R.id.rasp_search_edit));

        // Первичный вывод групп которые были открыты ранее
        new WatchSaveGroupRasp(activity);

        // Отслеживание нажатий на смену даты
        view.findViewById(R.id.subtitle).setOnClickListener(view2 -> {
            new ChangeDay(activity).setDate();
            view2.startAnimation(MainActivity.animScale);
        });

        if (MySharedPreferences.get(getContext(), "IsFirstAppStart", true)) {
            MySharedPreferences.put(getContext(), "IsFirstAppStart", false);
            new FirstAppStartHelper((Activity) view.getContext());
        }
    }
}
