package com.example.artikproject.background_work.site_parse;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFullGroupList_Online extends Thread {
    static public List<String> faculties_name = new ArrayList<>();
    static public List<List<String>> faculties_group_name = new ArrayList<>();
    static public List<List<String>> faculties_group_id = new ArrayList<>();
    Context context;

    public GetFullGroupList_Online(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
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
        for (int i = 1; i<count; i++) {
            String[] faculties_name_buttons = cards[i].split("</button>")[0].split(">");
            faculties_name.add(faculties_name_buttons[faculties_name_buttons.length-1]); // Извлекаем названия факультетов
            String[] faculties_name_class_p_2 = cards[i].split("<div class=\"p-2\">");
            int fac_leght = faculties_name_class_p_2.length;
            List<String> faculties_group_name_cur = new ArrayList<>(); // Инициализируем список с названием групп для текущего факультета
            List<String> faculties_group_id_cur = new ArrayList<>(); // Инициализируем список с id групп для текущего факультета
            for (int j = 1; j<fac_leght; j++){
                String[] faculties_name_class_p_2_a = faculties_name_class_p_2[j].split("</a>")[0].split(">");
                int fac_name_leght = faculties_name_class_p_2_a.length;
                faculties_group_name_cur.add(faculties_name_class_p_2_a[fac_name_leght-1]);
                faculties_group_id_cur.add(faculties_name_class_p_2[j].split("SearchId=")[1].split("&")[0]);
            }
            faculties_group_name.add(faculties_group_name_cur); // Добавляем название группы
            faculties_group_id.add(faculties_group_id_cur); // Добавиляем id группы
        }
    }
}
