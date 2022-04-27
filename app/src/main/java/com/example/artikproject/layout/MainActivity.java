package com.example.artikproject.layout;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import com.example.artikproject.background_work.GetCurrentWeekDay;
import com.example.artikproject.background_work.SetNewBackground;
import com.example.artikproject.background_work.adapters.ListViewItems;
import com.example.artikproject.background_work.main_show.EditTextRaspSearch_Listener;
import com.example.artikproject.background_work.main_show.ListViewGroupListener;
import com.example.artikproject.background_work.main_show.TodayClickListener;
import com.example.artikproject.background_work.service.PlayService;
import com.example.artikproject.background_work.settings_layout.Ficha_achievements;
import com.example.artikproject.background_work.site_parse.GetCurrentWeekId;
import com.example.artikproject.background_work.site_parse.GetFullGroupList_Online;
import com.example.artikproject.background_work.datebase.DataBase_Local;
import com.example.artikproject.background_work.main_show.ShowFullGroupList;
import com.example.artikproject.background_work.main_show.ShowFullWeekList;
import com.example.artikproject.background_work.main_show.ListViewAud_ClickListener;
import com.example.artikproject.background_work.main_show.MainToolBar;
import com.example.artikproject.background_work.main_show.WatchSaveGroupRasp;
import com.example.artikproject.R;
import com.example.artikproject.background_work.CheckAppUpdate;
import com.example.artikproject.background_work.server.SendInfoToServer;
import com.example.artikproject.background_work.theme.Theme;
import com.mikepenz.materialdrawer.Drawer;

public class MainActivity extends AppCompatActivity {

    public static EditText rasp_search_edit;
    public static ArrayList<ListViewItems> group_listed;
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
        sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetNewBackground.setting(findViewById(R.id.main_activity_layout)); // Установка нового фона | Должно быть после setContentView
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // Без этих двух строк
        StrictMode.setThreadPolicy(policy); // мы не можем подключиться к базе данных MSSQL так как потокам становится плохо

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

        // Инициализируем тулбар
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        try { new MainToolBar(MainActivity.this); } // Заполняем тулбар и вызываем его
        catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }

        new CheckAppUpdate(MainActivity.this, false).start(); // Запуск проверки обновлений при входе в приложение
        new SendInfoToServer(MainActivity.this).start(); // Запуск отправки анонимной статистики для отадки ошибок
        new GetCurrentWeekId(MainActivity.this).start(); // Получение номера текущей недели и закидывание списка недель в адаптер
        new GetFullGroupList_Online().start(); // Получение полного списка групп и закидывание их в адаптер
        new TodayClickListener(this, today); // Прослушка нажатий на текущую дату

        week_day = GetCurrentWeekDay.get();

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
        new EditTextRaspSearch_Listener(this, rasp_search_edit);
    }
}