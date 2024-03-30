package ru.agpu.artikproject.presentation.layout.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewPhotoAdapter
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animScale
import ru.agpu.artikproject.background_work.datebase.BuildingsRepository
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.background_work.theme.CustomBackground

class BuildingInfoFragment : Fragment(R.layout.fragment_main_activity_building_info) {

    companion object {
        var itemPosition = 0
        var mainText: String? = null
        var subText: String? = null
        var pictureByteArrayOutputArray: ByteArray = ByteArray(0)
        var pictureUrl: String? = null
        var mapUrl: String? = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка нового фона и затемнителя | Должно быть после setContentView
        val layout = view.findViewById<RelativeLayout>(R.id.building_info_layout)
        layout.background = CustomBackground.getBackground(view.context)
        view.findViewById<View>(R.id.cardBackgroundDarker)
            .setBackgroundColor(CustomBackground.getBackgroundDarker(view.context))

        // Настраиваем основной текст
        val mainTextView = view.findViewById<TextView>(R.id.cardViewAudMainText_second) // Находим TextEdit на слое
        mainTextView.text = mainText // Устанавливаем наш mainText

        // Настраиваем дополнительный текст
        val subTextView = view.findViewById<TextView>(R.id.cardViewAudSubText_second) // Находим TextEdit на слое
        subTextView.text = subText // Устанавливаем наш subText

//        val imageView = view.findViewById<ImageView>(R.id.cardViewAudImage_second) // Находим нашу вьюшку (ImageView) на слое

        val building = BuildingsRepository().getAll().firstOrNull {
            mainText == it.buildingName + " по " + it.buildingAddress
        }

        val recyclerViewImages = view.findViewById<RecyclerView>(R.id.photos)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        recyclerViewImages.layoutManager = layoutManager
        recyclerViewImages.adapter = RecyclerViewPhotoAdapter(requireContext(), building?.buildingsPhotosUrl)


        // Обработка кнопки перехода на карту
        view.findViewById<View>(R.id.btn_building_info).setOnClickListener { // И при нажатии на кнопку
            Log.i("Building", "user go to: ${building?.buildingAddressMapUrl}")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(building?.buildingAddressMapUrl ?: ""))
            activity?.startActivity(intent)
        } // Открываем карты

        // Отслеживание нажатий на иконку университета в тулбаре (фича)
        mainTextView.setOnClickListener {
            mainTextView.startAnimation(animScale)
            FichaAchievements().playFichaBuildingMainText(view.context)
        }
    }
}