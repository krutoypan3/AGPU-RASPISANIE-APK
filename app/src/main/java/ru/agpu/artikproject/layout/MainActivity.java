package ru.agpu.artikproject.layout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.CustomBackground;
import ru.agpu.artikproject.background_work.GetCurrentWeekDay;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.main_show.EditTextRaspSearch_Listener;
import ru.agpu.artikproject.background_work.main_show.ListViewGroupListener;
import ru.agpu.artikproject.background_work.main_show.TodayClickListener;
import ru.agpu.artikproject.background_work.main_show.tool_bar.MainToolBar;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;
import ru.agpu.artikproject.background_work.main_show.tool_bar.ShowToolBarRecyclerView;
import ru.agpu.artikproject.background_work.main_show.WatchSaveGroupRasp;
import ru.agpu.artikproject.background_work.server.SendInfoToServer;
import ru.agpu.artikproject.background_work.service.PlayService;
import ru.agpu.artikproject.background_work.site_parse.GetCurrentWeekId;
import ru.agpu.artikproject.background_work.site_parse.GetFullGroupList_Online;

public class MainActivity extends AppCompatActivity {

    public EditText rasp_search_edit;
    public static ArrayList<ListViewItems> group_listed;
    public static String[] group_listed_type;
    public static String[] group_listed_id;
    public ListView listview;
    public TextView today;
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
            new ShowToolBarRecyclerView(this, false);
            new WatchSaveGroupRasp(this);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        findViewById(R.id.main_activity_layout).setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.background_darker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

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
        listview = findViewById(R.id.listview);
        today = findViewById(R.id.main_activity_text);

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
        new LoadBuildingsList(this).start(); // Загрузка данных об строениях в адаптер
        week_day = GetCurrentWeekDay.get();

        // Отслеживание нажатий и зажатий на список групп и аудиторий
        new ListViewGroupListener(MainActivity.this, listview);

        startService(new Intent(getApplicationContext(), PlayService.class)); // ЗАПУСК СЛУЖБЫ

        new WatchSaveGroupRasp(this); // Первичный вывод групп которые были открыты ранее

        // Отслеживание изменений текстового поля
        new EditTextRaspSearch_Listener(this, rasp_search_edit);
    }
}