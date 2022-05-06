package ru.agpu.artikproject.background_work.rasp_show;

public class Week_show_resize {
    /**
     * Класс отвечающий за уменьшение размера расписания в недельном режиме
     */
    public void size_dec(){
        Week_show.table_size--;
        resize();
    }
    /**
     * Класс отвечающий за увеличение размера расписания в недельном режиме
     */
    public void size_add(){
        Week_show.table_size++;
        resize();
    }
    /**
     * Класс отвечающий за обновление измененного размера расписания в недельном режиме
     */
    public void resize(){
        for (int i = 0; i < 60; i++){
            Week_show.qqty[i].setTextSize(Week_show.table_size); }
        int fk = 0;
        int[] max_razmer = {0,0,0,0,0,0,0};
        for (int ff = 0; ff < 60; ff++){
            if ((ff % 10) == 0 & ff != 0) { fk++; }
            if (Week_show.qqty[ff].getText().length() * Week_show.table_size/4 > max_razmer[fk]){
                max_razmer[fk] = Week_show.qqty[ff].getText().length() * Week_show.table_size/4;
            }
        }
        fk = 0;
        for (int ff = 0; ff < 60; ff++){
            if ((ff % 10) == 0 & ff != 0) { fk++; }
            if (max_razmer[fk] < 120){ max_razmer[fk] = 120; }
            Week_show.qqty[ff].setMinHeight(max_razmer[fk]);
        }
    }
}
