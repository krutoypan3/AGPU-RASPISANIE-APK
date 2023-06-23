package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo.Companion.mainText
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo.Companion.pictureByteArrayOutputArray
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo.Companion.pictureUrl
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo.Companion.subText
import ru.agpu.artikproject.presentation.layout.MainActivity
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
        FragmentBuildingInfo.itemPosition = itemPosition // Позицию
        mainText = item.mainText // Основной текс
        subText = item.subText // Дополнительный текст

        val imageView = itemView.findViewById<ImageView>(R.id.cardViewAudImage) // Картинка

        val drawable = imageView.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)

        pictureByteArrayOutputArray = baos.toByteArray()

        pictureUrl = item.imageResourceUrl

        MainActivity.FRAGMENT = MainActivity.BACK_TO_BUILDINGS_SHOW
        MainActivity.fragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(R.id.fragment_container_view, FragmentBuildingInfo::class.java, null)
            .commit()
    }
}