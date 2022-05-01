package com.example.artikproject.background_work.rasp_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.ListView;

import com.example.artikproject.background_work.OnSwipeTouchListener;


public class ListViewDayPara_Listener {
    /**
     * Слушатель нажатия на список групп
     * @param listView Список с парами
     * @param act Активити
     */
    @SuppressLint("ClickableViewAccessibility")
    public ListViewDayPara_Listener(Activity act, ListView listView){

        // отслеживание жестов на списке с расписанием расписании
        listView.setOnTouchListener(new OnSwipeTouchListener(act.getApplicationContext()) {
            public void onSwipeRight() { new Swipe_rasp("Left", act); }
            public void onSwipeLeft() { new Swipe_rasp("Right", act); }
        });

        // Отслеживание нажатий на пару в списке
        listView.setOnItemClickListener((parent, v, position, id) ->
                new Para_info(position, act));
    }
}
