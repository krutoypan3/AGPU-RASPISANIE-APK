package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems;
import ru.agpu.artikproject.layout.BuildingInfo;

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

        // Создаем намерение и передаем необходимые параметры
        Intent intent = new Intent(act, BuildingInfo.class);
        intent.putExtra("itemPosition", itemPosition); // Позицию
        intent.putExtra("mainText", item.getMainText()); // Основной текст
        intent.putExtra("subText", item.getSubText()); // Дополнительный текст
        ImageView imageView = itemView.findViewById(R.id.cardViewAudImage); // Картинка
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        intent.putExtra("picture", b);
        intent.putExtra("picture_url", item.getImageResourceUrl());

        Pair<View, String> pair1 = Pair.create(imageView, "cardViewAudImage"); // Картинка
        Pair<View, String> pair2 = Pair.create(itemView.findViewById(R.id.cardViewAudMainText), "cardViewAudMainText"); // Основной текст
        Pair<View, String> pair3 = Pair.create(itemView.findViewById(R.id.cardViewAudSubText), "cardViewAudSubText"); // Дополнительный текст

        // Это нужно для предотвращения мерцания при анимации
        act.getWindow().setExitTransition(null);

        // Настраиваем анимацию намерения
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair1, pair2, pair3);
        act.startActivity(intent, options.toBundle()); // Запускаем наше намерение
    }
}
