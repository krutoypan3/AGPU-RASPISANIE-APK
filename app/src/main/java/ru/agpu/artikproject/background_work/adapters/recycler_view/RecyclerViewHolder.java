package ru.agpu.artikproject.background_work.adapters.recycler_view;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public final ImageView image;
    public final TextView mainTextView;
    public final TextView subTextView;
    public final CardView cardView;
    public final ConstraintLayout cardBackground;

    // @itemView: recyclerview_item_layout.xml
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        this.image = itemView.findViewById(R.id.cardViewAudImage);
        this.mainTextView = itemView.findViewById(R.id.cardViewAudMainText);
        this.subTextView = itemView.findViewById(R.id.cardViewAudSubText);
        this.cardView = itemView.findViewById(R.id.cardViewAudImageCard);
        this.cardBackground = itemView.findViewById(R.id.cardBackground);
    }
}