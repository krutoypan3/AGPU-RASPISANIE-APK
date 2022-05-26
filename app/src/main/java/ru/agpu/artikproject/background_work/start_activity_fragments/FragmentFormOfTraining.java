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

public class FragmentFormOfTraining extends Fragment {

    public FragmentFormOfTraining() {
        super(R.layout.fragment_start_activity_form_of_training);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        StartActivity.FRAGMENT = StartActivity.FRAGMENT_FORM_OF_TRAINING;

        // Очная форма обучения
        view.findViewById(R.id.Full_time_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.Full_time_btn).setClickable(false);
            view.findViewById(R.id.Full_time_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            StartActivity.FORM_OF_TRAINING = "Очная";
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectTraining.class, null).commit();
        });

        // Заочная форма обучения
        view.findViewById(R.id.Part_time_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.Part_time_btn).setClickable(false);
            view.findViewById(R.id.Part_time_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            StartActivity.FORM_OF_TRAINING = "Заочная";
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectTraining.class, null).commit();
        });

        // Очно-заочная форма обучения
        view.findViewById(R.id.Full_time_and_part_time_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.Full_time_and_part_time_btn).setClickable(false);
            view.findViewById(R.id.Full_time_and_part_time_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            StartActivity.FORM_OF_TRAINING = "Очно-заочная";
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectTraining.class, null).commit();
        });
    }
}

