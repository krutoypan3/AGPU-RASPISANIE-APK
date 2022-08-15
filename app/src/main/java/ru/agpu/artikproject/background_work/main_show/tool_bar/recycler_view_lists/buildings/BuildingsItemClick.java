package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentBuildingInfo;
import ru.agpu.artikproject.layout.MainActivity;

public class BuildingsItemClick {
    /**
     * Обрабатывает нажатия на список корпусов
     * @param recyclerView RecyclerView
     * @param itemView Выбранный элемент (View)
     * @param datas Информация о выбранном элементе
     * @param act Активити
     */
    public BuildingsItemClick(RecyclerView recyclerView, View itemView, List<RecyclerViewItems> datas, Activity act) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        RecyclerViewItems item  = datas.get(itemPosition); // Получаем сам нажатый элемент

        FragmentBuildingInfo.itemPosition = itemPosition; // Позицию

        FragmentBuildingInfo.mainText = item.getMainText(); // Основной текст

        FragmentBuildingInfo.subText = item.getSubText(); // Дополнительный текст

        ImageView imageView = itemView.findViewById(R.id.cardViewAudImage); // Картинка
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        FragmentBuildingInfo.pictureByteArrayOutputArray = baos.toByteArray();

        FragmentBuildingInfo.pictureUrl = item.getImageResourceUrl();

        MainActivity.FRAGMENT = MainActivity.BACK_TO_BUILDINGS_SHOW;
        MainActivity.fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, FragmentBuildingInfo.class, null)
                .commit();

    }
}
