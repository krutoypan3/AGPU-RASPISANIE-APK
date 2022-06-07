package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.layout.StartActivity;

/**
 * Фрагмент, на котором предоставляется выбор: "Выбор группы" или "Выбор направления"
 */
public class FragmentGroup extends Fragment {

    public FragmentGroup() {
        super(R.layout.fragment_start_activity_group);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        StartActivity.FRAGMENT = StartActivity.BACK_TO_EIOS;

        // Я знаю название группы
        view.findViewById(R.id.I_know_group_name_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.I_know_group_name_btn).setClickable(false);
            view.findViewById(R.id.I_know_group_name_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectGroup.class, null).commit();
        });

        // Я знаю направление подготовки
        view.findViewById(R.id.I_know_the_direction_of_training_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.I_know_the_direction_of_training_btn).setClickable(false);
            view.findViewById(R.id.I_know_the_direction_of_training_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectTraining.class, null).commit();
        });
    }
}

