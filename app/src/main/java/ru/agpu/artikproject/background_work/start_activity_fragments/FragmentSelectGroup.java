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
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.data.repository.TextDetranslitImpl;
import ru.agpu.artikproject.data.repository.groups_list.GroupsListImpl;
import ru.agpu.artikproject.domain.models.GroupsListItem;
import ru.agpu.artikproject.domain.repository.GroupsListRepository;
import ru.agpu.artikproject.domain.repository.TextDetranslitRepository;
import ru.agpu.artikproject.domain.usecase.TextDetranslitUseCase;
import ru.agpu.artikproject.domain.usecase.groups_list.GroupsListGetUseCase;
import ru.agpu.artikproject.presentation.layout.MainActivity;
import ru.agpu.artikproject.presentation.layout.StartActivity;

public class FragmentSelectGroup extends Fragment {
    static ArrayList<ListViewItems> groups_name = new ArrayList<>(); // Отсортированный список с названиями групп
    static ArrayList<String> groups_id = new ArrayList<>(); // Отсортированный список с id групп

    // Тут вроде все готово
    public FragmentSelectGroup() {
        super(R.layout.fragment_start_activity_select_group);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity act = (Activity) view.getContext();
        ListView listView = view.findViewById(R.id.list_groups);
        EditText group_name_et = view.findViewById(R.id.group_name);

        StartActivity.FRAGMENT = StartActivity.BACK_TO_GROUP;

        // Прослушка нажатий на кнопку помощи с группой
        view.findViewById(R.id.help_group).setOnClickListener(view12 -> {
            view12.findViewById(R.id.help_group).setClickable(false); // Отключаем кнопку после нажатия
            view12.findViewById(R.id.help_group).startAnimation(AnimationUtils.loadAnimation(view12.getContext(), R.anim.scale)); // Воспроизводим анимацию
            getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container_view, FragmentGroupHelp.class, null).commit(); // Переходим к фрагменты с помощью с группой
        });

        GroupsListRepository groupsListRepository = new GroupsListImpl(view.getContext());
        List<GroupsListItem> groupsListItems = new GroupsListGetUseCase(groupsListRepository).execute();

        TextDetranslitRepository textDetranslitRepository = new TextDetranslitImpl();
        TextDetranslitUseCase textDetranslitUseCase = new TextDetranslitUseCase(textDetranslitRepository);

        // Прослушиваем изменения текстового поля ввода группы
        group_name_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {} // До изменения поля
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // После изменения поля
            public void onTextChanged(CharSequence s, int start, int before, int count) { // Во время изменения поля
                String search_group = group_name_et.getText().toString().trim().toLowerCase();
                groups_name.clear(); // Очищаем списки с ранее отсортированными результатами
                groups_id.clear(); // Так же очищаем отсортированные id
                if (!(search_group.equals(""))) { // Если строка поиска не пустая
                    for (int i = 0; i < groupsListItems.size(); i++){
                        // Сравниваем "детранслированный" текст сохраненных групп с "детранслированным" текстом строки поиска
                        if (textDetranslitUseCase.execute(groupsListItems.get(i).getGroupName().toLowerCase()).contains(textDetranslitUseCase.execute(search_group))){
                            groups_id.add(groupsListItems.get(i).getGroupId()); // Добавляем ID группы в отсортированный массив
                            groups_name.add(new ListViewItems(groupsListItems.get(i).getGroupName())); // Добавляем название группы в отсортированный массив
                        }
                    }
                }
                // После сортировки применяем адаптер с отсортированными группами
                listView.setAdapter(new ListViewAdapter(view.getContext(), groups_name));
                if (listView.getCount() == 0) // Если групп не было найдено
                    // Делаем цвет строки поиска красным
                    group_name_et.setTextColor(view.getContext().getColor(R.color.error));
                else // Возвращаем цвет строки поиска в исходное состояние
                    group_name_et.setTextColor(view.getContext().getColor(R.color.black));
            }
        });

        group_name_et.setText(StartActivity.SELECTED_GROUP); // Устанавливаем ранее выбранную группу в поле ввода (если таковая есть)
        // Прослушиваем нажатия на группы
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            // При нажатии на группу - открывается расписание с выбранной группой
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