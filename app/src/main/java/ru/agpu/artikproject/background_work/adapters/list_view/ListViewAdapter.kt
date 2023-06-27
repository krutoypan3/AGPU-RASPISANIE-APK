package ru.agpu.artikproject.background_work.adapters.list_view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.theme.GetTextColor

class ListViewAdapter(
    private val ctx: Context,
    private val objects: ArrayList<ListViewItems>,
    val widget: Boolean = false
): BaseAdapter() {

    private val lInflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // кол-во элементов
    override fun getCount(): Int = objects.size

    // элемент по позиции
    override fun getItem(position: Int) = objects[position]

    // id по позиции
    override fun getItemId(position: Int) =position.toLong()

    // пункт списка
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // используем созданные, но не используемые view
        var view = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false)
        }
        val textView = view!!.findViewById<TextView>(R.id.item_name)
        textView.setPadding(30, 30, 30, 30)
        textView.textSize = 15f

        if (!widget) textView.setTextColor(GetTextColor.getAppColor(ctx))
        else textView.setTextColor(ctx.getColor(R.color.textColorPrimary))

        val p = getItem(position)
        textView.text = p.item
        return view
    }
}