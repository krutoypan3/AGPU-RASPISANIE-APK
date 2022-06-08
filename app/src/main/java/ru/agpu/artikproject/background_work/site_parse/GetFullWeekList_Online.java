package ru.agpu.artikproject.background_work.site_parse;

import android.content.ContentValues;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;

public class GetFullWeekList_Online extends Thread{
    static public final List<String> weeks_id = new ArrayList<>();
    static public final List<String> weeks_s_po = new ArrayList<>();
    /**
     * Получает список недель семестра и заносит их в базу данных
     */
    public GetFullWeekList_Online() {
    }

    @Override
    public void run() {
        try{
            Document doc;
            String urlq = "http://www.it-institut.ru/SearchString/ShowTestWeeks?ClientId=118"; // Отправляет запрос для получения Id текущего семестра
            try {
                doc = Jsoup.connect(urlq).get();
            } catch (IOException e) { // Прерывание функции, если нет интернета
                e.printStackTrace();
                return;
            }
            String semestrId = doc.select("div").toString().split("<input id=\"ID\" name=\"ID\"")[doc.select("div").toString().split("<input id=\"ID\" name=\"ID\"").length - 1].split("value=\"")[1].split("\"")[0];
            String urlw = "http://www.it-institut.ru/SearchString/OpenAllWeek?ClientId=118&SemestrId=" + semestrId;
            try {
                doc = Jsoup.connect(urlw).get();
            } catch (IOException e) { // Прерывание функции, если нет интернета
                e.printStackTrace();
                return;
            }
            String[] cards = doc.select("div").toString().split("<a");
            int cards_leght = cards.length;
            for (int i = 2; i < cards_leght; i++) {
                try {
                    String s = cards[i].split("<span>")[2].split("</span>")[0]; // С какого дня недели
                    String po = cards[i].split("<span>")[3].split("</span>")[0]; // По какой день недели
                    String spo = s + " " + po; // Пример "с 09.09.2022 по 16.09.2022"
                    if (!weeks_s_po.contains(spo)) {
                        String id = cards[i].split("WeekId=")[1].split("\"")[0]; // Id недели
                        weeks_id.add(id);
                        weeks_s_po.add(spo);
                        GetCurrentWeekId.weeks_s_po.add(new ListViewItems(spo));
                        ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                        rowValues.put("week_id", id);
                        rowValues.put("week_s", s);
                        rowValues.put("week_po", po);
                        DataBase_Local.sqLiteDatabase.insert("weeks_list", null, rowValues);
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
