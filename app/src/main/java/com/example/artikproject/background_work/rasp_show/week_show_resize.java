package com.example.artikproject.background_work.rasp_show;

public class week_show_resize {
    public void size_dec(){
        week_show.table_size--;
        resize();
    }
    public void size_add(){
        week_show.table_size++;
        resize();
    }
    public void resize(){
        for (int i = 0; i < 60; i++){week_show.qqty[i].setTextSize(week_show.table_size); }
        int fk = 0;
        int[] max_razmer = {0,0,0,0,0,0,0};
        for (int ff = 0; ff < 60; ff++){
            if ((ff % 10) == 0 & ff != 0) { fk++; }
            if (week_show.qqty[ff].getText().length() * week_show.table_size/4 > max_razmer[fk]){
                max_razmer[fk] = (int) (week_show.qqty[ff].getText().length() * week_show.table_size/4);
            }
        }
        fk = 0;
        for (int ff = 0; ff < 60; ff++){
            if ((ff % 10) == 0 & ff != 0) { fk++; }
            if (max_razmer[fk] < 120){ max_razmer[fk] = 120; }
            week_show.qqty[ff].setMinHeight(max_razmer[fk]);
        }
    }
}
