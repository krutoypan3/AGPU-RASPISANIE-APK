package ru.agpu.artikproject.background_work.adapters.recycler_view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.debug.Device_info;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.BuildingsItemClick;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.FacultiesItemClick;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.faculties.groups.GroupsItemClick;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.weeks.WeeksItemClick;
import ru.agpu.artikproject.background_work.theme.GetTextColor;
import ru.agpu.artikproject.background_work.zach_book.GetMarkImageUrl;
import ru.agpu.artikproject.layout.MainActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private final List<RecyclerViewItems> datas;
    private final Activity act;
    private final LayoutInflater mLayoutInflater;
    private final int adapter_is;
    public static final int IS_BUILDINGS_ADAPTER = 0;
    public static final int IS_FACULTIES_ADAPTER = 1;
    public static final int IS_FACULTIES_GROUPS_ADAPTER = 2;
    public static final int IS_WEEKS_ADAPTER = 3;
    public static final int IS_MARK_ADAPTER = 4;
    public static int selected_faculties_position;
    public static String selected_faculties_logos;

    public RecyclerViewAdapter(Activity act, List<RecyclerViewItems> datas, int adapter_is) {
        this.act = act;
        this.datas = datas;
        this.mLayoutInflater = LayoutInflater.from(act.getApplicationContext());
        this.adapter_is = adapter_is;
    }


    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        // Inflate view from recyclerview_item_layout.xml
        View recyclerViewItem = mLayoutInflater.inflate(R.layout.recyclerview_item_layout, parent, false);

        // Слушатель нажатий на элемент списка
        recyclerViewItem.setOnClickListener(v -> {
            switch (adapter_is){
                case IS_BUILDINGS_ADAPTER:
                    new BuildingsItemClick((RecyclerView)parent, v, datas, act);
                    break;
                case IS_FACULTIES_ADAPTER:
                    new FacultiesItemClick((RecyclerView)parent, v, datas, act);
                    parent.startAnimation(MainActivity.animUehalVlPriehalSprava);
                    break;
                case IS_FACULTIES_GROUPS_ADAPTER:
                    new GroupsItemClick((RecyclerView)parent, v, act);
                    parent.startAnimation(MainActivity.animUehalVl);
                    break;
                case IS_WEEKS_ADAPTER:
                    new WeeksItemClick((RecyclerView)parent, v, datas, act);
                    parent.startAnimation(MainActivity.animUehalVl);
                    break;
            }
        });

        return new RecyclerViewHolder(recyclerViewItem);
    }

    // Здесь мы настраиваем наши маленькие карточки
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Cet item in countries via position
        RecyclerViewItems item = this.datas.get(position);
        int text_color = GetTextColor.getAppColor(act.getApplicationContext());
        String imageResUrl;
        if (adapter_is == IS_MARK_ADAPTER){
            String subtext = act.getString(R.string.Semester) + ": " + item.getMainText() + ", " + act.getString(R.string.Teacher) + ": " + item.getSubText3();
            String maintext = item.getSubText4() + " - " + item.getImageResourceUrl();
            holder.subTextView.setText(subtext);
            holder.mainTextView.setText(maintext);
            imageResUrl = new GetMarkImageUrl().get(item.getSubText2());
            holder.cardView.getLayoutParams().height = 50;
            holder.image.getLayoutParams().height = 50;
            holder.cardView.getLayoutParams().width = 50;
            holder.image.getLayoutParams().width = 50;
            holder.mainTextView.setTextColor(text_color);
            holder.mainTextView.setPadding(5,0,5,0);
            holder.subTextView.setPadding(5,10,5,0);
            holder.subTextView.setTextColor(text_color);
        }
        else {
            imageResUrl = item.getImageResourceUrl();
            holder.mainTextView.setText(item.getMainText());
            int textColor = GetTextColor.getAppColor(act.getApplicationContext());
            if (textColor == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                textColor = GetTextColor.getAppColor(act.getApplicationContext());
            holder.mainTextView.setTextColor(textColor);
            String newSubText = item.getSubText();
            holder.subTextView.setText(newSubText);
            holder.subTextView.setTextColor(textColor);
        }
        Glide.with(act.getApplicationContext())
                .load(imageResUrl)
                .apply(new RequestOptions().override(Device_info.getDeviceWidth(act.getApplicationContext()), 360))
                .placeholder(R.drawable.agpu_ico)
                .into(holder.image);
    }

}