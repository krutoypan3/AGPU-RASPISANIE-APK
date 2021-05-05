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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class raspisanie_show extends Activity {
    static String[][][] daysps;
    static String[][][][] daysps3;
    static String[][] daysps_time;
    static String [][][] daysps_time3;
    static int week_day012;
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
        daysps3 = MainActivity.daysp3;
        daysps = MainActivity.daysp;
        daysps_time3 = MainActivity.daysp_time3;
        daysps_time = MainActivity.daysp_time;
        mainText = findViewById(R.id.main_text);
        addText = findViewById(R.id.add_text);
        Date date1 = new Date();
        long date_ms = date1.getTime() + 10800000;
        Date date2 = new Date(date_ms);
        week_day = date2.getDay() - 1;
        if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
            week_day = 0;
        }

        week_day012 = 1;
        day_show();
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
                if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
                    week_day = 5;
                    MainActivity.week_id -= 1;
                    week_day012 = 0;
                    new getraspweek().execute("");
                }
                day_show();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                    MainActivity.week_id += 1;
                    week_day012 = 2;
                    new getraspweek().execute("");
                }
                day_show();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                week_day_bt1.setClickable(true);
                week_day_bt2.setClickable(true);
            }
        });
    }
    protected void day_show(){

        String str = "";
        if (daysps_time3[week_day012][week_day][0] == null){
            mainText.setText("Расписание не успевает загрузиться! Пожалуйста, листайте странички медленнее!");
        }
        else {
            mainText.setText((daysps_time3[week_day012][week_day][0] + " " + daysps_time3[week_day012][week_day][1]));
        }
        boolean check = false;
        for (int j = 0; j< daysps3[week_day012][week_day].length; j++){
            for (int g = 0; g< daysps3[week_day012][week_day][j].length-1; g++) {
                if (daysps3[week_day012][week_day][j][g] != null) {
                    str += daysps3[week_day012][week_day][j][g] + "\n";
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

     static public class getraspweek extends AsyncTask<String, String, String> {

         @Override
         protected String doInBackground(String... strings) {
             daysps3 = new String[3][6][10][5];
             daysps_time3 = new String[3][6][2];
             for(int ff = -1; ff<2; ff++) {
                 String urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + MainActivity.selectedItem_id + "&SearchString=" + MainActivity.selectedItem + "&Type=Group&WeekId=" + (MainActivity.week_id + ff);
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
                 daysps = new String[6][10][5];
                 daysps_time = new String[6][2];
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
                         daysps[(i)][(j)] = new String[]{predmet_name, predmet_prepod, predmet_group, predmet_podgroup, predmet_razmer};
                     }
                     String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                     String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                     daysps_time[i] = new String[]{predmet_data_ned, predmet_data_chi};
                 }
                 daysps3[ff+1] = daysps;
                 daysps_time3[ff+1] = daysps_time;
             }
             daysps_time = daysps_time3[1];
             daysps = daysps3[1];
             week_day012 = 1;
             return null;
         }
     }


 }
