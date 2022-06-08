package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.layout.StartActivity;

public class FragmentEIOS extends Fragment {

    public FragmentEIOS() {
        super(R.layout.fragment_start_activity_eios);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StartActivity.FRAGMENT = StartActivity.BACK_TO_WELCOME;

        // Да мне выдали данные для ЭИОС
        view.findViewById(R.id.yes_i_have_data_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.yes_i_have_data_btn).setClickable(false);
            view.findViewById(R.id.yes_i_have_data_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentAuthorizationEIOS.class, null).commit();
        });

        // Я не уверен
        view.findViewById(R.id.Im_not_sure_btn).setOnClickListener(view1 -> {
            view.findViewById(R.id.Im_not_sure_btn).setClickable(false);
            view.findViewById(R.id.Im_not_sure_btn).startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.scale));
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentGroup.class, null).commit();
        });

    }

}

