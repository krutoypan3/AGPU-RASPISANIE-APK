package com.example.artikproject.background_work.main_show;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.artikproject.background_work.datebase.DataBase_Local;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//         https://ru.stackoverflow.com/questions/1040388/%D0%91%D0%BE%D0%BA%D0%BE%D0%B2%D0%BE%D0%B5-%D0%BC%D0%B5%D0%BD%D1%8E-%D0%B2-android
public class GetFullGroupList_Button {
    static public List<String> faculties_name = new ArrayList<>();
    static public List<List<String>> faculties_group_name = new ArrayList<>();
    static public List<List<String>> faculties_group_url_name = new ArrayList<>();
    static public List<List<String>> faculties_group_id = new ArrayList<>();
    SQLiteDatabase sqLiteDatabaseS;

    public GetFullGroupList_Button(Context context){
        this.sqLiteDatabaseS = new DataBase_Local(context).getWritableDatabase(); //Подключение к базе данных
        Document doc;
        String urlq = "http://www.it-institut.ru/SearchString/Index/118";
        try {
            doc = Jsoup.connect(urlq).get();
        } catch (IOException e) { // Прерывание функции, если нет интернета
            e.printStackTrace();
            this.sqLiteDatabaseS.close();
            return;
        }
        String[] cards = doc.select("body").toString().split("\"card\"");
        int count = cards.length;
        for (int i = 1; i<count; i++) {
            String[] faculties_name_buttons = cards[i].split("</button>")[0].split(">");
            faculties_name.add(faculties_name_buttons[faculties_name_buttons.length-1]);
            String[] faculties_name_class_p_2 = cards[i].split("<div class=\"p-2\">");
            int fac_leght = faculties_name_class_p_2.length;
            List<String> faculties_group_name_cur = new ArrayList<>();
            List<String> faculties_group_url_name_cur = new ArrayList<>();
            List<String> faculties_group_id_cur = new ArrayList<>();
            for (int j = 1; j<fac_leght; j++){
                String[] faculties_name_class_p_2_a = faculties_name_class_p_2[j].split("</a>")[0].split(">");
                int fac_name_leght = faculties_name_class_p_2_a.length;
                faculties_group_name_cur.add(faculties_name_class_p_2_a[fac_name_leght-1]);
                faculties_group_url_name_cur.add(faculties_name_class_p_2[j].split("SearchString=")[1].split("&")[0]);
                faculties_group_id_cur.add(faculties_name_class_p_2[j].split("SearchId=")[1].split("&")[0]);
            }
            faculties_group_name.add(faculties_group_name_cur);
            faculties_group_url_name.add(faculties_group_url_name_cur);
            faculties_group_id.add(faculties_group_id_cur);
        }
    }
}
