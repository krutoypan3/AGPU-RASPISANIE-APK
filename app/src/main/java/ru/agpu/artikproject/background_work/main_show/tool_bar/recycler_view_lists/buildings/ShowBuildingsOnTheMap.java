package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;


public class ShowBuildingsOnTheMap {
    /**
     * Слушатель нажатия на список аудиторий и корпусов
     * @param position Позиция выбранного элелемента
     * @param act Активити
     */
    public ShowBuildingsOnTheMap(int position, Activity act){
        Intent intent;
        switch (position) {
            case (1):
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/eQo5R9LdnCprwCvs6"));
                break;
            case (2):
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/KwDaKEg3w69a5xuy5"));
                break;
            case (3):
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/rtrpMGCP5t3E1FDU8"));
                break;
            case (4):
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/DhAqdFucRB5RgF8H6"));
                break;
            case (5):
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/m1Ddk1RmebQ9CK5P9"));
                break;
            case (6):
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/4fBCRhZPJbc7z4Ng6"));
                break;
            default:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/8Lv4W8uds3cFAeW36")); // 0 нулевое
                break;
        }
        act.startActivity(intent);
    }
}
