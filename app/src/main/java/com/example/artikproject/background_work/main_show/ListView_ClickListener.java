package com.example.artikproject.background_work.main_show;

import android.app.Activity;
import android.content.Intent;

import com.example.artikproject.background_work.CheckInternetConnection;
import com.example.artikproject.background_work.rasp_show.GetRasp;
import com.example.artikproject.layout.MainActivity;
import com.example.artikproject.layout.Raspisanie_show;

public class ListView_ClickListener {
    /**
     * Слушатель нажатия на список групп
     * @param position Позиция выбранного элелемента
     * @param act Активити
     */
    public ListView_ClickListener(int position, Activity act){
        MainActivity.selectedItem = MainActivity.group_listed[position];
        MainActivity.selectedItem_type = MainActivity.group_listed_type[position];
        MainActivity.selectedItem_id = MainActivity.group_listed_id[position];
        MainActivity.subtitle.setText(MainActivity.selectedItem);
        Intent intent = new Intent(act.getApplicationContext(), Raspisanie_show.class);
        if (CheckInternetConnection.getState(act.getApplicationContext())){
            new GetRasp(MainActivity.selectedItem_id, MainActivity.selectedItem_type, MainActivity.selectedItem, MainActivity.week_id, act.getApplicationContext()).start();
        }
        act.startActivity(intent);
    }
}
