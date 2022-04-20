package com.example.artikproject.background_work.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.artikproject.R;
import com.example.artikproject.background_work.theme.GetColorTextView;

public class ListViewAdapter extends BaseAdapter {
    final Context ctx;
    final LayoutInflater lInflater;
    final ArrayList<ListViewItems> objects;
    boolean widget = false;

    public ListViewAdapter(Context context, ArrayList<ListViewItems> items) {
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ListViewAdapter(Context context, ArrayList<ListViewItems> items, boolean widget) {
        ctx = context;
        objects = items;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.widget = widget;
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
        textView.setTextSize(15);
        if (!widget) textView.setTextColor(GetColorTextView.getAppColor(ctx));
        else textView.setTextColor(ctx.getColor(R.color.textColorPrimary));
        ListViewItems p = getProduct(position);
        textView.setText(p.item);
        return view;
    }

    // товар по позиции
    ListViewItems getProduct(int position) {
        return ((ListViewItems) getItem(position));
    }


}