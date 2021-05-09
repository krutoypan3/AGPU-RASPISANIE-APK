package com.example.artikproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.StatementEvent;

public class MainActivity extends AppCompatActivity {

    private EditText rasp_search_edit;
    private TextView result;
    private String[] group_listed;
    private String[] group_listed_type;
    private String[] group_listed_id;
    private ListView listview;
    private TextView subtitle;
    static public String selectedItem;
    static public String selectedItem_type;
    static public String selectedItem_id;
    public static int week_id;
    public static SQLiteDatabase sqLiteDatabase;
    public static int week_day;

    public static boolean isOnline(Context context){ // Функция определяющая есть ли интернет
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true; // Интернет есть
        }
        return false; // Интернета нет
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Date date1 = new Date();
        long date_ms = date1.getTime() + 10800000;
        week_id = (int) ((date_ms - 18489514000f) / 1000f / 60f / 60f / 24f / 7f); // Номер текущей недели

        Date date2 = new Date(date_ms); // дня недели и
        week_day = date2.getDay() - 1;
        if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
            week_day = 0;
            week_id += 1;
        }

        listview = (ListView) findViewById(R.id.listview);
        subtitle = (TextView) findViewById(R.id.subtitle);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                selectedItem = MainActivity.this.group_listed[position];
                selectedItem_type = MainActivity.this.group_listed_type[position];
                selectedItem_id = MainActivity.this.group_listed_id[position];
                MainActivity.this.subtitle.setText(selectedItem);

                if (!isOnline(MainActivity.this)){
                    Cursor r = sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + selectedItem_id + " AND r_week_number = " + week_id, null); // SELECT запрос
                    if (r.getCount()==0){// Если даной недели нет в базе
                        result.setText("НЕТ ПОДКЛЮЧЕНИЯ К ИНТЕРНЕТУ!");
                    }
                    else{ // Если неделя есть в базе
                        Intent intent = new Intent(MainActivity.this, raspisanie_show.class);
                        startActivity(intent);
                    }
                }
                else new GetRasp().execute(selectedItem);
            }
        });
        rasp_search_edit = findViewById(R.id.rasp_search_edit);
        Button main_button = findViewById(R.id.main_button);
        result = findViewById(R.id.result);


        sqLiteDatabase = new DataBase(MainActivity.this).getWritableDatabase(); //Подключение к базе данных

        main_button.setOnClickListener(new View.OnClickListener() { // Функция поиска группы или аудитории или преподователя при нажатии на кнопку
            @Override
            public void onClick(View v) {
                if (rasp_search_edit.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_SHORT).show();
                    result.setText("");
                }
                else {

                    if (!isOnline(MainActivity.this)){
                        Cursor r;
                        r = sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM rasp_test1 WHERE r_group NOT NULL AND r_prepod NOT NULL GROUP BY r_group_code", null);
                        if (r.moveToFirst()){
                            List<String> group_list = new ArrayList<>();
                            List<String> group_list_type = new ArrayList<>();
                            List<String> group_list_id = new ArrayList<>();
                            do{
                                switch (r.getString(2)) {
                                    case "Group":
                                        group_list.add(r.getString(1));
                                        break;
                                    case "Classroom":
                                        group_list.add(r.getString(3).split(",")[2]);
                                        break;
                                    case "Teacher":
                                        group_list.add(r.getString(3).split(",")[0]);
                                        break;
                                }
                                group_list_type.add(r.getString(2));
                                group_list_id.add(r.getString(0));
                            }while(r.moveToNext());
                            MainActivity.this.group_listed = group_list.toArray(new String[0]);
                            MainActivity.this.group_listed_type = group_list_type.toArray(new String[0]);
                            MainActivity.this.group_listed_id = group_list_id.toArray(new String[0]);
                        } // Вывод SELECT запроса
                        if( group_listed == null){
                            result.setText("Увы, но у вас нет сохраненных групп...");
                        }
                        else {
                            ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, group_listed);
                            listview.setAdapter(adapter);
                        }
                    }
                    else {
                        String urlq = "https://www.it-institut.ru/SearchString/KeySearch?Id=118&SearchProductName=" + rasp_search_edit.getText().toString();
                        result.setText(rasp_search_edit.getText().toString());
                        new GetURLData().execute(urlq);
                        new GetURLData().onOreExecute();
                    }
                }
            }
        });
    }



    class GetRasp extends AsyncTask<String, String, String>{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            for(int ff = -1; ff<2; ff++) {
                String urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + selectedItem_id + "&SearchString=" + selectedItem + "&Type=" + selectedItem_type + "&WeekId=" + (week_id + ff);
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
                    String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                    String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                    for (int j = 0; j < 10; j++) {

                        String predmet_name = null;
                        String predmet_prepod = null;
                        String predmet_group = null;
                        String predmet_podgroup = null;
                        String predmet_aud = null;
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
                        Cursor r = sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + selectedItem_id + " AND r_week_number = " + (week_id + ff) + " AND r_week_day = " + i + " AND r_para_number = " + j + " AND " + " r_search_type = '" + selectedItem_type + "'", null); // SELECT запрос
                        if (r.getCount()==0){// Если даной недели нет в базе
                            ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
                            rowValues.put("r_group_code", selectedItem_id);
                            rowValues.put("r_week_day", i);
                            rowValues.put("r_week_number", (week_id + ff));
                            rowValues.put("r_para_number", j);
                            rowValues.put("r_name", predmet_name);
                            rowValues.put("r_prepod", predmet_prepod);
                            rowValues.put("r_group", predmet_group);
                            rowValues.put("r_podgroup", predmet_podgroup);
                            rowValues.put("r_aud", predmet_aud);
                            rowValues.put("r_razmer", predmet_razmer);
                            rowValues.put("r_week_day_name", predmet_data_ned);
                            rowValues.put("r_week_day_date", predmet_data_chi);
                            rowValues.put("r_search_type", selectedItem_type);
                            rowValues.put("r_last_update", new Date().getTime());
                            sqLiteDatabase.insert("rasp_test1", null, rowValues); // Вставка строки в базу данных
                        }
                    }
                }
            }
            Intent intent = new Intent(MainActivity.this, raspisanie_show.class);
            startActivity(intent);
            return null;
        }
    }

    private class GetURLData extends AsyncTask<String, String, String> { // Класс отвечающий за поиск группы \ аудитории \ преподователя

        protected void onOreExecute() {
            super.onPreExecute();
            result.setText("Поиск группы...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String buffer = "";
                buffer = reader.readLine();

                String jsonString = buffer; // ЭТО JSON со списком групп
                JSONArray obj = new JSONArray(jsonString);
                List<String> group_list = new ArrayList<>();
                List<String> group_list_type = new ArrayList<>();
                List<String> group_list_id = new ArrayList<>();

                for (int ii = 0; ii<obj.length(); ii++){
                    JSONObject guysJSON = obj.getJSONObject(ii);
                    Iterator<?> keys = guysJSON.keys();
                    int i = 0;
                    while(keys.hasNext()) {
                        Object value = guysJSON.get((String)keys.next());
                        if (value != null && i % 4 == 0) {
                            String group_name = (String) value;
                            group_list.add(group_name);
                        }
                        else if (value != null && i % 4 == 1) {
                            String group_type = (String) value;
                            group_list_type.add(group_type);
                        }
                        else if (value != null && i % 4 == 2) {
                            String group_id = value.toString();
                            group_list_id.add(group_id);
                        }
                        i++;
                    }
                }


                MainActivity.this.group_listed = group_list.toArray(new String[0]);
                MainActivity.this.group_listed_type = group_list_type.toArray(new String[0]);
                MainActivity.this.group_listed_id =group_list_id.toArray(new String[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, group_listed);
            listview.setAdapter(adapter);
            result.setText(res);
        }
    }
}