package com.example.artikproject.background_work;

import android.content.Context;
import android.widget.RelativeLayout;

import com.example.artikproject.R;
import com.example.artikproject.background_work.datebase.MySharedPreferences;

public class SetNewBackground {
    public static void set(Context context, int resource_id){
        MySharedPreferences.put(context, "background_id", resource_id);
    }

    public static void setting(RelativeLayout layout){
        try { // Устанавливаем новый фон
            layout.setBackgroundResource(MySharedPreferences.get(
                    layout.getContext(), "background_id", R.drawable.background));
        }
        catch (Exception e){ // Если фон был поменян на новый(новая картинка),
            layout.setBackgroundResource(R.drawable.background); // то ставим стандартный фон
            set(layout.getContext(), R.drawable.background);
            MySharedPreferences.remove(layout.getContext(), "is_event_background"); // удаляем галочку с checkbox
        }
    }
}
