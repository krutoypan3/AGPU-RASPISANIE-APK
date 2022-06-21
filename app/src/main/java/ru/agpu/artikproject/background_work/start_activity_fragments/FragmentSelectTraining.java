package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.app.Activity;
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
import ru.agpu.artikproject.background_work.CustomAlertDialog;
import ru.agpu.artikproject.background_work.GetDirectionsList;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.layout.StartActivity;

/**
 * Фрагмент с выбором направления обучения на стартовом экране
 */
public class FragmentSelectTraining extends Fragment {

    public FragmentSelectTraining() {
        super(R.layout.fragment_start_activity_select_training);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StartActivity.FRAGMENT = StartActivity.BACK_TO_GROUP;

        ListView listView = view.findViewById(R.id.list_training);
        EditText training_name_et = view.findViewById(R.id.training_name);
        ArrayList<ListViewItems> sorted_directions = new ArrayList<>();

        ArrayList<List<String>> finalDirections_list = new GetDirectionsList().getDirectionsFromDatabase();

        // Отслеживаем изменения текстового поля
        training_name_et.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {} // До изменения поля
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // После изменения поля
            public void onTextChanged(CharSequence s, int start, int before, int count) { // Во время изменения поля
                String search_dir = training_name_et.getText().toString().trim().toLowerCase(); // Получаем введенный текст без пробелов в начале и конце, маленькими буквами
                sorted_directions.clear(); // Очищаем ранее сформированный список
                if (!(search_dir.equals(""))) { // Если строка поиска не пустая
                    for (int i = 0; i < finalDirections_list.size(); i++){ // Проходимся по всему списку с направлениями
                        if (finalDirections_list.get(i).get(1).toLowerCase().contains(search_dir)){ // Если в направлении есть строка поиска
                            // То добавляем её в новый отсортированный массив
                            sorted_directions.add(new ListViewItems(view.getContext().getString(R.string.Group) + ": " + finalDirections_list.get(i).get(0) + "\n" + finalDirections_list.get(i).get(1)));
                        }
                    }
                }
                // После сортировки выводим новый отсортированный список
                listView.setAdapter(new ListViewAdapter(view.getContext(), sorted_directions));

                if (listView.getCount() == 0) // Если список пуст
                    // Делаем цвет текстового поля красным
                    training_name_et.setTextColor(view.getContext().getColor(R.color.error));
                else
                    // Возвращаем цвет в исходное состояние
                    training_name_et.setTextColor(view.getContext().getColor(R.color.black));
            }
        });

        // Прослушиваем нажатия на направления в списке
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            // Выводим новый всплывающий диалог
            CustomAlertDialog dialog_confirm = new CustomAlertDialog((Activity) view1.getContext(), "update");
            dialog_confirm.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
            dialog_confirm.show();
            // Ставим в главный текст название группы
            dialog_confirm.main_text.setText(sorted_directions.get(i).item.split("\n")[0]);
            dialog_confirm.body_text.setText(view.getContext().getString(R.string.if_understand_group_name));
            dialog_confirm.yes.setText(view.getContext().getString(R.string.go));
            dialog_confirm.no.setText(view.getContext().getString(R.string.Back));
            // Устанавливаем слушатель нажатий на кнопку перехода к группе
            dialog_confirm.yes.setOnClickListener(view2 -> {
                dialog_confirm.dismiss(); // Закрываем диалог
                // Удаляем первую букву Z и S (Удаляем сокращенки и заочки
                // [Вместо SZВМ-ИВТ-3-1 будет показан список: ВМ-ИВТ-3-1, ZВМ-ИВТ-3-1, SZВМ-ИВТ-3-1],
                // это нужно потому что группа может быть как SZВМ так и ZSВМ (короче - человеческий фактор (криворукие не могут определиться в каком порядке ставить буквы))
                // Устанавливаем выбранную группу в основное активити фрагмента
                StartActivity.SELECTED_GROUP = sorted_directions.get(i).item.split("\n")[0].split(view.getContext().getString(R.string.Group) + ": ")[1].toUpperCase().replaceFirst("Z", "").replaceFirst("S", "");
                // Открываем фрагмент с выбором группы
                getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.fragment_container_view, FragmentSelectGroup.class, null).commit();
            });
        });

        // Прослушка нажатий на кнопку помощи с группой
        view.findViewById(R.id.help_group).setOnClickListener(view12 -> {
            view12.findViewById(R.id.help_group).setClickable(false); // Отключаем кнопку после нажатия
            view12.findViewById(R.id.help_group).startAnimation(AnimationUtils.loadAnimation(view12.getContext(), R.anim.scale)); // Воспроизводим анимацию
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentGroupHelp.class, null).commit(); // Переходим к фрагменты с помощью с группой
        });
    }

}