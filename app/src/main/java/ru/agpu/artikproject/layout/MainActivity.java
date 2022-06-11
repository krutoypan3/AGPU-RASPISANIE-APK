package ru.agpu.artikproject.layout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.GetCurrentWeekDay;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentMainShow;
import ru.agpu.artikproject.background_work.main_show.tool_bar.MainToolBar;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;
import ru.agpu.artikproject.background_work.service.PlayService;
import ru.agpu.artikproject.background_work.site_parse.GetCurrentWeekId;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.background_work.theme.CustomBackground;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<ListViewItems> group_listed;
    public static String[] group_listed_type;
    public static String[] group_listed_id;
    public static String selectedItem;
    public static String selectedItem_type;
    public static String selectedItem_id;
    public static int week_id;
    public static int week_day;
    public static Animation animRotate;
    public static Animation animUehalVp;
    public static Animation animUehalVl;
    public static Animation animUehalVlPriehalSprava;
    public static Animation animUehalVpPriehalSleva;
    public static Animation animPriehalSprava;
    public static Animation animUehalVpravo;
    public static Animation animPriehalSleva;
    public static Animation animScale;
    public static Animation animRotate_ok;
    public static Drawer.Result drawerResult = null;
    public static Toolbar toolbar = null;
    public static FragmentManager fragmentManager;


    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        if (drawerResult.isDrawerOpen()){ // Если тулбар открыт
            drawerResult.closeDrawer(); // Закрываем его
        }
        MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container_view, FragmentMainShow.class, null).commit();
        drawerResult.setSelection(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBase_Local.sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        findViewById(R.id.main_activity_layout).setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.background_darker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // Без этих двух строк
        StrictMode.setThreadPolicy(policy); // мы не можем подключиться к базе данных MSSQL так как потокам становится плохо

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Инициализируем анимации
        animScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animUehalVp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vpravo);
        animUehalVl = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vlevo);
        animUehalVpravo = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vpravo_daleko);
        animUehalVlPriehalSprava = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vlevo_priehal_sprava);
        animUehalVpPriehalSleva = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uehal_vpravo_priehal_sleva);
        animPriehalSprava = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.priehal_sprava);
        animPriehalSleva = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.priehal_sleva);
        animRotate_ok = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_ok);

        // Инициализируем тулбар
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        try { new MainToolBar(MainActivity.this); } // Заполняем тулбар и вызываем его
        catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }

        week_day = GetCurrentWeekDay.get();

        new CheckAppUpdate(this, false).start(); // Запуск проверки обновлений при входе в приложение
        new LoadBuildingsList(this).start(); // Загрузка данных об строениях в адаптер
        new GetCurrentWeekId(this).start(); // Получение номера текущей недели и закидывание списка недель в адаптер

        startService(new Intent(getApplicationContext(), PlayService.class)); // ЗАПУСК СЛУЖБЫ

        if (getIntent().getBooleanExtra("start_rasp", false)){
            Intent intent2 = new Intent(getApplicationContext(), Raspisanie_show.class);
            if (CheckInternetConnection.getState(getApplicationContext())){
                MainActivity.selectedItem = getIntent().getStringExtra("selectedItem");
                MainActivity.selectedItem_type = getIntent().getStringExtra("selectedItem_type");
                MainActivity.selectedItem_id = getIntent().getStringExtra("selectedItem_id");
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
            }
            startActivity(intent2);
        }

        fragmentManager = getSupportFragmentManager();
    }
}