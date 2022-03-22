package com.example.artikproject.background_work.main_show;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFullWeekList_Button {
    static public List<String> weeks_id = new ArrayList<>();
    static public List<String> weeks_s_po = new ArrayList<>();

    public GetFullWeekList_Button(Context context){
        Document doc;
        String urlq = "http://www.it-institut.ru/SearchString/ShowTestWeeks?ClientId=118";
        try {
            doc = Jsoup.connect(urlq).get();
        } catch (IOException e) { // Прерывание функции, если нет интернета
            e.printStackTrace();
            return;
        }
        String semestrId = doc.select("div").toString().split("<input id=\"ID\" name=\"ID\"")[1].split("value=\"")[1].split("\"")[0];
        String urlw = "http://www.it-institut.ru/SearchString/OpenAllWeek?ClientId=118&SemestrId=" + semestrId;
        try {
            doc = Jsoup.connect(urlw).get();
        } catch (IOException e) { // Прерывание функции, если нет интернета
            e.printStackTrace();
            return;
        }
        String[] cards =  doc.select("div").toString().split("<a");
        int cards_leght = cards.length;
        for (int i=2; i<cards_leght;i++){
            try{
                String s = cards[i].split("<span>")[2].split("</span>")[0];
                String po = cards[i].split("<span>")[3].split("</span>")[0];
                String spo = s + " " + po;
                if (!weeks_s_po.contains(spo)){
                    weeks_id.add(cards[i].split("WeekId=")[1].split("\"")[0]);
                    weeks_s_po.add(spo);
                }
            }
            catch (Exception ignored){}
        }
    }
}
