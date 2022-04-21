package com.example.artikproject.background_work.site_parse;

import static com.example.artikproject.layout.MainActivity.sqLiteDatabase;

import android.app.Activity;
import android.database.Cursor;

import com.example.artikproject.background_work.GetCurrentWeekId_Local;
import com.example.artikproject.background_work.adapters.ListViewItems;
import com.example.artikproject.background_work.datebase.MySharedPreferences;
import com.example.artikproject.background_work.main_show.GetWeekFromId;
import com.example.artikproject.layout.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Данный класс получает текущую неделю с сайта,
 * при отсутствии интернета последний сохраненный результат в БД
 */
public class GetCurrentWeekId extends Thread {
    final Activity act;
    /**
     * Получаем тукущую неделю
     * @param act Активити
     */
    public GetCurrentWeekId(Activity act){
        this.act = act;
    }

    @Override
    public void run() {
        try{
            // Получаем последнюю сохраненную неделю из базы данных
            MainActivity.week_id = GetCurrentWeekId_Local.get(act.getApplicationContext());
            // Затем пробуем получить текущую неделю через интернет
            Document doc;
            try {
                String urlq = "http://www.it-institut.ru/SearchString/Index/118";
                doc = Jsoup.connect(urlq).get();
                String today_info = doc.select("div").toString().split("today-info")[1];
                String today = today_info.split("</h5>")[0].split("<h5>")[1]; // Сегодня **.**.****, ******
                act.runOnUiThread(() -> MainActivity.today.setText(today)); // Устанавливаем текущий день на главном экране
                MainActivity.week_id = Integer.parseInt(today_info.split("value=\"")[1].split("\"")[0]); // Получаем текущую неделю с интернета

                MySharedPreferences.put(act.getApplicationContext(), "week_id", MainActivity.week_id);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try { // Получаем информацию о начале и конце недели
                String current_week = String.valueOf(MainActivity.week_id);
                Cursor r = sqLiteDatabase.rawQuery("SELECT * FROM weeks_list WHERE week_id = '" + current_week + "'",null);
                if (r.getCount() == 0){ // При отсутствии недели в базе данных обновляем список недель в базе данных
                    new GetFullWeekList_Online().start(); // Если неделя отсутствует в БД, то обновляем список недель
                }
                else{ // Если неделя есть в базе данных, то заполняем списки информацией из базы данных
                    Cursor f = sqLiteDatabase.rawQuery("SELECT * FROM weeks_list ORDER BY week_id", null);
                    while (f.moveToNext()){
                        GetFullWeekList_Online.weeks_s_po.add(new ListViewItems(f.getString(1) + " " + f.getString(2)));
                        GetFullWeekList_Online.weeks_id.add(f.getString(0));
                    }
                }
            }
            catch (Exception e){
                new GetFullWeekList_Online().start(); // Если неделя отсутствует в БД, то обновляем список недель
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                new GetWeekFromId(act); // Обновляем текущую неделю на главной странице
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
