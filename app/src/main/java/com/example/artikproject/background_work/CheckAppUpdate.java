package com.example.artikproject.background_work;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.example.artikproject.R;
import com.example.artikproject.background_work.debug.Device_info;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


public class CheckAppUpdate extends Thread {
    private final Activity act;
    /**
     * Класс отвечающий за поиск обновлений приложения
     * @param act Контекст приложения
     */
    public CheckAppUpdate(Activity act) {
        this.act = act;
    }

    @SuppressLint("InflateParams")
    @Override
    public void run() {
        try{
            URL url = new URL("https://api.github.com/repos/krutoypan3/AGPU-RASPISANIE-APK/releases/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String ass = response.toString();
            // Считываем json
            Object obj = new JSONParser().parse(ass);
            JSONObject jo = (JSONObject) obj;
            String newVersion = (String) Objects.requireNonNull(jo.get("tag_name"));
            String currentVersion = Device_info.getAppVersion(act.getApplicationContext());
            if(!newVersion.equals(currentVersion)){
                String whats_new = (String) jo.get("body");
                JSONArray assets = (JSONArray) jo.get("assets");
                assert assets != null;
                JSONObject assets_0 = (JSONObject) assets.get(0);
                String url_download = (String) assets_0.get("browser_download_url");

                // Это нужно для вызова вне основного потока
                new Handler(Looper.getMainLooper()).post(() -> {
                    CustomAlertDialog cdd=new CustomAlertDialog(act, act.getResources().getString(R.string.new_app_version), whats_new,Uri.parse(url_download));
                    cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
                    cdd.show();
                });
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
