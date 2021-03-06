package ru.agpu.artikproject.background_work.rasp_show;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.background_work.theme.GetTextColor;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class Week_show {
    public TableRow[] tableRows;
    public static TextView[] qqty;
    public static int table_size = 14;

    /**
     * Класс отвечающий за показ расписания в недельном режиме
     * @param context Контекст приложения
     */
    public Week_show(Context context){
        try{
            Cursor r = DataBase_Local.sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE " +
                    "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                    "r_week_number = " + MainActivity.week_id + " ORDER BY r_week_day, r_para_number", null);
            Cursor f = DataBase_Local.sqLiteDatabase.rawQuery("SELECT DISTINCT r_razmer FROM raspisanie WHERE " +
                    "r_group_code = " + MainActivity.selectedItem_id + " AND " +
                    "r_week_number = " + MainActivity.week_id + " ORDER BY r_week_day, r_para_number", null);
            if (r.getCount()!=0) {
                String prev_time = "";
                Raspisanie_show.week_para_view.removeAllViews();
                TableRow tableRow = new TableRow(context); // Новый столбец
                String str = "";
                TextView qty; // Новая ячейка
                TextView empty_cell; // Новая пустая ячейка
                qqty = new TextView[60];
                tableRows = new TableRow[6];
                int ff = 0;
                int fk = 0;
                r.moveToFirst();
                String mainText = r.getString(10) + " " + r.getString(11);
                Raspisanie_show.mainText.setText(mainText);
                f.moveToFirst();
                TableRow timeRow = new TableRow(context); // Новый столбец
                do{
                    qty = new TextView(context); // Новая ячейка
                    qty.setMaxEms(10);
                    qty.setTextSize(table_size);
                    qty.setPadding(5,5,5,5);
                    qty.setTextColor(GetTextColor.getAppColor(context));
                    qty.setBackgroundResource(R.drawable.table_granitsa_legenda);
                    qty.setGravity(Gravity.CENTER);
                    qty.setText(f.getString(0));
                    empty_cell = new TextView(context); // Новая пустая ячейка
                    empty_cell.setHeight(1);
                    empty_cell.setWidth(1);
                    timeRow.addView(empty_cell); // Добавление пустой ячейки в столбец
                    timeRow.addView(qty);
                }while(f.moveToNext());
                Raspisanie_show.week_para_view.addView(timeRow);
                do{
                    if (Objects.equals(prev_time, r.getString(9))){
                        str += "\n";
                        tableRow.removeView(qty);
                        tableRow.removeView(empty_cell);
                    }
                    else{
                        str = "";
                        qty = new TextView(context); // Новая ячейка
                        qty.setMaxEms(10);
                        qty.setTextSize(table_size);
                        qty.setPadding(0,5,5,5);
                        qty.setTextColor(ContextCompat.getColor(context, R.color.black));
                        try {
                            qty.setBackgroundColor(Color.parseColor(r.getString(14)));
                        }
                        catch (Exception e){
                            qty.setBackgroundResource(R.color.gray);
                        }
                        qty.setGravity(Gravity.CENTER);
                    }
                    prev_time = r.getString(9);
                    if (r.getString(4) != null){
                        str += r.getString(4) + "\n";
                        if (r.getString(5) != null) str += r.getString(5) + "\n";
                        if (r.getString(6) != null) str += r.getString(6) + "\n";
                        if (r.getString(7) != null) str += r.getString(7) + "\n";
                        if (r.getString(8) != null) str += r.getString(8) + "\n";
                        if (r.getString(15) != null) str += r.getString(15) + "\n";
                    }
                    if (r.getString(3).equals("0")){
                        tableRow = new TableRow(context); // Новый пустой столбец
                        empty_cell = new TextView(context); // Новая пустая ячейка
                        empty_cell.setHeight(1);
                        tableRow.addView(empty_cell); // Добавление пустой ячейки в столбец
                        Raspisanie_show.week_para_view.addView(tableRow); // Добавление столбца в таблицу
                        tableRow = new TableRow(context); // Новый столбец
                        tableRow.setGravity(Gravity.CENTER_VERTICAL);
                        str = r.getString(10);
                        str += "\n" + (r.getString(11));
                        Raspisanie_show.week_para_view.addView(tableRow); // Добавление столбца в таблицу
                        tableRows[fk] = tableRow;
                        fk++;
                        try {
                            qty.setBackgroundColor(Color.parseColor(r.getString(14)));
                        }
                        catch (Exception e){
                            qty.setBackgroundResource(R.drawable.table_granitsa_legenda);
                        }
                        qty.setTextColor(GetTextColor.getAppColor(context));
                    }
                    qty.setText(str);
                    empty_cell = new TextView(context); // Новая пустая ячейка
                    empty_cell.setHeight(1);
                    empty_cell.setWidth(1);
                    tableRow.addView(empty_cell); // Добавление пустой ячейки в столбец
                    tableRow.addView(qty); // Добавление ячейки в столбец
                    qqty[ff] = qty;
                    ff++;
                }while(r.moveToNext());
                tableRow.removeView(qty); // Удаление последней ячейки(хз что за ячейка (мусор какой-то))
                new Week_show_resize().resize();
                Raspisanie_show.mainText.setText("");
            }
        }
        catch (Exception e){ // Если недели нет в базе то ...
            e.printStackTrace();
            Raspisanie_show.mainText.setText(R.string.rasp_error);
        }
    }
}
