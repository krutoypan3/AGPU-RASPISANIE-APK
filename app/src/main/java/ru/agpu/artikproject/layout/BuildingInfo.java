package ru.agpu.artikproject.layout;

import android.annotation.SuppressLint;
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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.Random;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.CustomBackground;
import ru.agpu.artikproject.background_work.OnSwipeTouchListener;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.ShowBuildingsOnTheMap;
import ru.agpu.artikproject.background_work.settings_layout.ficha.Ficha_achievements;

/**
 * Данное активити отвечает за отображение подробной информации о корпусах и аудиториях
 */
public class BuildingInfo extends AppCompatActivity {

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        findViewById(R.id.btn_building_info).setVisibility(View.INVISIBLE);
        findViewById(R.id.building_info_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.cardBackgroundDarker).setVisibility(View.INVISIBLE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.building_info);
        // Это нужно для предотвращения мерцания при анимации
        getWindow().setEnterTransition(null);

        // Установка нового фона и затемнителя | Должно быть после setContentView
        RelativeLayout layout = findViewById(R.id.building_info_layout);
        layout.setBackground(CustomBackground.getBackground(getApplicationContext()));
        findViewById(R.id.cardBackgroundDarker).setBackgroundColor(CustomBackground.getBackgroundDarker(getApplicationContext()));

        // Настраиваем основной текст
        String mainText = getIntent().getStringExtra("mainText"); // Получаем переданное название корпуса
        TextView mainTextView = findViewById(R.id.cardViewAudMainText_second); // Находим TextEdit на слое
        mainTextView.setText(mainText); // Устанавливаем наш mainText

        // Настраиваем дополнительный текст
        String subText = getIntent().getStringExtra("subText"); // Получаем переданный список аудиторий
        TextView subTextView = findViewById(R.id.cardViewAudSubText_second); // Находим TextEdit на слое
        subTextView.setText(subText); // Устанавливаем наш subText

        // Настраиваем картинку
        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("picture");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        Drawable d = new BitmapDrawable(getResources(), bmp);

        ImageView imageView = findViewById(R.id.cardViewAudImage_second); // Находим нашу вьюшку (ImageView) на слое

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("picture_url")).placeholder(d).into(imageView);


        // Обработка кнопки перехода на карту
        int itemPosition = getIntent().getIntExtra("itemPosition", 0); // Получем переданный id позиции элемента
        findViewById(R.id.btn_building_info).setOnClickListener(v -> // И при нажатии на кнопку
                new ShowBuildingsOnTheMap(itemPosition, this));  // Открываем карты

        // Отслеживание нажатий на иконку университета в тулбаре (фича)
        mainTextView.setOnClickListener(v -> {
            mainTextView.startAnimation(MainActivity.animScale);
            int random_int = new Random().nextInt(30);
            if (random_int == 0){
                Ficha_achievements.put(getApplicationContext(), "ficha_building_main_text");
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                MediaPlayer mp = MediaPlayer.create(this, R.raw.amogus);
                mp.start();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/HFbRBrx/3.jpg"));
                startActivity(intent);
            }
        });
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeLeft() {
                imageView.startAnimation(MainActivity.animUehalVl);
                int random_int = new Random().nextInt(10);
                if (random_int == 0){
                    Ficha_achievements.put(getApplicationContext(), "ficha_building_ico");
                    AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 30, 0);
                    MediaPlayer mp = MediaPlayer.create(getApplication(), R.raw.winx);
                    mp.start();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://i.ibb.co/rZvW4Mj/yao-min-65474631-orig.jpg"));
                    startActivity(intent);
                }
            }
        });
    }
}
