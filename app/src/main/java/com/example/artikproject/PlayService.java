package com.example.artikproject;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlayService extends Service {
    private static SQLiteDatabase sqLiteDatabaseS;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class, 5, TimeUnit.MINUTES)
                        .build();
        WorkManager
                .getInstance(getApplicationContext())
                .enqueue(uploadWorkRequest);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addNotification(Context context, String title, String subtitle) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

// The id of the channel.
        int id = 123123;

// The user-visible name of the channel.
        CharSequence name = "agpu_chanel";

// The user-visible description of the channel.
        String description = "agpu_raspisanie";

        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel(id + "", name,importance);

// Configure the notification channel.
        mChannel.setDescription(description);

        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);

        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        mNotificationManager.createNotificationChannel(mChannel);
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

// Sets an ID for the notification, so it can be updated.
        int notifyID = 1;

// The id of the channel.
        String CHANNEL_ID = "123123";

// Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId(CHANNEL_ID)
                .build();

// Issue the notification.
        mNotificationManager.notify(id, notification);
    }

    public boolean isOnline(Context context) { // Функция определяющая есть ли интернет
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true; // Интернет есть
        }
        return false; // Интернета нет
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void get_group_db(Context context) {
        sqLiteDatabaseS = new DataBase(context).getWritableDatabase(); //Подключение к базе данных
        if (isOnline(context)) {
            Cursor r = sqLiteDatabaseS.rawQuery("SELECT r_group_code, r_selectedItem_type, r_selectedItem FROM rasp_update", null); // SELECT запрос
            ArrayList<String> r_group0 = new ArrayList<>();
            ArrayList<String> r_group1 = new ArrayList<>();
            ArrayList<String> r_group2 = new ArrayList<>();

            Date date1 = new Date();
            long date_ms = date1.getTime() + 10800000;
            int week_id_upd = (int) ((date_ms - 18489514000f) / 1000f / 60f / 60f / 24f / 7f); // Номер текущей недели
            r.moveToFirst();
            do {
                if (r.getCount() != 0) {
                    r_group0.add(r.getString(0));
                    r_group1.add(r.getString(1));
                    r_group2.add(r.getString(2));
                }
            } while (r.moveToNext());

            for (int i = 0; i < r_group0.size(); i++) {
                GetRasp x = new GetRasp(false, r_group0.get(i), r_group1.get(i), r_group2.get(i), week_id_upd, context);
                x.execute("");

            }
        }
    }


    class GetRasp extends AsyncTask<String, String, String> {
        boolean start_activity = true;
        String r_selectedItem;
        String r_selectedItem_id;
        String r_selectedItem_type;
        int week_id_upd;
        Context context;

        public GetRasp(boolean b, String rg0, String rg1, String rg2, int week_id_upd, Context context) {
            this.start_activity = b;
            this.r_selectedItem_id = rg0;
            this.r_selectedItem_type = rg1;
            this.r_selectedItem = rg2;
            this.week_id_upd = week_id_upd;
            this.context = context;
        }
        public GetRasp() {
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {

            for(int ff = -1; ff<2; ff++) {
                String urlq;
                urlq = "https://www.it-institut.ru/Raspisanie/SearchedRaspisanie?OwnerId=118&SearchId=" + r_selectedItem_id + "&SearchString=" + r_selectedItem + "&Type=" + r_selectedItem_type + "&WeekId=" + (week_id_upd + ff);
                Document doc = null;
                try {
                    doc = Jsoup.connect(urlq).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert doc != null;
                List<String[]> days = new ArrayList<>();
                for (int i = 1; i < 7; i++) {
                    days.add(doc.select("tbody").toString().split("th scope")[i].split("td colspan="));
                }
                String[] day_data_razmer = new String[7];
                String[] day_data_time = new String[7];
                boolean prohod = false;
                String[] dataaa = doc.select("thead").toString().split("colspan=\"");
                for (int i = 1; i < 8; i++) {
                    day_data_razmer[i-1] = (dataaa[i].split("\"")[0]);
                    day_data_time[i-1] = (dataaa[i].split(">")[2].split("<")[0]);
                }
                String[][] day;
                day = days.toArray(new String[0][0]);
                for (int i = 0; i < 6; i++) {
                    int schet = 0;
                    String predmet_data_ned = day[i][0].split("row\">")[1].split("<br>")[0];
                    String predmet_data_chi = day[i][0].split("<br>")[1].split("</th>")[0];
                    for (int j = 0; j < 10; j++) {

                        String predmet_name = null;
                        String predmet_prepod = null;
                        String predmet_group = null;
                        String predmet_podgroup = null;
                        String predmet_aud = null;
                        String predmet_razmer = null;
                        try {
                            predmet_razmer = day[i][j].split("\"")[1];
                            predmet_name = day[i][j].split("<span>")[1].split("</span>")[0];
                            predmet_prepod = day[i][j].split("<span>")[2].split("</span>")[0];
                            predmet_group = day[i][j].split("<span>")[3].split("</span>")[0];
                            predmet_podgroup = day[i][j].split("<span>")[4].split("</span>")[0];
                        }
                        catch (Exception e) {
                        }
                        String predmet_time = null;
                        if((j>0) && (schet < 7)){
                            predmet_time = day_data_time[schet];
                            if(day_data_razmer[schet].equals(predmet_razmer)){
                                if (prohod){
                                    prohod = false;
                                }
                                else{
                                    schet++;
                                }
                            }
                            else{
                                if (!prohod){
                                    prohod = true;
                                }
                                else{
                                    schet++;
                                    prohod = false;
                                }
                            }
                        }

                        Cursor r = sqLiteDatabaseS.rawQuery("SELECT * FROM rasp_test1 WHERE r_group_code = " + r_selectedItem_id + " AND r_week_number = " + (week_id_upd + ff) + " AND r_week_day = " + i + " AND r_para_number = " + j + " AND " + " r_search_type = '" + r_selectedItem_type + "'", null); // SELECT запрос
                        if (r.getCount()==0){// Если даной недели нет в базе
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
                            sqLiteDatabaseS.insert("rasp_test1", null, rowValues); // Вставка строки в базу данных
                        }
                        else{
                            r.moveToFirst();
                            String predmet_name_db = r.getString(4);
                            String predmet_prepod_db = r.getString(5);
                            String predmet_group_db = r.getString(6);
                            String predmet_podgroup_db = r.getString(7);
                            String predmet_aud_db = r.getString(8);


                            if (!(Objects.equals(predmet_name,predmet_name_db)) | !(Objects.equals(predmet_prepod, predmet_prepod_db)) |
                                    !(Objects.equals(predmet_group, predmet_group_db)) | !(Objects.equals(predmet_podgroup, predmet_podgroup_db)) |
                                    !(Objects.equals(predmet_aud, predmet_aud_db))){
                                sqLiteDatabaseS.delete("rasp_test1", "r_group_code = '" + r_selectedItem_id + "' AND r_week_number = '" + (week_id_upd + ff) + "' AND r_week_day = '" + i + "' AND r_para_number = '" + j + "' AND r_search_type = '" + r_selectedItem_type + "'", null);
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
                                rowValues.put("r_razmer", predmet_razmer);
                                rowValues.put("r_week_day_name", predmet_data_ned);
                                rowValues.put("r_week_day_date", predmet_data_chi);
                                rowValues.put("r_search_type", r_selectedItem_type);
                                rowValues.put("r_last_update", new Date().getTime());
                                sqLiteDatabaseS.insert("rasp_test1", null, rowValues);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    addNotification(context, r_selectedItem + " новое расписание!", "Расписание обновилось, скорее проверьте!");
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

}