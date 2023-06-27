package ru.agpu.artikproject.background_work.rasp_show.recycler_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.rasp_show.ParaInfo
import ru.agpu.artikproject.background_work.theme.GetTextColor
import ru.agpu.artikproject.background_work.theme.Theme

class DayShowRVAdapter(val act: Activity, val datas: List<DayShowRVItems>): RecyclerView.Adapter<DayShowRVHolder>() {
    private val mLayoutInflater = LayoutInflater.from(act.applicationContext)

    override fun getItemCount(): Int = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayShowRVHolder {
        // Inflate view from recyclerview_item_layout.xml
        val recyclerViewItem: View = mLayoutInflater.inflate(R.layout.recyclerview_item_rasp_day_show, parent, false)

        // Слушатель нажатий на элемент списка
        recyclerViewItem.setOnClickListener { v: View? ->
            val recyclerView = parent as RecyclerView
            val itemPosition = recyclerView.getChildLayoutPosition(v!!) // Получаем позицию нажатого элемента
            ParaInfo(itemPosition, act, datas)
        }
        return DayShowRVHolder(recyclerViewItem)
    }

    // Здесь мы настраиваем наши маленькие карточки
    override fun onBindViewHolder(holder: DayShowRVHolder, position: Int) {
        if (position == 0) { // Если на входе первый элемент, меняем цвет фона
            if (Theme.getApplicationTheme(act) == AppCompatDelegate.MODE_NIGHT_NO) // Если тема дневная
                act.findViewById<View>(R.id.day_para_view_rec).setBackgroundColor(act.getColor(R.color.white)) // Ставим светлый фон
            else  // Если тема ночная
                act.findViewById<View>(R.id.day_para_view_rec).setBackgroundColor(act.getColor(R.color.black)) // Ставим темный фон
        }
        val item = datas[position]
        holder.cardParaNumber.text = item.cardParaNumber
        holder.cardParaTime.text = item.cardParaTime
        holder.cardParaName.text = item.cardParaName
        holder.cardParaAud.text = item.cardParaAud
        holder.cardParaPrepod.text = item.cardParaPrepod
        holder.paraNumAndTimeLayout.setBackgroundColor(item.paraNumAndTimeLayoutColor)
        holder.paraDescriptionLayout.setBackgroundColor(item.paraDescriptionLayoutColor)
        val textColor = GetTextColor.getAppColor(act.applicationContext)
        holder.cardParaNumber.setTextColor(textColor)
        holder.cardParaTime.setTextColor(textColor)
        holder.cardParaName.setTextColor(textColor)
        holder.cardParaAud.setTextColor(textColor)
        holder.cardParaPrepod.setTextColor(textColor)
    }
}
