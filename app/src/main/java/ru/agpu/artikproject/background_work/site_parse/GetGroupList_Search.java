package ru.agpu.artikproject.background_work.site_parse;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewAdapter;
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems;
import ru.agpu.artikproject.layout.MainActivity;


public class GetGroupList_Search extends Thread {
    private final String urlq;
    private final Activity act;
    /**
     * Класс отвечающий за поиск группы, аудитории, преподователя
     * @param urlq Ссылка на сайт
     * @param act Активити
     */
    public GetGroupList_Search(String urlq, Activity act){
        this.act = act;
        this.urlq = urlq;
    }

    @Override
    public void run() {
        TextView result = act.findViewById(R.id.result);
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlq);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            try{ connection.connect();}
            catch (Exception e){
                // Это нужно для вызова вне основного потока
                new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(act.getApplicationContext(), R.string.Internet_error, Toast.LENGTH_LONG).show());
                return;
            }
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            String buffer;
            buffer = reader.readLine();

            String jsonString = buffer; // ЭТО JSON со списком групп
            JSONArray obj = new JSONArray(jsonString);
            ArrayList<ListViewItems> group_list = new ArrayList<>();
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
                        group_list.add(new ListViewItems(group_name));
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

            MainActivity.group_listed = group_list;
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
            ListView listview = act.findViewById(R.id.listview);
            ListViewAdapter adapter = new ListViewAdapter(act.getApplicationContext(), MainActivity.group_listed);
            listview.setAdapter(adapter);
            result.setText("");
            listview.setVisibility(View.VISIBLE);
        });
    }
}