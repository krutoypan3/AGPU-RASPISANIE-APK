package ru.agpu.artikproject.background_work.main_show.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class FragmentSelectGroupDirectionFaculty extends Fragment {

    public FragmentSelectGroupDirectionFaculty() {
        super(R.layout.fragment_main_activity_select_group_or_direction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.fac_btn).setOnClickListener(view1 -> {
            FragmentRecyclerviewShow.SELECTED_LIST = 2;
            MainActivity.FRAGMENT = MainActivity.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY;
            MainActivity.fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container_view, FragmentRecyclerviewShow.class, null)
                    .commit();
        });

        view.findViewById(R.id.dir_btn).setOnClickListener(view12 ->{
            MainActivity.FRAGMENT = MainActivity.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY;
            MainActivity.fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container_view, FragmentSelectTraining.class, null)
                        .commit();
        });

        view.findViewById(R.id.grp_btn).setOnClickListener(view13 ->{
                MainActivity.FRAGMENT = MainActivity.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY;
                MainActivity.fragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container_view, FragmentSelectGroup.class, null)
                        .commit();
        });
    }
}
