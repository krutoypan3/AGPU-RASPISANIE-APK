package ru.agpu.artikproject.background_work.adapters.recycler_view

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.debug.DeviceInfo
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.BuildingsItemClick
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.FacultiesItemClick
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups.GroupsItemClick
import ru.agpu.artikproject.background_work.theme.GetTextColor
import ru.agpu.artikproject.presentation.layout.MainActivity

class RecyclerViewAdapter(
    private val act: Activity,
    private val datas: List<RecyclerViewItems>,
    private val adapterIs: Int
): RecyclerView.Adapter<RecyclerViewHolder>() {

    private val mLayoutInflater = LayoutInflater.from(act.applicationContext)

    companion object {
        const val IS_BUILDINGS_ADAPTER = 0
        const val IS_FACULTIES_ADAPTER = 1
        const val IS_FACULTIES_GROUPS_ADAPTER = 2
        var selected_faculties_position = ""
        var selected_faculties_logos = ""
    }

    override fun getItemCount() = datas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate view from recyclerview_item_layout.xml
        val recyclerViewItem: View = mLayoutInflater.inflate(R.layout.recyclerview_item_layout, parent, false)

        // Слушатель нажатий на элемент списка
        recyclerViewItem.setOnClickListener { v: View ->
            when (adapterIs) {
                IS_BUILDINGS_ADAPTER -> BuildingsItemClick((parent as RecyclerView), v, datas)
                IS_FACULTIES_ADAPTER -> FacultiesItemClick((parent as RecyclerView), v, datas, act)
                IS_FACULTIES_GROUPS_ADAPTER -> {
                    GroupsItemClick((parent as RecyclerView), v, act)
                    parent.startAnimation(MainActivity.animUehalVl)
                }
            }
        }
        return RecyclerViewHolder(recyclerViewItem)
    }

    // Здесь мы настраиваем наши маленькие карточки
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // Cet item in countries via position
        val item = datas[position]
        val imageResUrl = item.imageResourceUrl
        holder.mainTextView.text = item.mainText

        val textColor = GetTextColor.getAppColor(act.applicationContext)

        holder.mainTextView.setTextColor(textColor)
        val newSubText = item.subText
        holder.subTextView.text = newSubText
        holder.subTextView.setTextColor(textColor)

        // Отображаем картинку в адаптере
        Glide.with(act.applicationContext)
            .load(imageResUrl)
            .apply(
                RequestOptions().override(
                    DeviceInfo.getDeviceWidth(act.applicationContext), 360
                )
            )
            .placeholder(R.drawable.agpu_ico)
            .into(holder.image)
    }
}