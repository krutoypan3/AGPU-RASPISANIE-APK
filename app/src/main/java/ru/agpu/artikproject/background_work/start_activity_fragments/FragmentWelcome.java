package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.StartActivity;

public class FragmentWelcome extends Fragment {

    public FragmentWelcome() {
        super(R.layout.fragment_start_activity_welcome);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        StartActivity.FRAGMENT = StartActivity.FRAGMENT_WELCOME;

        // Я только поступил
        view.findViewById(R.id.I_just_got_in_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.I_just_got_in_btn).setClickable(false);
            view.findViewById(R.id.I_just_got_in_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentEIOS.class, null).commit();
        });

        // Я студент
        view.findViewById(R.id.Im_a_student_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.Im_a_student_btn).setClickable(false);
            view.findViewById(R.id.Im_a_student_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentEIOS.class, null).commit();
        });

        // Я преподаватель
        view.findViewById(R.id.I_am_a_teacher_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.I_am_a_teacher_btn).setClickable(false);
            view.findViewById(R.id.I_am_a_teacher_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

    }

}

