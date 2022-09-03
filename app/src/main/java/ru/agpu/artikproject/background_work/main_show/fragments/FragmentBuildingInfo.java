package ru.agpu.artikproject.background_work.main_show.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.OnSwipeTouchListener;
import ru.agpu.artikproject.background_work.debug.Device_info;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.ShowBuildingsOnTheMap;
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements;
import ru.agpu.artikproject.background_work.theme.CustomBackground;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class FragmentBuildingInfo extends Fragment {

    public static int itemPosition;
    public static String mainText;
    public static String subText;
    public static byte[] pictureByteArrayOutputArray;
    public static String pictureUrl;

    public FragmentBuildingInfo() {
        super(R.layout.fragment_main_activity_building_info);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        RelativeLayout layout = view.findViewById(R.id.building_info_layout);
        layout.setBackground(CustomBackground.getBackground(view.getContext()));
        view.findViewById(R.id.cardBackgroundDarker).setBackgroundColor(CustomBackground.getBackgroundDarker(view.getContext()));

        // Настраиваем основной текст
        TextView mainTextView = view.findViewById(R.id.cardViewAudMainText_second); // Находим TextEdit на слое
        mainTextView.setText(mainText); // Устанавливаем наш mainText

        // Настраиваем дополнительный текст
        TextView subTextView = view.findViewById(R.id.cardViewAudSubText_second); // Находим TextEdit на слое
        subTextView.setText(subText); // Устанавливаем наш subText

        // Настраиваем картинку
        Bitmap bmp = BitmapFactory.decodeByteArray(pictureByteArrayOutputArray, 0, pictureByteArrayOutputArray.length);
        Drawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);

        ImageView imageView = view.findViewById(R.id.cardViewAudImage_second); // Находим нашу вьюшку (ImageView) на слое

        Glide.with(view.getContext())
                .load(pictureUrl)
                .apply(new RequestOptions().override(Device_info.getDeviceWidth(view.getContext()), 960))
                .dontTransform()
                .placeholder(bitmapDrawable)
                .into(imageView);


        // Обработка кнопки перехода на карту
        view.findViewById(R.id.btn_building_info).setOnClickListener(v -> // И при нажатии на кнопку
                new ShowBuildingsOnTheMap(itemPosition, (Activity) view.getContext()));  // Открываем карты

        // Отслеживание нажатий на иконку университета в тулбаре (фича)
        mainTextView.setOnClickListener(v -> {
            mainTextView.startAnimation(MainActivity.animScale);
            int random_int = new Random().nextInt(30);
            if (random_int == 0){
                Ficha_achievements.put(view.getContext(), "ficha_building_main_text");
                AudioManager audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                MediaPlayer mp = MediaPlayer.create(view.getContext(), R.raw.amogus);
                mp.start();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/HFbRBrx/3.jpg"));
                startActivity(intent);
            }
        });
        imageView.setOnTouchListener(new OnSwipeTouchListener(view.getContext()) {
            public void onSwipeLeft() {
                imageView.startAnimation(MainActivity.animUehalVl);
                int random_int = new Random().nextInt(10);
                if (random_int == 0){
                    Ficha_achievements.put(view.getContext(), "ficha_building_ico");
                    AudioManager audioManager = (AudioManager) view.getContext().getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                    MediaPlayer mp = MediaPlayer.create(view.getContext(), R.raw.winx);
                    mp.start();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/rZvW4Mj/yao-min-65474631-orig.jpg"));
                    startActivity(intent);
                }
            }
        });

    }
}
