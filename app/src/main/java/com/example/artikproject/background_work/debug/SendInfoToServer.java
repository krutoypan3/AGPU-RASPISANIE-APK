package com.example.artikproject.background_work.debug;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SendInfoToServer extends Thread {
    Context context;
    /**
     * Класс отвечающий за отправку информации для отладки на сервер
     * @param context Контекст приложения
     */
    public SendInfoToServer(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        try {
            URL url = new URL("http://sql-serverartem.ddns.net:54984/raspisanie_agpu_debug_info");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Текущая версия приложения
            String version = Device_info.getAppVersion(context); // Её название**
            int versionCode = Device_info.getAppVersionCode(context); // Её код**

            Map<Object, Object> params = new HashMap<>();
            params.put("DeviceManufacturer", Device_info.getDeviceManufacturer());
            params.put("DeviceModel", Device_info.getDeviceModel());
            params.put("DeviceId", Device_info.getDeviceId());
            params.put("DeviceDisplay", Device_info.getDeviceDisplay());
            params.put("DeviceWidth", Device_info.getDeviceWidth(context));
            params.put("DeviceHeight", Device_info.getDeviceHeight(context));
            params.put("AndroidVersion", Device_info.getDeviceAndroidVersion());
            params.put("SDK", Device_info.getDeviceSDK());
            params.put("DeviceHardware", Device_info.getDeviceHardware());
            params.put("DeviceDevice", Device_info.getDeviceDevice());
            params.put("DeviceProduct", Device_info.getDeviceProduct());
            params.put("AppVersion", version);
            params.put("AppVersionCode", versionCode);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<Object, Object> param : params.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                try {
                    postData.append(URLEncoder.encode(String.valueOf(param.getKey()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                postData.append('=');
                try {
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
            connection.setDoOutput(true);
            try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
                writer.write(postDataBytes);
                writer.flush();
                writer.close();

                StringBuilder content;

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    content = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connection.disconnect();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

