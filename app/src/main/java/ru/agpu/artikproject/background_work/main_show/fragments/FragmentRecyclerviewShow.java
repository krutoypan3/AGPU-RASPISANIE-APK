package ru.agpu.artikproject.background_work.main_show.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.LoadFacultiesList;

public class FragmentRecyclerviewShow extends Fragment {
    public static int SELECTED_LIST;

    public FragmentRecyclerviewShow() {
        super(R.layout.fragment_main_activity_recyclerview_show);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        Activity activity = (Activity) view.getContext();

        switch (SELECTED_LIST){
            case (1):
                recyclerView.setAdapter(new RecyclerViewAdapter(activity, LoadBuildingsList.buildings_list, RecyclerViewAdapter.IS_BUILDINGS_ADAPTER));
                break;
            case(2):
                recyclerView.setAdapter(new RecyclerViewAdapter(activity, LoadFacultiesList.FACULTIES_LIST, RecyclerViewAdapter.IS_FACULTIES_ADAPTER));
                break;
        }

    }
}
