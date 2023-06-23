package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings

import android.app.Activity
import android.content.Intent
import android.net.Uri

/**
 * Слушатель нажатия на список аудиторий и корпусов
 * @param position Позиция выбранного элелемента
 * @param act Активити
 */
class ShowBuildingsOnTheMap(position: Int, act: Activity) {
    init {
        val intent = when (position) {
            LoadBuildingsList.BUILDING_ZAOCHKA -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/eQo5R9LdnCprwCvs6"))
            LoadBuildingsList.BUILDING_SPF -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/KwDaKEg3w69a5xuy5"))
            LoadBuildingsList.BUILDING_FOC -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/DhAqdFucRB5RgF8H6"))
            LoadBuildingsList.BUILDING_TEHFAK -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/m1Ddk1RmebQ9CK5P9"))
            LoadBuildingsList.BUILDING_DORMITORY -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/4fBCRhZPJbc7z4Ng6"))
            LoadBuildingsList.BUILDING_ISTFAK -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/8SYEY7YwjHLHmtt9A"))
            else -> Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/8Lv4W8uds3cFAeW36")) // 0 нулевое
        }
        act.startActivity(intent)
    }
}