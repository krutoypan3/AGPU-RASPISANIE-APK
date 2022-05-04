package com.example.artikproject.background_work.adapters.RecyclerView;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artikproject.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    ImageView image;
    TextView mainTextView;
    TextView subTextView;
    ConstraintLayout cardBackgroundDarker;
    ConstraintLayout cardBackground;

    // @itemView: recyclerview_item_layout.xml
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.cardViewAudImage);
        this.mainTextView = itemView.findViewById(R.id.cardViewAudMainText);
        this.subTextView = itemView.findViewById(R.id.cardViewAudSubText);
        this.cardBackground = itemView.findViewById(R.id.cardBackground);
        this.cardBackgroundDarker = itemView.findViewById(R.id.cardBackgroundDarker);
    }
}