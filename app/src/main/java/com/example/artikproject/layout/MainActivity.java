package com.example.artikproject.layout;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.Objects;

import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.main_show.ListViewGroupListener;
import com.example.artikproject.background_work.site_parse.GetCurrentWeekId;
import com.example.artikproject.background_work.site_parse.GetFullGroupList_Online;
import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.main_show.ShowFullGroupList;
import com.example.artikproject.background_work.main_show.ShowFullWeekList;
import com.example.artikproject.background_work.site_parse.GetGroupList_Search;
import com.example.artikproject.background_work.main_show.ListViewAud_ClickListener;
import com.example.artikproject.background_work.main_show.MainToolBar;
import com.example.artikproject.background_work.main_show.WatchSaveGroupRasp;
import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckAppUpdate;
import com.example.artikproject.background_work.server.SendInfoToServer;
import com.example.artikproject.background_work.service.PlayService;
import com.mikepenz.materialdrawer.Drawer;

public class MainActivity extends AppCompatActivity {

    public static EditText rasp_search_edit;
    public static String[] group_listed;
    public static String[] group_listed_type;
    public static String[] group_listed_id;
    public static ListView listview;
    public static TextView result;
    public static TextView today;
    public static TextView current_week;
    public static String selectedItem;
    public static String selectedItem_type;
    public static String selectedItem_id;
    public static int week_id;
    public static int week_day;
    public static Animation animRotate;
    public static Animation animUehalVp;
    public static Animation animUehalVl;
    public static Animation animScale;
    public static Animation animRotate_ok;
    public static Drawer.Result drawerResult = null;
    public static Toolbar toolbar = null;
    public static ListView listview_aud = null;
    public static Button list_groups = null;
    public static Button list_weeks = null;
    public static SQLiteDatabase sqLiteDatabase;

    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }
        else{
            MainToolBar.show_group_list();
            new WatchSaveGroupRasp(getApplicationContext());
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
        listview = findViewById(R.id.listview);
        today = findViewById(R.id.main_activity_text);
        current_week = findViewById(R.id.subtitle);
        list_groups = findViewById(R.id.list_groups);
        listview_aud = findViewById(R.id.listview_aud);
        list_weeks = findViewById(R.id.list_weeks);
        sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();

        // Инициализируем тулбар
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        try { new MainToolBar(MainActivity.this); } // Заполняем тулбар и вызываем его
        catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }

        new CheckAppUpdate(MainActivity.this).start(); // Запуск проверки обновлений при входе в приложение
        new SendInfoToServer(MainActivity.this).start(); // Запуск отправки анонимной статистики для отадки ошибок
        new GetCurrentWeekId(MainActivity.this).start(); // Получение номера текущей недели и закидывание списка недель в адаптер
        new GetFullGroupList_Online(getApplicationContext()).start(); // Получение полного списка групп и закидывание их в адаптер

        // Получение актуального текущего времени
        long date_ms = new Date().getTime();// Нужно ли це переменная?
        week_day = new Date(date_ms).getDay() - 1; // дня недели
        if (week_day == -1){ // Если будет воскресенье, то будет показан понедельник
            week_day = 0;
        }

        // Отслеживание нажатий на элемент в списке(ауд)
        listview_aud.setOnItemClickListener((parent, v, position, id) ->
                new ListViewAud_ClickListener(position, MainActivity.this));
        // Отслеживание нажатий на кнопку списка групп
        list_groups.setOnClickListener((v) ->
                new ShowFullGroupList(MainActivity.this, v).start());
        // Отслеживание нажатий на кнопку списка недель
        list_weeks.setOnClickListener((v) ->
                new ShowFullWeekList(MainActivity.this, v).start());

        // Отслеживание нажатий и зажатий на список групп и аудиторий
        new ListViewGroupListener(MainActivity.this, listview);

        startService(new Intent(getApplicationContext(), PlayService.class)); // ЗАПУСК СЛУЖБЫ

        new WatchSaveGroupRasp(getApplicationContext()); // Первичный вывод групп которые были открыты ранее

        // Отслеживание изменений текстового поля
        rasp_search_edit.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {} // До изменения поля
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // После изменения поля
            public void onTextChanged(CharSequence s, int start, int before, int count) { // Во время изменения поля
                if (!(rasp_search_edit.getText().toString().trim().equals(""))) { // Если строка поиска не пустая
                    if (!CheckInternetConnection.getState(getApplicationContext())){ new WatchSaveGroupRasp(getApplicationContext()); } // Если нет доступа к интернету то выводить список из бд
                    else {
                        String urlq = "https://www.it-institut.ru/SearchString/KeySearch?Id=118&SearchProductName=" + rasp_search_edit.getText().toString();
                        new GetGroupList_Search(urlq, MainActivity.this).start(); // Отправляем запрос на сервер и выводим получившийся список
                    }
                }
                else{new WatchSaveGroupRasp(getApplicationContext());}
            }
        });
    }
}