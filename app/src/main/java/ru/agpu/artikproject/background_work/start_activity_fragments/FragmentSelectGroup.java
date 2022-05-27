package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.StartActivity;

public class FragmentSelectGroup extends Fragment {
    static ArrayList<ListViewItems> groups_name = new ArrayList<>();
    static ArrayList<String> groups_id = new ArrayList<>();

    // Тут вроде все готово
    public FragmentSelectGroup() {
        super(R.layout.fragment_start_activity_select_group);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        Activity act = (Activity) view.getContext();
        ListView listView = view.findViewById(R.id.list_groups);
        EditText group_name_et = view.findViewById(R.id.group_name);

        StartActivity.FRAGMENT = StartActivity.BACK_TO_GROUP;

        view.findViewById(R.id.help_group).setOnClickListener(view12 -> {
            view12.findViewById(R.id.help_group).setClickable(false);
            view12.findViewById(R.id.help_group).startAnimation(AnimationUtils.loadAnimation(view12.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentGroupHelp.class, null).commit();
        });

        group_name_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {} // До изменения поля
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // После изменения поля
            public void onTextChanged(CharSequence s, int start, int before, int count) { // Во время изменения поля
                String search_group = group_name_et.getText().toString().trim().toLowerCase();
                groups_name.clear();
                groups_id.clear();
                if (!(search_group.equals(""))) { // Если строка поиска не пустая
                    for (int i = 0; i < GetFullGroupList_Online.faculties_group_name.size(); i++){
                        for (int j = 0; j < GetFullGroupList_Online.faculties_group_name.get(i).size(); j++){
                            if (GetFullGroupList_Online.faculties_group_name.get(i).get(j).item.toLowerCase().contains(search_group)){
                                groups_id.add(GetFullGroupList_Online.faculties_group_id.get(i).get(j).item);
                                groups_name.add(new ListViewItems(GetFullGroupList_Online.faculties_group_name.get(i).get(j).item));
                            }
                        }
                    }
                }
                listView.setAdapter(new ListViewAdapter(view.getContext(), groups_name));
                if (listView.getCount() == 0)
                    group_name_et.setTextColor(view.getContext().getColor(R.color.error));
                else
                    group_name_et.setTextColor(view.getContext().getColor(R.color.black));
            }
        });

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(act.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("start_rasp", true);
            intent.putExtra("selectedItem_id", groups_id.get(i));
            intent.putExtra("selectedItem_type", "Group");
            intent.putExtra("selectedItem", groups_name.get(i).item);
            act.startActivity(intent);
        });

    }

}