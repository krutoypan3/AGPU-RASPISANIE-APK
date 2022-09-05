package ru.agpu.artikproject.background_work.main_show.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;
import ru.agpu.artikproject.data.repository.groups_list.GroupsListImpl;
import ru.agpu.artikproject.domain.repository.GroupsListRepository;
import ru.agpu.artikproject.domain.usecase.groups_list.GroupsListGetFacultiesUseCase;

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

        GroupsListRepository groupsListRepository = new GroupsListImpl(requireContext());
        List<String> groupsListItems = new GroupsListGetFacultiesUseCase(groupsListRepository).execute();

        List<RecyclerViewItems> facultiesListRVItems = new ArrayList<>();

        for (int i = 0; i < groupsListItems.size(); i++) {
            String facultiesName = groupsListItems.get(i);
            String ico = "https://i.ibb.co/PZhZjkZ/agpu-ico.png";
            if (facultiesName.contains("Институт прикладной информатики"))
                ico = "https://i.ibb.co/j33KDk0/agpu-ico-ipimif.png";
            else if (facultiesName.contains("Институт русской"))
                ico = "https://i.ibb.co/K6xBNY0/agpu-ico-iriif.png";
            else if (facultiesName.contains("Исторический факультет"))
                ico = "https://i.ibb.co/z42VmXz/agpu-ico-istfak.png";
            else if (facultiesName.contains("Социально-психологический факультет"))
                ico = "https://i.ibb.co/FzXkFdy/agpu-ico-spf.png";
            else if (facultiesName.contains("Факультет дошкольного"))
                ico = "https://i.ibb.co/hWQR2CL/agpu-ico-fdino.png";
            else if (facultiesName.contains("Факультет технологии"))
                ico = "https://i.ibb.co/gg2rsfV/agpu-ico-fteid.png";
            facultiesListRVItems.add(new RecyclerViewItems(facultiesName, ico, ""));
        }

        switch (SELECTED_LIST){
            case (1):
                recyclerView.setAdapter(new RecyclerViewAdapter(activity, LoadBuildingsList.buildings_list, RecyclerViewAdapter.IS_BUILDINGS_ADAPTER));
                break;
            case(2):
                recyclerView.setAdapter(new RecyclerViewAdapter(activity, facultiesListRVItems, RecyclerViewAdapter.IS_FACULTIES_ADAPTER));
                break;
        }

    }
}
