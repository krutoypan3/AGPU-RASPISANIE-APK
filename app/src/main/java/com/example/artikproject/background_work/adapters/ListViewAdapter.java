package com.example.artikproject.background_work.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.artikproject.R;

public class ListViewAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ListViewItems> objects;

    public ListViewAdapter(Context context, ArrayList<ListViewItems> items) {
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        TextView textView = view.findViewById(R.id.tvItemText);
        textView.setPadding(30,30,30,30);
        textView.setTextSize(18);
        textView.setTextColor(ctx.getColor(GetColorTextView.get()));

        ListViewItems p = getProduct(position);
        textView.setText(p.item);
        return view;
    }

    // товар по позиции
    ListViewItems getProduct(int position) {
        return ((ListViewItems) getItem(position));
    }


}