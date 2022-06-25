package ru.agpu.artikproject.layout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentGroup;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentWelcome;
import ru.agpu.artikproject.background_work.theme.Theme;

/**
 * Стартовое активити - в нем определяется что будет происходить при нажатии на кнопку назад в
 * разных фрагментах, а так же здесь хранятся различные временные данные необходимые фрагментам
 */
public class StartActivity extends AppCompatActivity {
    public static int FRAGMENT; // Номер открытого фрагмента
    public static String SELECTED_GROUP = ""; // Выбранная группа
    public final static int BACK_TO_GROUP = 2;
    public final static int BACK_TO_WELCOME = 5;

    @Override
    public void onBackPressed(){
        switch (FRAGMENT){
            case(BACK_TO_GROUP):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentGroup.class, null).commit();
                break;
            case(BACK_TO_WELCOME):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentWelcome.class, null).commit();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DataBase_Local.sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase(); // Подключаемся к базе данных
        Theme.setting(this); // Применяем тему к приложению

        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
   }
}
