package com.example.artikproject.background_work.adapters.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artikproject.R;
import com.example.artikproject.background_work.theme.GetColorTextView;
import com.example.artikproject.layout.BuildingInfo;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<RecyclerViewItems> datas;
    private final Activity act;
    private final LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Activity act, List<RecyclerViewItems> datas ) {
        this.act = act;
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(act.getApplicationContext());
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        // Inflate view from recyclerview_item_layout.xml
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.recyclerview_item_layout, parent, false);

        // Слушатель нажатий на элемент списка
        recyclerViewItem.setOnClickListener(v -> handleRecyclerItemClick( (RecyclerView)parent, v));

        return new RecyclerViewHolder(recyclerViewItem);
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        // Cet image_name in countries via position
        RecyclerViewItems image_name = this.datas.get(position);

        int imageResId = image_name.getImageResourceId();
        // Bind data to viewholder
        holder.image.setImageResource(imageResId);
        holder.mainTextView.setText(image_name.getMainText());
        int textColor = GetColorTextView.getAppColor(act.getApplicationContext());
        if (textColor == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            textColor = GetColorTextView.getAppColor(act.getApplicationContext());
        holder.mainTextView.setTextColor(textColor);
        String newSubText = act.getString(R.string.Audiences) +  " : "+ image_name.getSubText();
        holder.subTextView.setText(newSubText);
        holder.subTextView.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    // Функция обработки нажатия на элемент списка
    private void handleRecyclerItemClick(RecyclerView recyclerView, View itemView) {
        int itemPosition = recyclerView.getChildLayoutPosition(itemView); // Получаем позицию нажатого элемента
        RecyclerViewItems item  = this.datas.get(itemPosition); // Получаем сам нажатый элемент

        // Создаем намерение и передаем необходимые параметры
        Intent intent = new Intent(act, BuildingInfo.class);
        intent.putExtra("itemPosition", itemPosition); // Позицию
        intent.putExtra("mainText", item.getMainText()); // Основной текст
        intent.putExtra("subText", item.getSubText()); // Дополнительный текст
        intent.putExtra("imageResId", item.getImageResourceId()); // Id картинки

        Pair<View, String> pair1 = Pair.create(itemView.findViewById(R.id.cardViewAudImage), "cardViewAudImage"); // Картинка
        Pair<View, String> pair2 = Pair.create(itemView.findViewById(R.id.cardViewAudMainText), "cardViewAudMainText"); // Основной текст
        Pair<View, String> pair3 = Pair.create(itemView.findViewById(R.id.cardViewAudSubText), "cardViewAudSubText"); // Дополнительный текст

        // Это нужно для предотвращения мерцания при анимации
        act.getWindow().setExitTransition(null);

        // Настраиваем анимацию намерения
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair1, pair2, pair3);
        act.startActivity(intent, options.toBundle()); // Запускаем наше намерение
    }
}