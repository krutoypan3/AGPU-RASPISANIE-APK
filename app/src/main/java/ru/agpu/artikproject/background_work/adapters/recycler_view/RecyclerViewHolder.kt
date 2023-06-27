package ru.agpu.artikproject.background_work.adapters.recycler_view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R

class RecyclerViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.cardViewAudImage)
    val mainTextView: TextView = itemView.findViewById(R.id.cardViewAudMainText)
    val subTextView: TextView = itemView.findViewById(R.id.cardViewAudSubText)
    val cardView: CardView = itemView.findViewById(R.id.cardViewAudImageCard)
    val cardBackground: ConstraintLayout = itemView.findViewById(R.id.cardBackground)
}