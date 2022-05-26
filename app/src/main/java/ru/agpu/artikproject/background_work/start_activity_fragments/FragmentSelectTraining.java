package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.layout.StartActivity;

public class FragmentSelectTraining extends Fragment {

    public FragmentSelectTraining() {
        super(R.layout.fragment_start_activity_select_training);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        StartActivity.FRAGMENT = StartActivity.FRAGMENT_SELECT_TRAINING;

        EditText training_name_et = view.findViewById(R.id.training_name);
        String training_name = training_name_et.getText().toString();
    }

}