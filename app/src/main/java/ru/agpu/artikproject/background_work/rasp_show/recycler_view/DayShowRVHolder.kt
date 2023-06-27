package ru.agpu.artikproject.background_work.rasp_show.recycler_view

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R

class DayShowRVHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var cardParaNumber: TextView = itemView.findViewById(R.id.card_para_number)
    var cardParaTime: TextView = itemView.findViewById(R.id.card_para_time)
    var cardParaName: TextView = itemView.findViewById(R.id.card_para_name)
    var cardParaAud: TextView = itemView.findViewById(R.id.card_para_aud)
    var cardParaPrepod: TextView = itemView.findViewById(R.id.card_para_prepod)
    var paraNumAndTimeLayout: ConstraintLayout = itemView.findViewById(R.id.para_num_and_time_layout)
    var paraDescriptionLayout: ConstraintLayout = itemView.findViewById(R.id.para_description_layout)
}