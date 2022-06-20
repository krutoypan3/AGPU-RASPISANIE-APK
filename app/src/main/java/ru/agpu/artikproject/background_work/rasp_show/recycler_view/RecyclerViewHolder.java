package ru.agpu.artikproject.background_work.rasp_show.recycler_view;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import ru.agpu.artikproject.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public final TextView card_para_number;
    public final TextView card_para_time;
    public final TextView card_para_name;
    public final TextView card_para_aud;
    public final TextView card_para_prepod;
    public final ConstraintLayout para_num_and_time_layout;
    public final ConstraintLayout para_description_layout;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.card_para_number = itemView.findViewById(R.id.card_para_number);
        this.card_para_time = itemView.findViewById(R.id.card_para_time);
        this.card_para_name = itemView.findViewById(R.id.card_para_name);
        this.card_para_aud = itemView.findViewById(R.id.card_para_aud);
        this.card_para_prepod = itemView.findViewById(R.id.card_para_prepod);
        this.para_num_and_time_layout = itemView.findViewById(R.id.para_num_and_time_layout);
        this.para_description_layout = itemView.findViewById(R.id.para_description_layout);
    }
}