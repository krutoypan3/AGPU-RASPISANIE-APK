package com.example.artikproject;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    private EditText rasp_search_edit;
    private Button main_button;
    private TextView result;
    private String[] group_listed;
    private String[] group_listed_id;
    private ArrayAdapter<String> adapter;
    private ListView listview;
    private TextView subtitle;
    static public String selectedItem;
    static public String selectedItem_id;
    public static String[][][] daysp;
    public static String[][][][] daysp3;
    public static  String[][] daysp_time;
    public static String[][][] daysp_time3;
    public static int week_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Date date1 = new Date();
        long date_ms = date1.getTime() + 10800000;
        week_id = (int) ((date_ms - 18489514000f) / 1000f / 60f / 60f / 24f / 7f); // Номер текущей недели
        listview = (ListView) findViewById(R.id.listview);
        subtitle = (TextView) findViewById(R.id.subtitle);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                selectedItem = MainActivity.this.group_listed[position];
                selectedItem_id = MainActivity.this.group_listed_id[position];
                MainActivity.this.subtitle.setText(selectedItem);
                new GetRasp().execute(selectedItem);
            }
        });
        rasp_search_edit = findViewById(R.id.rasp_search_edit);
        main_button = findViewById(R.id.main_button);
        result = findViewById(R.id.result);

        main_button.setOnClickListener(new View.OnClickListener() { // Функция поиска группы или аудитории или преподователя при нажатии на кнопку
            @Override
            public void onClick(View v) {
                if (rasp_search_edit.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_SHORT).show();
                    result.setText("");
                }
                else {
                    String urlq = "https://www.it-institut.ru/SearchString/KeySearch?Id=118&SearchProductName=" + rasp_search_edit.getText().toString();
                    result.setText(rasp_search_edit.getText().toString());
                    new GetURLData().execute(urlq);
                    new GetURLData().onOreExecute();
                }
            }
        });
    }



    class GetRasp extends AsyncTask<String, String, String>{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            daysp3 = new String[3][6][10][5];
            daysp_time3 = new String[3][6][2];
            for(int ff = -1; ff<2; ff++) {
                String urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + selectedItem_id + "&SearchString=" + selectedItem + "&Type=Group&WeekId=" + (week_id + ff);
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
                daysp = new String[6][10][5];
                daysp_time = new String[6][2];
                for (int i = 0; i < 6; i++) {
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
                        daysp[(i)][(j)] = new String[]{predmet_name, predmet_prepod, predmet_group, predmet_podgroup, predmet_aud, predmet_razmer};
                    }
                    String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                    String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                    daysp_time[i] = new String[]{predmet_data_ned, predmet_data_chi};
                }
                daysp3[ff+1] = daysp;
                daysp_time3[ff+1] = daysp_time;
            }
            daysp_time = daysp_time3[1];
            daysp = daysp3[1];
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
                        else if (value != null && i % 4 == 2) {
                            String group_id = value.toString();
                            group_list_id.add(group_id);
                        }
                        i++;
                    }
                }


                MainActivity.this.group_listed = group_list.toArray(new String[0]);
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
            adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, group_listed);
            listview.setAdapter(adapter);
            result.setText(res);
        }
    }
}