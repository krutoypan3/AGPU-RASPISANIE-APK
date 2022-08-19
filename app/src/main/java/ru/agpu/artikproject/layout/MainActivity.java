package ru.agpu.artikproject.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.FirstAppStartHelper;
import ru.agpu.artikproject.background_work.GetCurrentWeekDay;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentMainShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentNoHaveFavoriteSchedule;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSelectGroupDirectionFaculty;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSettingsShow;
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
    public static FragmentManager fragmentManager;
    public static BottomNavigationView bottomNavigationView;

    public static int FRAGMENT; // Номер открытого фрагмента
    public static boolean IS_MAIN_SHOWED = true;
    public final static int BACK_TO_SELECT_GROUP_DIRECTION_FACULTY = 1;
    public final static int BACK_TO_BUILDINGS_SHOW = 3;
    public final static int BACK_TO_MAIN_SHOW = 2;


    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy(){
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    public void onBackPressed(){
        switch (FRAGMENT){
            case(BACK_TO_SELECT_GROUP_DIRECTION_FACULTY):
                FRAGMENT = BACK_TO_MAIN_SHOW;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectGroupDirectionFaculty.class, null).commit();
                break;
            case(BACK_TO_MAIN_SHOW):
                if (!IS_MAIN_SHOWED){
                    IS_MAIN_SHOWED = true;
                    bottomNavigationView.setSelectedItemId(R.id.details_page_Home_page);
                }
                break;
            case (BACK_TO_BUILDINGS_SHOW):
                FRAGMENT = BACK_TO_MAIN_SHOW;
                FragmentRecyclerviewShow.SELECTED_LIST = 1;
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.fragment_container_view, FragmentRecyclerviewShow.class, null).commit();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
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


        week_day = GetCurrentWeekDay.get();

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            // TODO Заглушка, чтобы элементы повторно не отрисовывались при нажатии на нижний бар
        });

        bottomNavigationView.setSelectedItemId(R.id.details_page_Home_page);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.details_page_Faculties_list:
                    fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentSelectGroupDirectionFaculty.class, null).commit();
                    break;
                case R.id.details_page_Home_page:
                    IS_MAIN_SHOWED = true;
                    fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentMainShow.class, null).commit();
                    return true;
                case R.id.details_page_Schedule:
                    // Если данные не пустые - показываем расписание по ним
                    if (!Objects.equals(MainActivity.selectedItem, "")) {
                        fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.fragment_container_view, FragmentScheduleShow.class, null).commit();
                    }
                    else { // Иначе - выводим сообщение о том, что тут будет показано расписание
                        fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.fragment_container_view, FragmentNoHaveFavoriteSchedule.class, null).commit();
                    }
                    break;
                case R.id.details_page_Audiences:
                    FragmentRecyclerviewShow.SELECTED_LIST = 1;
                    fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentRecyclerviewShow.class, null).commit();
                    break;
                case R.id.details_page_Settings:
                    // Кнопка 'Настройки'
                    fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentSettingsShow.class, null).commit();
                    break;
                default:
                    return false;
            }
            IS_MAIN_SHOWED = false;
            FRAGMENT = BACK_TO_MAIN_SHOW;
            return true;
        });

        new CheckAppUpdate(this, false).start(); // Запуск проверки обновлений при входе в приложение
        new LoadBuildingsList(this).start(); // Загрузка данных об строениях в адаптер
        new GetCurrentWeekId(this).start(); // Получение номера текущей недели и закидывание списка недель в адаптер

        startService(new Intent(getApplicationContext(), PlayService.class)); // ЗАПУСК СЛУЖБЫ

        // Выгружаем данные о последнем открытом расписании в главное активити
        selectedItem = MySharedPreferences.get(getApplicationContext(), "selectedItem", "");
        selectedItem_type = MySharedPreferences.get(getApplicationContext(), "selectedItem_type", "");
        selectedItem_id = MySharedPreferences.get(getApplicationContext(), "selectedItem_id", "");

        if (getIntent().getBooleanExtra("start_rasp", false)){
            if (CheckInternetConnection.getState(getApplicationContext())){
                MainActivity.selectedItem = getIntent().getStringExtra("selectedItem");
                MainActivity.selectedItem_type = getIntent().getStringExtra("selectedItem_type");
                MainActivity.selectedItem_id = getIntent().getStringExtra("selectedItem_id");
                new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, getApplicationContext()).start();
            }
        }

        if (!MySharedPreferences.get(getApplicationContext(), "IsFirstAppStart", true) && !Objects.equals(selectedItem, "")){
            IS_MAIN_SHOWED = false;
            FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW;
            bottomNavigationView.setSelectedItemId(R.id.details_page_Schedule);
            fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container_view, FragmentScheduleShow.class, null).commit();
        }
        findViewById(R.id.main_app_text).setOnClickListener(v -> new FirstAppStartHelper(this));
    }
}