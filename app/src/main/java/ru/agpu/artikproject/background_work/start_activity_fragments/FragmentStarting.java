package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.Starter_MainActivity;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;
import ru.agpu.artikproject.background_work.theme.CustomBackground;

public class FragmentStarting extends Fragment {

    public FragmentStarting() {
        super(R.layout.fragment_start_activity_starting);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        // Если это первый запуск приложения
        if (MySharedPreferences.get(getContext(),"IsFirstAppStart", true)){
            // Запускаем фрагмент с приветствием
            new GetFullGroupList_Online().start(); // Получение полного списка групп и закидывание их в адаптер
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentWelcome.class, null).commit();
        }
        else { // Иначе показываем анимацию запускаи переходим в приложение
            ImageView loading_ico = view.findViewById(R.id.loading_ico);
            new Starter_MainActivity((Activity) view.getContext(), loading_ico).start();
        }
    }

}