package ru.agpu.artikproject.layout;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentAuthorizationEIOS;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentEIOS;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentFormOfTraining;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentGroup;
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentWelcome;
import ru.agpu.artikproject.background_work.theme.Theme;

public class StartActivity extends AppCompatActivity {
    public static String FORM_OF_TRAINING;
    public static int FRAGMENT;
    public final static int BACK_TO_FORM_OF_TRAINING = 4;
    public final static int BACK_TO_EIOS = 3;
    public final static int BACK_TO_GROUP = 2;
    public final static int BACK_TO_WELCOME = 5;
    public final static int BACK_TO_AUTHOTIZATION = 7;

    @Override
    public void onBackPressed(){
        switch (FRAGMENT){
            case(BACK_TO_GROUP):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentGroup.class, null).commit();
                break;
            case(BACK_TO_EIOS):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentEIOS.class, null).commit();
                break;
            case(BACK_TO_FORM_OF_TRAINING):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentFormOfTraining.class, null).commit();
                break;
            case(BACK_TO_WELCOME):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentWelcome.class, null).commit();
                break;
            case(BACK_TO_AUTHOTIZATION):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentAuthorizationEIOS.class, null).commit();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainActivity.sqLiteDatabase = new DataBase_Local(getApplicationContext()).getWritableDatabase();
        Theme.setting(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

   }
}
