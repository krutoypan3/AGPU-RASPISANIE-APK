package com.example.artikproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class GetGroupList extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... search_request) {
        try {
            DateBase_Online dateBase_online = new DateBase_Online();
            List<String[]> group_list = dateBase_online.GetGroupList(search_request[0]);
            List<String> group_list_id = new ArrayList<>();
            List<String> group_list_name = new ArrayList<>();
            List<String> group_list_type = new ArrayList<>();
            for(int i = 0; i < group_list.size(); i++){
                group_list_id.add(group_list.get(i)[0]);
                group_list_name.add(group_list.get(i)[1]);
                group_list_type.add(group_list.get(i)[2]);
                ContentValues rowValues = new ContentValues();
                rowValues.put("r_item_id", group_list.get(i)[0]);
                rowValues.put("r_item_name", group_list.get(i)[1]);
                rowValues.put("r_item_type", group_list.get(i)[2]);
                try{MainActivity.sqLiteDatabase.insert("item_list", null, rowValues);}
                catch (Exception ignored){}
            }
            MainActivity.group_listed_id =group_list_id.toArray(new String[0]);
            MainActivity.group_listed = group_list_name.toArray(new String[0]);
            MainActivity.group_listed_type = group_list_type.toArray(new String[0]);
        } catch (Exception e) {
            System.out.println("Исключение в классе GetGroupList");
        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        ArrayAdapter<String> adapter = new ArrayAdapter(MainActivity.listview.getContext(), android.R.layout.simple_list_item_1, MainActivity.group_listed);
        MainActivity.listview.setAdapter(adapter);
        MainActivity.result.setText(res);
        MainActivity.listview.setVisibility(View.VISIBLE);
    }
}
