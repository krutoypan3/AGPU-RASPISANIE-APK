package ru.agpu.artikproject.background_work.rasp_show;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.RecyclerViewAdapter;
import ru.agpu.artikproject.background_work.rasp_show.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.background_work.theme.ColorChanger;
import ru.agpu.artikproject.background_work.theme.Theme;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class Day_show {
    /**
     * Класс отвечающий за показ расписания в дневном режиме
     * @param activity Контекст приложения
     */
    public Day_show(Activity activity) {
        ArrayList<ListViewItems> group_list = new ArrayList<>();
        Cursor r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                "r_week_number = " + MainActivity.week_id + " AND " +
                "r_week_day = " + MainActivity.week_day + " ORDER BY r_para_number", null);
        List<RecyclerViewItems> list = new ArrayList<>();
        if (r.getCount() != 0) { // Если количество элементов не равно нулю
            r.moveToFirst(); // Переходим к первому элементу
            String prev_time = "";
            int prev_number = 0;
            String mainText = r.getString(10) + " " + r.getString(11); // Получаем текущий день недели и дату
            Raspisanie_show.mainText.setText(mainText); // Устанавливаем полученную дату на экране расписания
            do {
                if (r.getString(4) != null) { // Если название пары не пустое
                    String para_number; // Номер пары
                    String para_time; // Время пары
                    if (Objects.equals(prev_time, r.getString(9))){ // Если предыдущее время пары совпадает с текущим (две пары в одной время)
                        para_number = String.valueOf(prev_number); // Оставляем номер прежним
                        para_time = prev_time.replace("-", "\n"); // Оставляем время тоже прежним
                    }
                    else{ // Если время не совпадает
                        prev_number += 1; // Добавляем номер пары в предыдущее значение
                        para_number = String.valueOf(prev_number); // Номер пары в расписании
                        para_time = r.getString(9).replace("-", "\n"); // Время пары с какого по какое
                        prev_time = r.getString(9); // Устанавливаем текущее время предыдущим
                    }
                    String para_name = r.getString(4); // Название пары
                    String para_prepod = r.getString(5) == null ? "": r.getString(5); // Преподаватель

                    String para_aud = r.getString(8) == null ? "": r.getString(8); // Аудитория
                    if(para_aud.equals("") && para_prepod.contains(",")){ // Если в имени преподавателя есть "," то после нее будет номер аудитории (ДОЛЖНА БЫТЬ ПО КРАЙНЕЙ МЕРЕ)
                        para_aud = para_prepod.split(",")[para_prepod.split(",").length - 1]; // Делим по запятой и берем последнюю часть
                    }
                    if (!para_aud.equals("")) // Если аудитория не пустая
                        para_aud = activity.getString(R.string.Audience) + ": " + para_aud; // Добавляем слово "Аудитория:" к аудитории

                    if (para_prepod.contains(",")){ // Если у преподавателя есть "," в тексте
                        String[] prepod_splited = para_prepod.split(","); // Делим по символу ","
                        StringBuilder new_para_prepod = new StringBuilder(prepod_splited[0]);
                        for (int i=1; i < prepod_splited.length-1; i++){ // Собираем весь текст кроме последнего отделенного фрагмента
                            new_para_prepod.append(",").append(prepod_splited[i]);
                        }
                        para_prepod = activity.getString(R.string.Prepod) + ": " + new_para_prepod; // Добавляем слово "Преподаватель:" к аудитории
                    }

                    String para_group = r.getString(6) == null ? "": r.getString(6); // Группа
                    if (!para_group.equals("")) // Если группа не пустая, то добавляем слово "Группа:" к группе
                        para_group = activity.getString(R.string.Group) + ": " + para_group;
                    String para_podgroup = r.getString(7) == null ? "": r.getString(7); // Подгруппа

                    String para_distant = r.getString(15) == null ? "": r.getString(15); // Дистант или нет
                    int para_color = r.getString(14) == null ? Color.LTGRAY: Color.parseColor(r.getString(14)); // Цвет пары

                    int rasp_color_right; // Цвет фона правой части расписания
                    int rasp_color_left; // Цвет фона левой части расписания
                    if (Theme.getApplicationTheme(activity) == AppCompatDelegate.MODE_NIGHT_YES) { // Если стоит ночная тема
                        rasp_color_right = ColorChanger.GetDarkColor(para_color, 120); // Затемняем цвет фона правой части
                        rasp_color_left = ColorChanger.GetLightColor(rasp_color_right, 30); // Затемняем цвет фона левой части чуть меньше правой
                    }
                    else{ // Если тема дневная
                        rasp_color_right = ColorChanger.GetLightColor(para_color, 30); // Немного осветляем правый фон
                        rasp_color_left = para_color; // А левый оставляем таким как получили с сайта
                    }

                    list.add(new RecyclerViewItems(para_number, para_time, para_name, para_aud + " " +
                            para_distant + "\n" + para_group + " " + para_podgroup, para_prepod,
                            rasp_color_left, rasp_color_right)); // Добавляем пару в список
                }
            } while (r.moveToNext());
        } else { // Если кол-во элементов = 0
            if (CheckInternetConnection.getState(activity)) { // Проверяем подключение к интернету и обновляем расписание
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, activity).start();
            }
        }
        RecyclerView recyclerView = activity.findViewById(R.id.day_para_view_rec); // Находим нашу вьюшку на слое
        recyclerView.setLayoutManager(new LinearLayoutManager(activity)); // Это обязательная фигня, я хз, но без нее список просто не показывается
        recyclerView.setAdapter(new RecyclerViewAdapter(activity, list)); // Устанавливаем новоиспеченный адаптер
    }
}

