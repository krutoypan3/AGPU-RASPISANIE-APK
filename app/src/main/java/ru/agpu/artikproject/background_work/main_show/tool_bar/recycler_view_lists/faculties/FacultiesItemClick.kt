package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewAdapter
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups.LoadFacultiesGroupsList

/**
 * Обрабатывает нажатия на список факультетов
 * @param recyclerView RecyclerView
 * @param itemView Выбранный элемент (View)
 * @param datas Информация о выбранном элементе
 * @param act Активити
 */
class FacultiesItemClick(recyclerView: RecyclerView, itemView: View, datas: List<RecyclerViewItems>, act: Activity) {
    init {
        val itemPosition = recyclerView.getChildLayoutPosition(itemView) // Получаем позицию нажатого элемента

        val item = datas[itemPosition] // Получаем сам нажатый элемент

        RecyclerViewAdapter.selected_faculties_position = item.mainText
        RecyclerViewAdapter.selected_faculties_logos = item.imageResourceUrl
        recyclerView.adapter = RecyclerViewAdapter(
            act,
            LoadFacultiesGroupsList()[act.applicationContext],
            RecyclerViewAdapter.IS_FACULTIES_GROUPS_ADAPTER
        )
    }
}