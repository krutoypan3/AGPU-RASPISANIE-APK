package com.example.artikproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class raspisanie_show extends Activity {
    static String[][][][] daysps3 = new String[3][6][10][5];
    static String [][][] daysps_time3 = new String[3][6][2];
    static int week_day012;
    static int week_ids;
    TextView mainText;
    TextView addText;
    static int week_day;

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raspisanie_layout);
        daysps3 = MainActivity.copy4d(MainActivity.daysp3);
        week_ids = MainActivity.week_id;
        daysps_time3 = MainActivity.copy3d(MainActivity.daysp_time3);
        mainText = findViewById(R.id.main_text);
        addText = findViewById(R.id.add_text);
        Date date1 = new Date(); // Эти строки отвечают за
        long date_ms = date1.getTime() + 10800000; // получение текущего
        Date date2 = new Date(date_ms); // дня недели и
        week_day = date2.getDay() - 1;
        if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
            week_day = 0;
            week_ids += 1;
        }

        week_day012 = 1;
        day_show(week_day012, week_day, daysps3, daysps_time3);
        new getraspweek().execute("");


        Button week_day_bt1;
        Button week_day_bt2;
        week_day_bt1 = findViewById(R.id.week_day_bt1);
        week_day_bt2 = findViewById(R.id.week_day_bt2);
        week_day_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_day_bt1.setClickable(false);
                week_day_bt2.setClickable(false);
                week_day -= 1;
                if (week_day == -1){ // Если будет воскресенье, то будет показана суббота
                    week_day = 5;
                    week_ids -= 1;
                    week_day012 = 0;
                    new getraspweek().execute("");
                }
                day_show(week_day012, week_day, daysps3, daysps_time3);
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }

        });
        week_day_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_day_bt1.setClickable(false);
                week_day_bt2.setClickable(false);
                week_day += 1;
                if (week_day == 6){ // Если будет воскресенье, то будет показана суббота
                    week_day = 0;
                    week_ids += 1;
                    week_day012 = 2;
                    new getraspweek().execute("");
                }
                day_show(week_day012, week_day, daysps3, daysps_time3);
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
        });
    }
    protected void day_show(int week_day012_f, int week_day_f, String[][][][] daysps3_f, String[][][] daysps_time3_f){

        String str = "";
        if (daysps_time3_f[week_day012_f][week_day_f][0] == null){
            mainText.setText("Расписание не успевает загрузиться! Пожалуйста, листайте странички медленнее!");
        }
        else {
            mainText.setText((daysps_time3_f[week_day012_f][week_day_f][0] + " " + daysps_time3_f[week_day012_f][week_day_f][1]));
        }
        boolean check = false;
        for (int j = 0; j< daysps3_f[week_day012_f][week_day_f].length; j++){
            for (int g = 0; g< daysps3_f[week_day012_f][week_day_f][j].length-1; g++) {
                if (daysps3_f[week_day012_f][week_day_f][j][g] != null) {
                    str += daysps3_f[week_day012_f][week_day_f][j][g] + "\n";
                    check = true;
                }
            }
            if (check) {
                str += "\n";
                check = false;
            }
        }
        addText.setText(str);
    }

    private class getraspweek extends AsyncTask<String, String, String> {
        private String[][][] dayspsf = new String[6][10][5];
        private String[][][][] dayspsf3 = new String[3][6][10][5];
        private String[][] dayspsf_time = new String[6][2];
        private String[][][] dayspsf_time3 = new String[3][6][2];

         @Override
         protected String doInBackground(String... strings) {
             for(int ff = -1; ff<2; ff++) {
                 String urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&SearchString=" + MainActivity.selectedItem + "&Type=Group&WeekId=" + (week_ids + ff);
                 Document doc = null;
                 try {
                     doc = Jsoup.connect(urlq).get();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 assert doc != null;
                 List<String[]> days = new ArrayList<>();
                 for (int i = 1; i < 7; i++) {
                     days.add(doc.select("tbody").toString().split("th scope")[i].split("td colspan="));
                 }
                 String[][] day;
                 day = days.toArray(new String[0][0]);
                 for (int i = 0; i < 6; i++) {
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
                         }
                         catch (Exception e) {
                         }
                         dayspsf[(i)][(j)] = new String[]{predmet_name, predmet_prepod, predmet_group, predmet_podgroup, predmet_razmer};
                     }
                     String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                     String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                     dayspsf_time[i] = new String[]{predmet_data_ned, predmet_data_chi};
                 }
                 dayspsf3[ff+1] = MainActivity.copy3d(dayspsf);
                 dayspsf_time3[(ff+1)] = MainActivity.copy2d(dayspsf_time);
             }
             week_day012 = 1;
             daysps3 = MainActivity.copy4d(dayspsf3);
             daysps_time3 = MainActivity.copy3d(dayspsf_time3);
             return null;
         }
     }


 }
