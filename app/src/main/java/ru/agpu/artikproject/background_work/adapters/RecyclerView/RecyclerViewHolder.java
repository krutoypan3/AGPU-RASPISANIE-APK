package ru.agpu.artikproject.background_work.adapters.RecyclerView;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    public TextView mainTextView;
    public TextView subTextView;

    // @itemView: recyclerview_item_layout.xml
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.cardViewAudImage);
        this.mainTextView = itemView.findViewById(R.id.cardViewAudMainText);
        this.subTextView = itemView.findViewById(R.id.cardViewAudSubText);
    }
}