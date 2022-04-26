package com.example.artikproject.background_work.settings_layout;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.example.artikproject.R;
import com.example.artikproject.background_work.Ficha_achievements;

public class FichaShow extends Thread{
    Activity act;
    static MediaPlayer mp;
    public FichaShow(Activity act){
        this.act = act;
    }

    @Override
    public void run() {
        TextView ficha_count_text = act.findViewById(R.id.ficha_count_text);
        TextView ficha_count = act.findViewById(R.id.ficha_count);
        ImageView ficha_nyan = act.findViewById(R.id.ficha_nyan);
        int fic_count = Ficha_achievements.get(act.getApplicationContext());
        if (fic_count > 0){
            ficha_count_text.setVisibility(View.VISIBLE);
            ficha_count.setVisibility(View.VISIBLE);
            String newText = fic_count + " / " + Ficha_achievements.MAX_FICHA_COUNT;
            act.runOnUiThread(() -> ficha_count.setText(newText));
        }
        if (fic_count == 8){
            act.runOnUiThread(() -> ficha_count_text.setText("Пасхалки?.Ты собрал их все: "));
            if (mp == null) mp = MediaPlayer.create(act.getApplicationContext(), R.raw.nyan_cat);
            else if (!mp.isPlaying()){
                mp = MediaPlayer.create(act.getApplicationContext(), R.raw.nyan_cat);
                mp.start();}
            ficha_nyan.setVisibility(View.VISIBLE);
            Glide.with(act.getApplicationContext()).load("https://www.nyan.cat/cats/original.gif").into(new DrawableImageViewTarget(ficha_nyan));
        }
    }

}
