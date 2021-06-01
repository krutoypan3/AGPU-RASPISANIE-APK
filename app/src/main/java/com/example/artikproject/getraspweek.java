package com.example.artikproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class getraspweek extends AsyncTask<String, String, String> { // INTERNET ONLY

    @Override
    protected String doInBackground(String... strings) {
        for (int ff = -1; ff < 2; ff++) {
            String urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&SearchString=" + MainActivity.selectedItem + "&Type="+ MainActivity.selectedItem_type + "&WeekId=" + (MainActivity.week_id + ff);
            Document doc = null;
            try {
                doc = Jsoup.connect(urlq).timeout(5000).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            assert doc != null;
            List<String[]> days = new ArrayList<>();
            for (int i = 1; i < 7; i++) {
                days.add(doc.select("tbody").toString().split("th scope")[i].split("td colspan="));
            }
            String[] day_data_razmer = new String[7];
            String[] day_data_time = new String[7];
            String[] dataaa = doc.select("thead").toString().split("colspan=\"");
            for (int i = 1; i < 8; i++) {
                day_data_razmer[i-1] = (dataaa[i].split("\"")[0]);
                day_data_time[i-1] = (dataaa[i].split(">")[2].split("<")[0]);
            }
            String[][] day;
            day = days.toArray(new String[0][0]);
            for (int i = 0; i < 6; i++) {
                boolean prohod = false;
                int schet = 0;
                String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                for (int j = 0; j < 10; j++) {

                    String predmet_name = null;
                    String predmet_prepod = null;
                    String predmet_group = null;
                    String predmet_podgroup = null;
                    String predmet_razmer = null;
                    try {
                        predmet_razmer = day[i][j].split("\"")[1];
                        predmet_name = day[i][j].split("<span>")[1].split("</span>")[0];
                        predmet_prepod = day[i][j].split("<span>")[2].split("</span>")[0];
                        predmet_group = day[i][j].split("<span>")[3].split("</span>")[0];
                        predmet_podgroup = day[i][j].split("<span>")[4].split("</span>")[0];
                    } catch (Exception ignored) {
                    }
                    String predmet_time = null;
                    if((j>0) && (schet < 7)){
                        predmet_time = day_data_time[schet];
                        if(day_data_razmer[schet].equals(predmet_razmer)){
                            if (prohod){
                                prohod = false;
                            }
                            else{
                                schet++;
                            }
                        }
                        else{
                            if (!prohod){
                                prohod = true;
                            }
                            else{
                                schet++;
                                prohod = false;
                            }
                        }
                    }
                    Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + MainActivity.selectedItem_id + " AND r_week_number = " + (MainActivity.week_id + ff) + " AND r_week_day = " + i + " AND r_para_number = " + j, null); // SELECT запрос
                    if (r.getCount() == 0) {// Если даной недели нет в базе
                        ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                        rowValues.put("r_group_code", MainActivity.selectedItem_id);
                        rowValues.put("r_week_day", i);
                        rowValues.put("r_week_number", (MainActivity.week_id + ff));
                        rowValues.put("r_para_number", j);
                        rowValues.put("r_name", predmet_name);
                        rowValues.put("r_prepod", predmet_prepod);
                        rowValues.put("r_group", predmet_group);
                        rowValues.put("r_podgroup", predmet_podgroup);
                        rowValues.put("r_aud", "");
                        rowValues.put("r_razmer", predmet_time);
                        rowValues.put("r_week_day_name", predmet_data_ned);
                        rowValues.put("r_week_day_date", predmet_data_chi);
                        rowValues.put("r_search_type", MainActivity.selectedItem_type);
                        rowValues.put("r_last_update", new Date().getTime());
                        MainActivity.sqLiteDatabase.insert("rasp_test1", null, rowValues); // Вставка строки в базу данных
                    }
                }
            }}
        return null;
    }
}