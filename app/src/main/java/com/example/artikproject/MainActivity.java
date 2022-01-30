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
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.artikproject.background_work.CheckAppUpdate;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MainActivity extends AppCompatActivity {

    private EditText rasp_search_edit;
    public static String[] group_listed;
    public static String[] group_listed_type;
    public static String[] group_listed_id;
    protected static ListView listview;
    protected static TextView result;
    protected static TextView subtitle;
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
    private Drawer.Result drawerResult = null;

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
    public void onBackPressed(){
        if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }
        else{
            see_group_rasp();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // Без этих двух строк
        StrictMode.setThreadPolicy(policy); // мы не можем подключиться к базе данных MSSQL так как потокам становится плохо
        animScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animUehalVp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vpravo);
        animUehalVl = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vlevo);
        animRotate_ok = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_ok);
        rasp_search_edit = findViewById(R.id.rasp_search_edit);
        result = findViewById(R.id.result);
        listview = (ListView) findViewById(R.id.listview);
        subtitle = (TextView) findViewById(R.id.subtitle);

        // Handle Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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
        // Создание тулбара слева
        drawerResult = new Drawer()
                .withActivity(this)  // В каком активити создать тулбар
                .withToolbar(toolbar)  // Выбираем сам тулбар
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header) // С заголовком88
                .addDrawerItems(  // Содержимое тулбара
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_location).withIcon(FontAwesome.Icon.faw_location_arrow).withIdentifier(2),
                        new SectionDrawerItem().withName(R.string.drawer_item_settings),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(3),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_delete).withIcon(FontAwesome.Icon.faw_remove).withIdentifier(5),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_github).withIcon(FontAwesome.Icon.faw_question).withIdentifier(6),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    // Обработка клика
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            switch (drawerItem.getIdentifier()) {
                                case (1):
                                    listview.setVisibility(View.VISIBLE);
                                    result.setVisibility(View.VISIBLE);
                                    rasp_search_edit.setVisibility(View.VISIBLE);
                                    listview_aud.setVisibility(View.INVISIBLE);
                                    subtitle.setVisibility(View.VISIBLE);
                                    break;
                                case (2):
                                    listview.setVisibility(View.INVISIBLE);
                                    result.setVisibility(View.INVISIBLE);
                                    rasp_search_edit.setVisibility(View.INVISIBLE);
                                    listview_aud.setVisibility(View.VISIBLE);
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
                                case (3): // Кнопка 'Настройки'
                                    Intent intent = new Intent(MainActivity.this, settings_layout.class);
                                    startActivity(intent);
                                    drawerResult.setSelection(0);
                                    break;
                                case (4):
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases")));
                                    drawerResult.setSelection(0);
                                    break;
                                case (5): // Если нажали на кнопку удаления
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
                                    listview.setAdapter(null);
                                    drawerResult.setSelection(0);
                                    break;
                                case (6):
                                    try { // Проверка обновлений
                                        new CheckAppUpdate(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                    }
                })
                .build();
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setBackgroundResource(R.color.black);

        new CheckAppUpdate(MainActivity.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        float rasnitsa_v_nedelyah = 222.48f; // ВАЖНО!!! ЭТО ЧИСЛО МЫ получаем путем вычитания номера
        // недели с сайта расписания и того, что получается в week_id без "rasnitsa_v_nedelyah"
        // КАЖДЫЙ ГОД ЭТО число изменяется!!! Для 2021 это число "222.48 | В душе не знаю как это
        // число стало дробным. В прошлом году было норм, в этом - каждую среду начиналась новая
        // неделя, хз почему, пришлось так выкручиваться."

        // Получение актуального текущего времени
        long date_ms = new Date().getTime();
        week_id = (int) (date_ms / 1000f / 60f / 60f / 24f / 7f + rasnitsa_v_nedelyah); // Номер текущей недели и
        week_day = new Date(date_ms).getDay() - 1; // дня недели
        if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
            week_day = 0;
        }

        // Отслеживание нажатий на элемент в списке(группа\ауд\препод)
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                selectedItem = group_listed[position];
                selectedItem_type = group_listed_type[position];
                selectedItem_id = group_listed_id[position];
                subtitle.setText(selectedItem);
                Intent intent = new Intent(MainActivity.this, raspisanie_show.class);
                if (isOnline(MainActivity.this)){
                    new GetRaspOnline(selectedItem_id, week_id, getApplicationContext()).execute();
                }
                startActivity(intent);
            }
        });

        sqLiteDatabase = new DataBaseLocal(MainActivity.this).getWritableDatabase(); // Подключение к локальной базе данных
        startService(new Intent(getApplicationContext(), PlayService.class)); // ЗАПУСК СЛУЖБЫ

        see_group_rasp(); // Первичный вывод групп которые были открыты ранее

        // При изменение текстового поля делать:
        rasp_search_edit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {} // До изменения поля
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // После изменения поля
            public void onTextChanged(CharSequence s, int start, int before, int count) { // Во время изменения поля
                if (!(rasp_search_edit.getText().toString().trim().equals(""))) { // Если строка поиска не пустая
                    if (!isOnline(MainActivity.this)){ see_group_rasp(); } // Если нет доступа к интернету то выводить список из бд
                    else {
                        new GetGroupListOnline().execute(rasp_search_edit.getText().toString()); // Получение списка групп из онлайн - бд
                    }
                }
                else{see_group_rasp();}
            }
        });
    }


    // Всё сохраненное расписание
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
            group_listed = group_list.toArray(new String[0]);
            group_listed_type = group_list_type.toArray(new String[0]);
            group_listed_id = group_list_id.toArray(new String[0]);
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
}