package com.example.artikproject.background_work.adapters.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artikproject.R;
import com.example.artikproject.background_work.main_show.ListViewAud_ClickListener;
import com.example.artikproject.background_work.theme.GetColorTextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<RecyclerViewItems> countries;
    private final Activity act;
    private final LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Activity act, List<RecyclerViewItems> datas ) {
        this.act = act;
        this.countries = datas;
        this.mLayoutInflater = LayoutInflater.from(act.getApplicationContext());
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        // Inflate view from recyclerview_item_layout.xml
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.recyclerview_item_layout, parent, false);

        recyclerViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRecyclerItemClick( (RecyclerView)parent, v);
            }
        });
        return new RecyclerViewHolder(recyclerViewItem);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        // Cet image_name in countries via position
        RecyclerViewItems image_name = this.countries.get(position);

        int imageResId = this.getDrawableResIdByName(image_name.getImageName());
        // Bind data to viewholder
        holder.image.setImageResource(imageResId);
        holder.mainTextView.setText(image_name.getMainText());
        int textColor = GetColorTextView.getAppColor(act.getApplicationContext());
        if (textColor == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            textColor = GetColorTextView.getSystemColor(act.getApplicationContext());
        holder.mainTextView.setTextColor(textColor);
        String newSubText = act.getString(R.string.Audiences) +  " : "+ image_name.getSubText();
        holder.subTextView.setText(newSubText);
        holder.subTextView.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return this.countries.size();
    }

    // Find Image ID corresponding to the name of the image (in the directory drawable).
    public int getDrawableResIdByName(String resName)  {
        String pkgName = act.getPackageName();
        // Return 0 if not found.
        return act.getResources().getIdentifier(resName , "drawable", pkgName);
    }

    // Click on RecyclerView Item.
    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView);
        RecyclerViewItems item  = this.countries.get(itemPosition);
        new ListViewAud_ClickListener(itemPosition, act);
        Toast.makeText(act.getApplicationContext(), item.getMainText(), Toast.LENGTH_LONG).show();
    }
}