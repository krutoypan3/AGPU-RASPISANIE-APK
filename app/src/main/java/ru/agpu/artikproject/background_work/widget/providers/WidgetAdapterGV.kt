package ru.agpu.artikproject.background_work.widget.providers

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.widget.WidgetGridViewItem


class WidgetAdapterGV(
    private val context: Context,
    private val datas: List<WidgetGridViewItem>,
) :
    BaseAdapter() {


    override fun getCount() = datas.size

    override fun getItem(position: Int) = datas[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val grid: View = convertView
            ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.widget_gv_item, parent, true)

        val timeRange = grid.findViewById<TextView>(R.id.time_range)
        val separator = grid.findViewById<ImageView>(R.id.separator)
        val backgroundConstraint = grid.findViewById<ConstraintLayout>(R.id.background_image_view)
        val itemName = grid.findViewById<TextView>(R.id.item_name)
        val itemPrepodAndTime = grid.findViewById<TextView>(R.id.item_prepod_and_time)
        val itemGroup = grid.findViewById<TextView>(R.id.item_group)

        timeRange.text = datas[position].timeRange
        itemName.text = datas[position].itemName
        itemPrepodAndTime.text = datas[position].itemPrepodAndTime
        itemGroup.text = datas[position].itemGroup

        val backgroundConstraintColor: Int = datas[position].backgroundConstraint

        val shape = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(backgroundConstraintColor, backgroundConstraintColor)
        )
        shape.cornerRadius = 50f

        // TODO Если нужно будет изменить фон ращделителя в зависимости от темы... separator
        backgroundConstraint.background = shape
        return grid
    }
}