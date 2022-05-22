package ru.agpu.artikproject.background_work;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.debug.Device_info;


public class CheckAppUpdate extends Thread {
    private final Activity act;
    private final boolean show_no_update_message;
    private static boolean show = false;
    public static boolean app_have_update = false;
    public static String whats_new;
    public static String url_download;

    /**
     * Класс отвечающий за поиск обновлений приложения
     * @param act Контекст приложения
     * @param show_no_update Показать уведомление об отсутствии обновлений (True / False)
     */
    public CheckAppUpdate(Activity act, boolean show_no_update) {
        this.act = act;
        this.show_no_update_message = show_no_update;
    }
    /**
     * Класс отвечающий за ручной поиск обновлений приложения
     * @param act Контекст приложения
     * @param show_no_update Показать уведомление об отсутствии обновлений (True / False)
     * @param show Показать уведомление о наличии обновлений (True / False)
     */
    public CheckAppUpdate(Activity act, boolean show_no_update, boolean show) {
        this.act = act;
        this.show_no_update_message = show_no_update;
        CheckAppUpdate.show = show;
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
            if(!newVersion.equals(currentVersion)){ // Если версия приложения отличается от версии приложения на сервере
                act.runOnUiThread(() -> {
                    ImageView update_btn = act.findViewById(R.id.update_btn);
                    update_btn.setVisibility(View.VISIBLE);
                    update_btn.setAnimation(AnimationUtils.loadAnimation(act.getApplicationContext(), R.anim.slow_scale_long));
                    update_btn.setOnClickListener(view -> show_update_message());
                });
                app_have_update = true;
                whats_new = (String) jo.get("body");
                JSONArray assets = (JSONArray) jo.get("assets");
                assert assets != null;
                JSONObject assets_0 = (JSONObject) assets.get(0);
                url_download = (String) assets_0.get("browser_download_url");
                if (show) show_update_message();
            }
            else if(show_no_update_message){ // Если версии одинаковые
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(act, R.string.no_new_version, Toast.LENGTH_SHORT).show());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void show_update_message(){
        CustomAlertDialog cdd = new CustomAlertDialog(act, act.getResources().getString(R.string.new_app_version), whats_new, Uri.parse(url_download));
        cdd.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_background);
        cdd.show();
    }
}
