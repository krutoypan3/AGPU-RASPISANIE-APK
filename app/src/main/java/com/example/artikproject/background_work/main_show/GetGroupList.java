package com.example.artikproject.background_work.main_show;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.artikproject.R;
import com.example.artikproject.layout.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GetGroupList extends Thread {
    private final String urlq;
    private final Context context;
    /**
     * Класс отвечающий за поиск группы, аудитории, преподователя
     * @param urlq ссылка на сайт
     * @param context активное активити
     */
    public GetGroupList(String urlq, Context context){
        this.context = context;
        this.urlq = urlq;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlq);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            try{ connection.connect();}
            catch (Exception e){
                // Это нужно для вызова вне основного потока
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, R.string.Internet_error, Toast.LENGTH_LONG).show());
                return;
            }
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String buffer;
            buffer = reader.readLine();

            String jsonString = buffer; // ЭТО JSON со списком групп
            JSONArray obj = new JSONArray(jsonString);
            List<String> group_list = new ArrayList<>();
            List<String> group_list_type = new ArrayList<>();
            List<String> group_list_id = new ArrayList<>();

            for (int ii = 0; ii<obj.length(); ii++){
                JSONObject guysJSON = obj.getJSONObject(ii);
                Iterator<?> keys = guysJSON.keys();
                int i = 0;
                while(keys.hasNext()) {
                    Object value = guysJSON.get((String)keys.next());
                    if (i % 4 == 0) {
                        String group_name = (String) value;
                        group_list.add(group_name);
                    }
                    else if (i % 4 == 1) {
                        String group_type = (String) value;
                        group_list_type.add(group_type);
                    }
                    else if (i % 4 == 2) {
                        String group_id = value.toString();
                        group_list_id.add(group_id);
                    }
                    i++;
                }
            }

            MainActivity.group_listed = group_list.toArray(new String[0]);
            MainActivity.group_listed_type = group_list_type.toArray(new String[0]);
            MainActivity.group_listed_id =group_list_id.toArray(new String[0]);

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) connection.disconnect();
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Это нужно для вызова вне основного потока
        new Handler(Looper.getMainLooper()).post(() -> {
            ArrayAdapter<String> adapter = new ArrayAdapter(context, R.layout.listviewadapterbl, MainActivity.group_listed);
            MainActivity.listview.setAdapter(adapter);
            MainActivity.result.setText("");
            MainActivity.listview.setVisibility(View.VISIBLE);
        });
    }
}