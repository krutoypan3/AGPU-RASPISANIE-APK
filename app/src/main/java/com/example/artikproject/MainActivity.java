package com.example.artikproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.*;


import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        // Асинхронная проверка наличия обновлений
        new Thread(() -> {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0); // Получение текущих значений
                String versionName = packageInfo.versionName; // Названия версии
                int versionCode = packageInfo.versionCode; // И ее кода
                String connectionUrl =
                        "jdbc:sqlserver://sql-serverartem.ddns.net:1433;"
                                + "database=AdventureWorks;"
                                + "user=yourusername@yourserver;"
                                + "password=yourpassword;"
                                + "encrypt=true;"
                                + "trustServerCertificate=false;"
                                + "loginTimeout=30;";  // Данные для подключения к базе данных

                try (Connection connection = DriverManager.getConnection(connectionUrl);
                     Statement statement = connection.createStatement();) {

                    // Выбираем в нашей таблице строку с наибольшим значением кода версии
                    String selectSql = "SELECT version_code, version_name, url_version, whats_new from check_update WHERE version_code = MAX(version_code)";
                    ResultSet resultSet = statement.executeQuery(selectSql);
                    while (resultSet.next()) {
                        int versionCode_db = Integer.parseInt(resultSet.getString(0));
                        String versionName_db = resultSet.getString(1);
                        String versionUrl_db = resultSet.getString(2);
                        String versionNew_db = resultSet.getString(3);
                        if (versionCode != versionCode_db){
                            System.out.println("Доступна новая версия приложения!");
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException | SQLException e) {
                System.out.println("Ошибка при проверке наличия обновлений");
            }
        }).start();

        // Получение актуального текущего времени
        Date date1 = new Date();

        float rasnitsa_v_nedelyah = 222.48f; // ВАЖНО!!! ЭТО ЧИСЛО МЫ получаем путем вычитания номера
        // недели с сайта расписания и того, что получается в week_id без "rasnitsa_v_nedelyah"
        // КАЖДЫЙ ГОД ЭТО число изменяется!!! Для 2021 это число "222.48 | В душе не знаю как это
        // число стало дробным. В прошлом году было норм, в этом - каждую среду начиналась новая
        // неделя, хз почему, пришлось так выкручиваться."

        long date_ms = date1.getTime();
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
                    if (!isOnline(MainActivity.this)){ see_group_rasp(); }
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
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases")));
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

                // Выбор режима (поиск пар\аудиторий)
        String[] countries = {"Расписание", "Аудитории и корпуса"};

        Spinner spinner = (Spinner) findViewById(R.id.vibor_rezhima);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        // Определяем разметку для использования при выборе элемента
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner.setAdapter(adapter);

        ListView listview_aud = findViewById(R.id.listview_aud);
        // Отслеживание нажатий на элемент в списке(ауд)
        listview_aud.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent intent;
                switch (position) {
                    case (1):
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/eQo5R9LdnCprwCvs6"));
                        break;
                    case (2):
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/KwDaKEg3w69a5xuy5"));
                        break;
                    case (3):
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/rtrpMGCP5t3E1FDU8"));
                        break;
                    case (4):
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/DhAqdFucRB5RgF8H6"));
                        break;
                    case (5):
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/m1Ddk1RmebQ9CK5P9"));
                        break;
                    case (6):
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/4fBCRhZPJbc7z4Ng6"));
                        break;
                    default:
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/8Lv4W8uds3cFAeW36")); // 0 нулевое
                        break;
                }
                startActivity(intent);
            }
        });

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Получаем выбранный объект
                String item = (String)parent.getItemAtPosition(position);

                // Здесь мы изменяем на то, что нам нужно))
                switch (item) {
                    case  ("Расписание"):
                        listview.setVisibility(View.VISIBLE);
                        delete_btn.setVisibility(View.VISIBLE);
                        main_button.setVisibility(View.VISIBLE);
                        rasp_search_edit.setVisibility(View.VISIBLE);
                        listview_aud.setVisibility(View.INVISIBLE);
                        favorite.setVisibility(View.VISIBLE);
                        favorite_card.setVisibility(View.VISIBLE);
                        subtitle.setVisibility(View.VISIBLE);
                        break;
                    case ("Аудитории и корпуса"):
                        listview.setVisibility(View.INVISIBLE);
                        delete_btn.setVisibility(View.INVISIBLE);
                        main_button.setVisibility(View.INVISIBLE);
                        rasp_search_edit.setVisibility(View.INVISIBLE);
                        listview_aud.setVisibility(View.VISIBLE);
                        favorite.setVisibility(View.INVISIBLE);
                        favorite_card.setVisibility(View.INVISIBLE);
                        subtitle.setVisibility(View.INVISIBLE);
                        List<String> group_list_aud = new ArrayList<>();
                        group_list_aud.add("Главный корпус  по ул. Р. Люксембург, 159\n" +
                                "Аудитории: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 14а," +
                                " 15, 15а, 16, 17, 18,  21, 22, 23\n");
                        group_list_aud.add("Корпус по ул. Кирова 50 (Заочка)\n" +
                                "Аудитории: 24, 25, 26, 27, 28\n");
                        group_list_aud.add("Корпус по ул. Ленина, 79  (СПФ)\n" +
                                "Аудитории: 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50\n");
                        group_list_aud.add("Корпус по ул. Ефремова, 35 (ЕБД)\n" +
                                "Аудитории: 80, 81, 82, 82а, 83, 84\n");
                        group_list_aud.add("Корпус по ул. П. Осипенко, 83 (ФОК)\n" +
                                "Аудитории: 85, 85а, 86\n");
                        group_list_aud.add("Корпус по ул. П. Комсомольская, 93 (ФТЭиД\\ТЕХФАК)\n" +
                                "Аудитории: 51, 52, 53, 57, 58 а, 58 б, 59, 60, 61, 62, 63, 64," +
                                " 65, 66, 67, 68\n");
                        group_list_aud.add("Корпус по ул. К. Маркса, 49 (Общежитие 1)\n" +
                                "Аудитории: 30, 31, 32, 33, 34, 35, 36, 37, 38, ЛК-1 – ЛК-6, 101," +
                                " 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113," +
                                " 114, 115, 116, 117, 118, 119, 120, 121");
                        String[] group_listed_aud;
                        group_listed_aud = group_list_aud.toArray(new String[0]);
                        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, group_listed_aud);
                        listview_aud.setAdapter(adapter);
                        listview_aud.setVisibility(View.VISIBLE);
                        listview_aud.setBackgroundResource(R.drawable.list_view_favorite);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

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
                try{ connection.connect();}
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
                System.out.println("Исключение MalformedURLException в классе GetURLData");
            } catch (IOException e) {
                System.out.println("Исключение IOException в классе GetURLData");
            } catch (JSONException e) {
                System.out.println("Исключение JSONException в классе GetURLData");
            } finally {
                if (connection != null) connection.disconnect();
                if (reader != null) {
                    try { reader.close(); }
                    catch (IOException e) {
                        System.out.println("Исключение IOException в классе GetURLData");
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