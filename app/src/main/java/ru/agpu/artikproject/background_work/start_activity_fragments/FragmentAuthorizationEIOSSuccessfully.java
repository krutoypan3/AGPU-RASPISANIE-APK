package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.layout.MainActivity;
import ru.agpu.artikproject.layout.StartActivity;

public class FragmentAuthorizationEIOSSuccessfully extends Fragment {

    public FragmentAuthorizationEIOSSuccessfully() {
        super(R.layout.fragment_start_activity_authorization_eios_successfully);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Установка нового фона [и ТУТ НЕТ затемнителя] | Должно быть после setContentView
        view.findViewById(R.id.fragment_activity_start_layout).setBackground(CustomBackground.getBackground(view.getContext()));

        Activity act = (Activity) view.getContext();

        TextView main_text = view.findViewById(R.id.main_text);
        String new_text = getString(R.string.Welcome) + ", " + MySharedPreferences.get(view.getContext(), "user_info_first_name", "");
        main_text.setText(new_text);

        TextView textView = view.findViewById(R.id.Your_group);
        new_text = getString(R.string.Your_group) + ": " + MySharedPreferences.get(view.getContext(), "user_info_group_name", "");
        textView.setText(new_text);

        StartActivity.FRAGMENT = StartActivity.BACK_TO_EIOS;

        // Да, перейти к расписанию
        view.findViewById(R.id.Yes_go_to_the_schedule_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.Yes_go_to_the_schedule_btn).setClickable(false);
            view.findViewById(R.id.Yes_go_to_the_schedule_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            Intent intent = new Intent(act.getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("start_rasp", true);
            intent.putExtra("selectedItem_id", MySharedPreferences.get(view.getContext(), "user_info_group_id", ""));
            intent.putExtra("selectedItem_type", "Group");
            intent.putExtra("selectedItem", MySharedPreferences.get(view.getContext(), "user_info_group_name", ""));
            act.startActivity(intent);
        });

        // Нет, найти расписание
        view.findViewById(R.id.No_find_a_schedule_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.No_find_a_schedule_btn).setClickable(false);
            view.findViewById(R.id.No_find_a_schedule_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentGroup.class, null).commit();
        });
    }
}
