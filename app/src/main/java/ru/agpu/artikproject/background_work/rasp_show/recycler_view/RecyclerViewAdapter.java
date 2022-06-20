package ru.agpu.artikproject.background_work.rasp_show.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.rasp_show.Para_info;
import ru.agpu.artikproject.background_work.theme.GetTextColor;
import ru.agpu.artikproject.background_work.theme.Theme;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<RecyclerViewItems> datas;
    private final Activity act;
    private final LayoutInflater mLayoutInflater;

    public RecyclerViewAdapter(Activity act, List<RecyclerViewItems> datas) {
        this.act = act;
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(act.getApplicationContext());
    }
    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        // Inflate view from recyclerview_item_layout.xml
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.recyclerview_item_rasp_day_show, parent, false);

        // Слушатель нажатий на элемент списка

        recyclerViewItem.setOnClickListener(v -> {
            RecyclerView recyclerView = (RecyclerView) parent;
            int itemPosition = recyclerView.getChildLayoutPosition(v); // Получаем позицию нажатого элемента
            new Para_info(itemPosition, act, datas);
        });

        return new RecyclerViewHolder(recyclerViewItem);
    }

    // Здесь мы настраиваем наши маленькие карточки
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        if (position == 0){ // Если на входе первый элемент, меняем цвет фона
            if (Theme.getApplicationTheme(act) == AppCompatDelegate.MODE_NIGHT_NO) // Если тема дневная
                act.findViewById(R.id.day_para_view_rec).setBackgroundColor(act.getColor(R.color.white)); // Ставим светлый фон
            else // Если тема ночная
                act.findViewById(R.id.day_para_view_rec).setBackgroundColor(act.getColor(R.color.black)); // Ставим темный фон
        }

        RecyclerViewItems item = this.datas.get(position);

        holder.card_para_number.setText(item.getCard_para_number());
        holder.card_para_time.setText(item.getCard_para_time());
        holder.card_para_name.setText(item.getCard_para_name());
        holder.card_para_aud.setText(item.getCard_para_aud());
        holder.card_para_prepod.setText(item.getCard_para_prepod());
        holder.para_num_and_time_layout.setBackgroundColor(item.getPara_num_and_time_layout_color());
        holder.para_description_layout.setBackgroundColor(item.getPara_description_layout_color());

        int textColor = GetTextColor.getAppColor(act.getApplicationContext());
        holder.card_para_number.setTextColor(textColor);
        holder.card_para_time.setTextColor(textColor);
        holder.card_para_name.setTextColor(textColor);
        holder.card_para_aud.setTextColor(textColor);
        holder.card_para_prepod.setTextColor(textColor);
    }
}
