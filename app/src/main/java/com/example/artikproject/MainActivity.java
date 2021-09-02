package com.example.artikproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.*;


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
    private String[] group_listed;
    private String[] group_listed_type;
    private String[] group_listed_id;
    private ListView listview;
    private TextView result;
    private TextView subtitle;
    private Button main_button;
    private static boolean star_toggle = false;
    public static String selectedItem;
    public static String selectedItem_type;
    public static String selectedItem_id;
    public static int week_id;
    public static int week_day;
    public static SQLiteDatabase sqLiteDatabase;
    public static Animation animRotate;
    public static Animation animUehalVp;
    public static Animation animUehalVl;
    public static Animation animScale;
    public static Animation animRotate_ok;

    public static boolean isOnline(Context context){ // Функция определяющая есть ли интернет
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true; // Интернет есть
        }
        return false; // Интернета нет
    }

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        sqLiteDatabase.close();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animUehalVp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vpravo);
        animUehalVl = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vlevo);
        animRotate_ok = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_ok);
        rasp_search_edit = findViewById(R.id.rasp_search_edit);
        main_button = findViewById(R.id.main_button);
        result = findViewById(R.id.result);
        listview = (ListView) findViewById(R.id.listview);
        subtitle = (TextView) findViewById(R.id.subtitle);

        // Получение актуального текущего времени
        Date date1 = new Date();

        int rasnitsa_v_nedelyah = 222; // ВАЖНО!!! ЭТО ЧИСЛО МЫ получаем путем вычитания номера
        // недели с сайта расписания и того, что получается в week_id без "rasnitsa_v_nedelyah"
        // КАЖДЫЙ ГОД ЭТО число изменяется!!! Для 2021 это число "223"

        long date_ms = date1.getTime() + 10800000; // 1080000 это + 3 часа ко дню (чтобы в 9-10 вечера было показано расписание на завтра)
        week_id = (int) (date_ms / 1000f / 60f / 60f / 24f / 7f + rasnitsa_v_nedelyah); // Номер текущей недели
        System.out.println(week_id);
        Date date2 = new Date(date_ms); // дня недели и
        week_day = date2.getDay() - 1;
        if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
            week_day = 0;
            week_id += 1;
        }

        // Отслеживание нажатий на элемент в списке(группа\ауд\препод)
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                selectedItem = MainActivity.this.group_listed[position];
                selectedItem_type = MainActivity.this.group_listed_type[position];
                selectedItem_id = MainActivity.this.group_listed_id[position];
                MainActivity.this.subtitle.setText(selectedItem);
                Cursor r = sqLiteDatabase.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + selectedItem_id + " AND r_week_number = " + week_id, null); // SELECT запрос
                Intent intent = new Intent(MainActivity.this, raspisanie_show.class);
                if (isOnline(MainActivity.this)){
                    new GetRasp(false, MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                startActivity(intent);
            }
        });

        sqLiteDatabase = new DataBase(MainActivity.this).getWritableDatabase(); //Подключение к базе данных
        startService(new Intent(getApplicationContext(), PlayService.class)); //ЗАПУСК СЛУЖБЫ

        see_group_rasp(); // Вывод групп которые были открыты ранее

        // Функция поиска группы или аудитории или преподователя при нажатии на кнопку
        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_button.setAnimation(animScale);
                main_button.setClickable(false);
                if (rasp_search_edit.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.no_user_input, Toast.LENGTH_SHORT).show();
                    result.setText("");
                }
                else {
                    if (!isOnline(MainActivity.this)){
                        see_group_rasp();
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

        // Функция перехода на GitHub при нажатии на кнопку
        ImageView GitHub = findViewById(R.id.GitHub);
        GitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases"));
                startActivity(intent);
            }
        });


        // Функция показа избранных групп при нажатии на кнопку
        ImageView favorite = findViewById(R.id.favorite);
        CardView favorite_card = findViewById(R.id.favorite_card);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite_card.startAnimation(animScale);
                if (!star_toggle) {
                    group_listed = null;
                    Cursor r = sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_selectedItem, r_selectedItem_type FROM rasp_update", null);
                    if (r.moveToFirst()) {
                        List<String> group_list = new ArrayList<>();
                        List<String> group_list_type = new ArrayList<>();
                        List<String> group_list_id = new ArrayList<>();
                        do {
                            switch (r.getString(2)) {
                                case "Group":
                                    group_list.add(r.getString(1).split(",")[0].replace(")", "").replace("(", ""));
                                    break;
                                case "Classroom":
                                case "Teacher":
                                    group_list.add(r.getString(1).split(",")[0]);
                                    break;
                            }
                            group_list_type.add(r.getString(2));
                            group_list_id.add(r.getString(0));
                        } while (r.moveToNext());
                        MainActivity.this.group_listed = group_list.toArray(new String[0]);
                        MainActivity.this.group_listed_type = group_list_type.toArray(new String[0]);
                        MainActivity.this.group_listed_id = group_list_id.toArray(new String[0]);
                    } // Вывод SELECT запроса
                    if (group_listed == null) {
                        result.setText("Вы не отслеживаете изменений в расписании...");
                        listview.setVisibility(View.INVISIBLE);
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, group_listed);
                        listview.setAdapter(adapter);
                        listview.setVisibility(View.VISIBLE);
                        listview.setBackgroundResource(R.drawable.list_view_favorite);
                        result.setText("");
                        subtitle.setText("");
                    }
                    favorite.setBackgroundResource(R.color.yellow);
                }
                else{
                    see_group_rasp();
                    listview.setBackgroundResource(R.drawable.list_view);
                    favorite.setBackgroundResource(R.color.gray);
                }
                star_toggle = !star_toggle;
            }
        });

        // Функция удаления групп
        ImageView delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_btn.startAnimation(animScale);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Удалить все сохраненные расписания?!")
                        .setMessage("Подтвердите удаление!")
                        .setCancelable(false)
                        .setPositiveButton("Отмена",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Удалить все!",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sqLiteDatabase.execSQL("DELETE FROM rasp_test1");
                                sqLiteDatabase.execSQL("DELETE FROM rasp_update");
                                group_listed = null;
                                see_group_rasp();
                                dialog.cancel();
                            }
                        });
                AlertDialog Error = builder.create();
                Error.show();
            }
        });
    }
    public void see_group_rasp(){ // Вывод ранее открываемых групп
        Cursor r;
        r = sqLiteDatabase.rawQuery("SELECT DISTINCT r_group_code, r_group, r_search_type, r_prepod, r_aud FROM rasp_test1 WHERE r_group NOT NULL AND r_prepod NOT NULL AND r_search_type NOT NULL GROUP BY r_group_code", null);
        if (r.moveToFirst()){
            List<String> group_list = new ArrayList<>();
            List<String> group_list_type = new ArrayList<>();
            List<String> group_list_id = new ArrayList<>();
            do{
                switch (r.getString(2)) {
                    case "Group":
                        group_list.add(r.getString(1).split(",")[0].replace(")","").replace("(", ""));
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
            listview.setVisibility(View.INVISIBLE);
        }
        else {
            listview.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, group_listed);
            listview.setAdapter(adapter);
            result.setText("");
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
                connection.setConnectTimeout(5000);
                try{
                connection.connect();}
                catch (Exception e){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Проверьте подключение к интернету", Toast.LENGTH_LONG).show();
                        }
                    });
                    return null;
                }
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
            listview.setVisibility(View.VISIBLE);
            main_button.setClickable(true);
        }
    }
}