package ru.agpu.artikproject.background_work.site_parse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.ShowNotification;
import ru.agpu.artikproject.background_work.datebase.DataBase_Local;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.Raspisanie_show;

public class GetRasp extends Thread {
    final String r_selectedItem;
    final String r_selectedItem_id;
    final String r_selectedItem_type;
    final int week_id_upd;
    final Context context;
    String predmet_name;
    String predmet_prepod;
    String predmet_group;
    String predmet_podgroup;
    String predmet_aud;
    String predmet_razmer;
    String predmet_color;
    String predmet_distant;
    String predmet_time;
    String predmet_data_ned;
    String predmet_data_chi;
    String type = "";

    /**
     * Класс отвечающий за обновление расписание и поика изменений в старом
     * @param context Контекст приложения
     * @param r_selectedItem Название выбранной группы \ препода \ аудитории
     * @param r_selectedItem_id ID выбранной группы \ препода \ аудитории
     * @param r_selectedItem_type Тип выбранной (группы \ препода \ аудитории)
     * @param week_id_upd ID недели, расписание которой нужно получить
     */
    public GetRasp(String r_selectedItem_id, String r_selectedItem_type, String r_selectedItem, int week_id_upd, Context context) {
        this.r_selectedItem_id = r_selectedItem_id;
        this.r_selectedItem_type = r_selectedItem_type;
        this.r_selectedItem = r_selectedItem;
        this.week_id_upd = week_id_upd;
        this.context = context;
    }

    public GetRasp(String r_selectedItem_id, String r_selectedItem_type, String r_selectedItem, int week_id_upd, Context context, String type) {
        this.r_selectedItem_id = r_selectedItem_id;
        this.r_selectedItem_type = r_selectedItem_type;
        this.r_selectedItem = r_selectedItem;
        this.week_id_upd = week_id_upd;
        this.context = context;
        this.type = type;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void run() {
        //if (type.equals("CheckRaspChanges")){Raspisanie_show.refresh_on_off = false;}
        if (!Raspisanie_show.refresh_on_off) {
            Raspisanie_show.refresh_on_off = true;
            System.out.println("Был сделан запрос на обновление расписания для " + r_selectedItem);
            try{
                Document doc;
                for (int ff = -1; ff < 2; ff++) {
                    String urlq;
                    urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + r_selectedItem_id + "&SearchString=" + r_selectedItem + "&Type=" + r_selectedItem_type + "&WeekId=" + (week_id_upd + ff);
                    try {
                        doc = Jsoup.connect(urlq).get();
                    } catch (IOException e) { // Прерывание функции, если нет интернета
                        Raspisanie_show.refresh_on_off = false;
                        Raspisanie_show.refresh_successful = false;
                        return;
                    }
                    List<String[]> days = new ArrayList<>();
                    for (int i = 1; i < 7; i++) {
                        days.add(doc.select("tbody").toString().split("th scope")[i].split("td colspan="));
                    }
                    String[] day_data_razmer = new String[7];
                    String[] day_data_time = new String[7];
                    String[] dataaa = doc.select("thead").toString().split("colspan=\"");
                    for (int i = 1; i < 8; i++) {
                        day_data_razmer[i - 1] = (dataaa[i].split("\"")[0]);
                        day_data_time[i - 1] = (dataaa[i].split(">")[2].split("<")[0]);
                    }
                    String[][] day;
                    day = days.toArray(new String[0][0]);
                    for (int i = 0; i < 6; i++) {
                        boolean prohod = false;
                        int schet = 0;
                        predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                        predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                        for (int j = 0; j < 10; j++) {
                            predmet_name = null;
                            predmet_prepod = null;
                            predmet_group = null;
                            predmet_podgroup = null;
                            predmet_aud = null;
                            predmet_razmer = null;
                            predmet_color = null;
                            predmet_distant = null;
                            try {
                                predmet_razmer = day[i][j].split("\"")[1];
                                predmet_name = day[i][j].split("<span>")[1].split("</span>")[0];
                                predmet_prepod = day[i][j].split("<span>")[2].split("</span>")[0];
                                predmet_group = day[i][j].split("<span>")[3].split("</span>")[0];
                                predmet_podgroup = day[i][j].split("<span>")[4].split("</span>")[0];
                            } catch (Exception ignored) {
                            }
                            try {
                                predmet_color = day[i][j].split("style=\"background-color:")[1].split("\">")[0];
                            } catch (Exception ignored) {
                            }
                            try {
                                predmet_distant = day[i][j].split("</div>")[2].split("</td>")[0];
                            } catch (Exception ignored) {
                            }
                            predmet_time = null;
                            if ((j > 0) && (schet < 7)) {
                                predmet_time = day_data_time[schet];
                                if (day_data_razmer[schet].equals(predmet_razmer)) {
                                    if (prohod) {
                                        prohod = false;
                                    } else {
                                        schet++;
                                    }
                                } else {
                                    if (!prohod) {
                                        prohod = true;
                                    } else {
                                        schet++;
                                        prohod = false;
                                    }
                                }
                            }

                            Cursor r = MainActivity.sqLiteDatabase.rawQuery("SELECT * FROM raspisanie WHERE r_group_code = " + r_selectedItem_id + " AND r_week_number = " + (week_id_upd + ff) + " AND r_week_day = " + i + " AND r_para_number = " + j + " AND " + " r_search_type = '" + r_selectedItem_type + "'", null); // SELECT запрос
                            if (r.getCount() == 0) {
                                put_db(i, j, ff);
                            } // Если даной недели нет в базе
                            else {
                                r.moveToFirst();
                                String predmet_name_db = r.getString(4);
                                String predmet_prepod_db = r.getString(5);
                                String predmet_group_db = r.getString(6);
                                String predmet_podgroup_db = r.getString(7);
                                String predmet_aud_db = r.getString(8);
                                String predmet_time_db = r.getString(9);
                                String predmet_distant_db = r.getString(15);
                                String whereClause = "r_group_code = '" + r_selectedItem_id + "' AND r_week_number = '" + (week_id_upd + ff) + "' AND r_week_day = '" + i + "' AND r_para_number = '" + j + "' AND r_search_type = '" + r_selectedItem_type + "'";
                                if (!(Objects.equals(predmet_name, predmet_name_db)) | !(Objects.equals(predmet_prepod, predmet_prepod_db)) |
                                        !(Objects.equals(predmet_distant, predmet_distant_db)) |
                                        !(Objects.equals(predmet_group, predmet_group_db)) | !(Objects.equals(predmet_podgroup, predmet_podgroup_db)) |
                                        !(Objects.equals(predmet_aud, predmet_aud_db)) | !(Objects.equals(predmet_time, predmet_time_db))) {
                                    MainActivity.sqLiteDatabase.delete("raspisanie", whereClause, null);
                                    put_db(i, j, ff);
                                    // Это нужно для вызова вне основного потока
                                    new Handler(Looper.getMainLooper()).post(() -> { // Выводим уведомление о наличии нового расписания
                                        new ShowNotification(context, r_selectedItem + " " + context.getResources().getString(R.string.new_rasp) + "!", predmet_data_ned + " " + predmet_data_chi + ". " + context.getResources().getString(R.string.new_rasp_sub), Integer.parseInt(r_selectedItem_id)).start();
                                    });
                                }
                                if (type.equals("widget")){
                                    MainActivity.sqLiteDatabase.delete("raspisanie", whereClause, null);
                                    put_db(i, j, ff);
                                }
                            }
                        }
                    }
                }
                Raspisanie_show.refresh_successful = true;
            }
            catch(Exception e){
                e.printStackTrace();
            }
            finally {
                System.out.println("Расписание для " + r_selectedItem + " было обновлено");
                Raspisanie_show.refresh_on_off = false;
            }
        }
    }

    private void put_db(int i, int j, int ff){
        try(SQLiteDatabase sqLiteDatabaseS = new DataBase_Local(context).getWritableDatabase()){
            ContentValues rowValues = new ContentValues(); // Значения для вставки в базу данных
            rowValues.put("r_group_code", r_selectedItem_id);
            rowValues.put("r_week_day", i);
            rowValues.put("r_week_number", (week_id_upd + ff));
            rowValues.put("r_para_number", j);
            rowValues.put("r_name", predmet_name);
            rowValues.put("r_prepod", predmet_prepod);
            rowValues.put("r_group", predmet_group);
            rowValues.put("r_podgroup", predmet_podgroup);
            rowValues.put("r_aud", predmet_aud);
            rowValues.put("r_razmer", predmet_time);
            rowValues.put("r_week_day_name", predmet_data_ned);
            rowValues.put("r_week_day_date", predmet_data_chi);
            rowValues.put("r_search_type", r_selectedItem_type);
            rowValues.put("r_last_update", new Date().getTime());
            rowValues.put("r_color", predmet_color);
            rowValues.put("r_distant", predmet_distant);
            sqLiteDatabaseS.insert("raspisanie", null, rowValues);
        }
    }
}