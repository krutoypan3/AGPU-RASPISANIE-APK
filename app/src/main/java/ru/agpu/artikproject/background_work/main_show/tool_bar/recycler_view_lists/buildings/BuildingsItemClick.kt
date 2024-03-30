package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_BUILDINGS_SHOW
import ru.agpu.artikproject.presentation.layout.fragment.BuildingInfoFragment
import ru.agpu.artikproject.presentation.layout.fragment.BuildingInfoFragment.Companion.mainText
import ru.agpu.artikproject.presentation.layout.fragment.BuildingInfoFragment.Companion.pictureByteArrayOutputArray
import ru.agpu.artikproject.presentation.layout.fragment.BuildingInfoFragment.Companion.pictureUrl
import ru.agpu.artikproject.presentation.layout.fragment.BuildingInfoFragment.Companion.subText
import java.io.ByteArrayOutputStream

/**
 * Обрабатывает нажатия на список корпусов
 * @param recyclerView RecyclerView
 * @param itemView Выбранный элемент (View)
 * @param datas Информация о выбранном элементе
 */
class BuildingsItemClick(recyclerView: RecyclerView, itemView: View, datas: List<RecyclerViewItems> ) {
    init {
        val itemPosition = recyclerView.getChildLayoutPosition(itemView) // Получаем позицию нажатого элемента
        val item = datas[itemPosition] // Получаем сам нажатый элемент
        BuildingInfoFragment.itemPosition = itemPosition // Позицию
        mainText = item.mainText // Основной текст
        subText = item.subText // Дополнительный текст

        val imageView = itemView.findViewById<ImageView>(R.id.cardViewAudImage) // Картинка

        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)

        pictureByteArrayOutputArray = baos.toByteArray()

        pictureUrl = item.imageResourceUrl

        FRAGMENT = BACK_TO_BUILDINGS_SHOW
        myFragmentManager?.beginTransaction()
            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ?.replace(R.id.fragment_container_view, BuildingInfoFragment::class.java, null)
            ?.commit()
    }
}