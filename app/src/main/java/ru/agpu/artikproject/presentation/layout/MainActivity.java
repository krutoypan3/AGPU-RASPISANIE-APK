package ru.agpu.artikproject.presentation.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CheckAppUpdate;
import ru.agpu.artikproject.background_work.CheckInternetConnection;
import ru.agpu.artikproject.background_work.FirstAppStartHelper;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.main_show.BottomNavigationViewListener;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSelectGroupDirectionFaculty;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;
import ru.agpu.artikproject.background_work.service.PlayService;
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements;
import ru.agpu.artikproject.background_work.site_parse.GetRasp;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.oganesyanartem.core.data.repository.CurrentWeekDayImpl;
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl;
import ru.oganesyanartem.core.domain.repository.CurrentWeekIdRepository;
import ru.oganesyanartem.core.domain.usecase.CurrentWeekDayGetUseCase;
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase;

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
    BottomNavigationView bottomNavigationView;

    public static int FRAGMENT; // Номер открытого фрагмента
    public static boolean IS_MAIN_SHOWED = true;
    public final static int BACK_TO_SELECT_GROUP_DIRECTION_FACULTY = 1;
    public final static int BACK_TO_BUILDINGS_SHOW = 3;
    public final static int BACK_TO_MAIN_SHOW = 2;
    Disposable disposable = null;


    // Вызывается перед выходом из "полноценного" состояния.
    @Override
    public void onDestroy() {
        // Очистите все ресурсы. Это касается завершения работы
        // потоков, закрытия соединений с базой данных и т. д.
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("onKeyMultiple", "kc=" + keyCode);
        new FichaAchievements().playKeysFicha(this, keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        switch (FRAGMENT) {
            case (BACK_TO_SELECT_GROUP_DIRECTION_FACULTY):
                FRAGMENT = BACK_TO_MAIN_SHOW;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentSelectGroupDirectionFaculty.class, null).commit();
                break;
            case (BACK_TO_MAIN_SHOW):
                if (!IS_MAIN_SHOWED) {
                    IS_MAIN_SHOWED = true;
                    bottomNavigationView.setSelectedItemId(R.id.details_page_Home_page);
                }
                break;
            case (BACK_TO_BUILDINGS_SHOW):
                FRAGMENT = BACK_TO_MAIN_SHOW;
                FragmentRecyclerviewShow.Companion.setSELECTED_LIST(1);
                getSupportFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .replace(R.id.fragment_container_view, FragmentRecyclerviewShow.class, null).commit();
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBaseSqlite.Companion.getSqliteDatabase(getApplicationContext());
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


        week_day = new CurrentWeekDayGetUseCase(new CurrentWeekDayImpl()).execute();

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigatin_view);
        new BottomNavigationViewListener(this); // Слушатель нажатий на нижний тулбар
        new CheckAppUpdate(this, false).start(); // Запуск проверки обновлений при входе в приложение
        new LoadBuildingsList(this).start(); // Загрузка данных об строениях в адаптер

        startService(new Intent(getApplicationContext(), PlayService.class)); // ЗАПУСК СЛУЖБЫ

        // Выгружаем данные о последнем открытом расписании в главное активити
        selectedItem = MySharedPreferences.get(getApplicationContext(), "selectedItem", "");
        selectedItem_type = MySharedPreferences.get(getApplicationContext(), "selectedItem_type", "");
        selectedItem_id = MySharedPreferences.get(getApplicationContext(), "selectedItem_id", "");


        Observable<Integer> observable = Observable.create(subscriber -> { // Создаем observable, который будет выполняться в отдельном потоке
            CurrentWeekIdRepository currentWeekIdRepository = new CurrentWeekIdImpl(getApplicationContext());
            CurrentWeekIdGetUseCase currentWeekIdGetUseCase = new CurrentWeekIdGetUseCase(currentWeekIdRepository);
            subscriber.onNext(currentWeekIdGetUseCase.execute());
            subscriber.onComplete();
        });

        disposable = observable
                .subscribeOn(Schedulers.newThread()) // Выбираем ядро на котором будет выполняться наш observable
                .observeOn(AndroidSchedulers.mainThread()) // Выбираем ядро на котором будет выполняться наш код после observable
                .subscribe(week_id -> { // Код, выполняющийся после observable
                            MainActivity.week_id = week_id;
                        }
                );


        if (getIntent().getBooleanExtra("start_rasp", false)) {
            if (CheckInternetConnection.INSTANCE.getState(getApplicationContext())) {
                selectedItem = getIntent().getStringExtra("selectedItem");
                selectedItem_type = getIntent().getStringExtra("selectedItem_type");
                selectedItem_id = getIntent().getStringExtra("selectedItem_id");
                new GetRasp(selectedItem_id, selectedItem_type, selectedItem, week_id, getApplicationContext(), null).start();
            }
        }

        if (!MySharedPreferences.get(getApplicationContext(), "IsFirstAppStart", true) && !Objects.equals(selectedItem, "")) {
            IS_MAIN_SHOWED = false;
            FRAGMENT = BACK_TO_MAIN_SHOW;
            bottomNavigationView.setSelectedItemId(R.id.details_page_Schedule);
            fragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container_view, FragmentScheduleShow.class, null).commit();
        }
        findViewById(R.id.main_app_text).setOnClickListener(v -> {
            if (IS_MAIN_SHOWED)
                new FirstAppStartHelper(this);
        });
    }
}