package ru.agpu.artikproject.background_work.site_parse;

import android.content.ContentValues;
import android.database.Cursor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.LoadFacultiesList;

public class GetFullGroupList_Online extends Thread {
    static public final ArrayList<ListViewItems> faculties_name = new ArrayList<>();
    static public final List<ArrayList<ListViewItems>> faculties_group_name = new ArrayList<>();
    static public final List<ArrayList<ListViewItems>> faculties_group_id = new ArrayList<>();

    public GetFullGroupList_Online() {
    }

    @Override
    public void run() {
        try {
            Cursor r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT DISTINCT faculties_name FROM groups_list", null);
            if (!(r.getCount() == 0)) { // При отсутствии недели в базе данных обновляем список недель в базе данных
                while (r.moveToNext()) {
                    faculties_name.add(new ListViewItems(r.getString(0)));
                }
                for (int i = 0; i < faculties_name.size(); i++) {
                    String cur_faculties = faculties_name.get(i).item;
                    r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT faculties_group_name, faculties_group_id FROM groups_list WHERE faculties_name = '" + cur_faculties + "'", null);
                    ArrayList<ListViewItems> faculties_group_name_cur = new ArrayList<>(); // Инициализируем список с названием групп для текущего факультета
                    ArrayList<ListViewItems> faculties_group_id_cur = new ArrayList<>(); // Инициализируем список с id групп для текущего факультета
                    while (r.moveToNext()) {
                        faculties_group_name_cur.add(new ListViewItems(r.getString(0)));
                        faculties_group_id_cur.add(new ListViewItems(r.getString(1)));
                    }
                    faculties_group_name.add(faculties_group_name_cur);
                    faculties_group_id.add(faculties_group_id_cur);
                }
            } else {
                Document doc;
                String urlq = "http://www.it-institut.ru/SearchString/Index/118";
                try {
                    doc = Jsoup.connect(urlq).get();
                } catch (IOException e) { // Прерывание функции, если нет интернета
                    e.printStackTrace();
                    return;
                }
                String[] cards = doc.select("body").toString().split("\"card\"");
                int count = cards.length;
                for (int i = 1; i < count; i++) {
                    String[] faculties_name_buttons = cards[i].split("</button>")[0].split(">");
                    String fac_name = faculties_name_buttons[faculties_name_buttons.length - 1]; // Извлекаем названия факультетов
                    faculties_name.add(new ListViewItems(fac_name)); // Добавляем в общий список
                    String[] faculties_name_class_p_2 = cards[i].split("<div class=\"p-2\">");
                    int fac_leght = faculties_name_class_p_2.length;
                    ArrayList<ListViewItems> faculties_group_name_cur = new ArrayList<>(); // Инициализируем список с названием групп для текущего факультета
                    ArrayList<ListViewItems> faculties_group_id_cur = new ArrayList<>(); // Инициализируем список с id групп для текущего факультета
                    for (int j = 1; j < fac_leght; j++) {
                        String[] faculties_name_class_p_2_a = faculties_name_class_p_2[j].split("</a>")[0].split(">");
                        int fac_name_leght = faculties_name_class_p_2_a.length;
                        String faculties_group_name_cur_group = faculties_name_class_p_2_a[fac_name_leght - 1];
                        faculties_group_name_cur.add(new ListViewItems(faculties_group_name_cur_group));
                        String faculties_group_id_cur_group = faculties_name_class_p_2[j].split("SearchId=")[1].split("&")[0];
                        faculties_group_id_cur.add(new ListViewItems(faculties_group_id_cur_group));
                        ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                        rowValues.put("faculties_name", fac_name);
                        rowValues.put("faculties_group_name", faculties_group_name_cur_group);
                        rowValues.put("faculties_group_id", faculties_group_id_cur_group);
                        DataBase_Local.sqLiteDatabase.insert("groups_list", null, rowValues);
                    }
                    faculties_group_name.add(faculties_group_name_cur); // Добавляем название группы
                    faculties_group_id.add(faculties_group_id_cur); // Добавиляем id группы
                }
            }
        }
        finally {
            new LoadFacultiesList().start();
        }
    }
}
