package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.presentation.layout.StartActivity;

public class FragmentGroupHelp extends Fragment {

    // Тут вроде все готово
    public FragmentGroupHelp() {
        super(R.layout.fragment_start_activity_group_help);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StartActivity.FRAGMENT = StartActivity.BACK_TO_GROUP;
    }
}