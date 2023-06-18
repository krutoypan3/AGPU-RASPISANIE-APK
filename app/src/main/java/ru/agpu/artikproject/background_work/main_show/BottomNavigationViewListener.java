package ru.agpu.artikproject.background_work.main_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewTreeViewModelKt;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;
import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentMainShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentNoHaveFavoriteSchedule;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSelectGroupDirectionFaculty;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSettingsShow;
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class BottomNavigationViewListener {
    @SuppressLint("NonConstantResourceId")
    public BottomNavigationViewListener(Activity activity){
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigatin_view);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            // TODO Заглушка, чтобы элементы повторно не отрисовывались при нажатии на нижний бар
        });

        bottomNavigationView.setSelectedItemId(R.id.details_page_Home_page);
        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.details_page_Home_page:
                    if (MainActivity.IS_MAIN_SHOWED) {
                        if (new Random().nextInt(30) == 0) {
                            Ficha_achievements.put(activity, Ficha_achievements.FICHA_GO_TO_HOME);
                            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                            MediaPlayer mp = MediaPlayer.create(activity, R.raw.luntik_i_kloun);
                            mp.start();
                        }
                    }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.details_page_Faculties_list:
                    MainActivity.fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentSelectGroupDirectionFaculty.class, null).commit();
                    break;
                case R.id.details_page_Home_page:
                    MainActivity.IS_MAIN_SHOWED = true;
                    MainActivity.fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentMainShow.class, null).commit();
                    return true;
                case R.id.details_page_Schedule:
                    // Если данные не пустые - показываем расписание по ним
                    if (!Objects.equals(MainActivity.selectedItem, "")) {
                        MainActivity.fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.fragment_container_view, FragmentScheduleShow.class, null).commit();
                    }
                    else { // Иначе - выводим сообщение о том, что тут будет показано расписание
                        MainActivity.fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.fragment_container_view, FragmentNoHaveFavoriteSchedule.class, null).commit();
                    }
                    break;
                case R.id.details_page_Audiences:
                    FragmentRecyclerviewShow.SELECTED_LIST = 1;
                    MainActivity.fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentRecyclerviewShow.class, null).commit();
                    break;
                case R.id.details_page_Settings:
                    // Кнопка 'Настройки'
                    MainActivity.fragmentManager.beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.fragment_container_view, FragmentSettingsShow.class, null).commit();
                    break;
                default:
                    return false;
            }
            MainActivity.IS_MAIN_SHOWED = false;
            MainActivity.FRAGMENT = MainActivity.BACK_TO_MAIN_SHOW;
            return true;
        });
    }
}
