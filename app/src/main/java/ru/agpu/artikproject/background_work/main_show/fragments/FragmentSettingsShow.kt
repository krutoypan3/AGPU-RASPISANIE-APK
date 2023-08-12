package ru.agpu.artikproject.background_work.main_show.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckAppUpdate
import ru.agpu.artikproject.background_work.CustomDialog
import ru.agpu.artikproject.background_work.CustomDialogType
import ru.agpu.artikproject.background_work.debug.DeviceInfo
import ru.agpu.artikproject.background_work.debug.LeaveReview
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaShow
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.background_work.settings_layout.theme_radio_group.ThemeRadioButtonSetting
import ru.agpu.artikproject.background_work.settings_layout.theme_radio_group.ThemeRadioGroupListener
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundCheckBoxListener
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundCheckBoxSetting
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundPictureSetting
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserBackgroundSelectorListener
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserSeekBarListener
import ru.agpu.artikproject.background_work.settings_layout.user_background.UserSeekBarSetting
import ru.agpu.artikproject.presentation.layout.Admin_panel

class FragmentSettingsShow: Fragment(R.layout.fragment_main_activity_settings_show) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = view.context as Activity

        val userBackgroundCheckBox = view.findViewById<CheckBox>(R.id.my_app_background_checkbox)
        val userBackgroundLightSelector = view.findViewById<ImageView>(R.id.background_light_image_selector)
        val userBackgroundDarkSelector = view.findViewById<ImageView>(R.id.background_dark_image_selector)

        // Установка фона для картинок с фонами
        UserBackgroundPictureSetting(
            activity,
            userBackgroundLightSelector,
            userBackgroundDarkSelector
        )

        // Проверка количества собранных пасхалок
        FichaShow(activity)

        // Установка позиции themeRadioButton
        ThemeRadioButtonSetting(activity)

        // Прослушка нажатий на themeRadioGroup
        ThemeRadioGroupListener(activity)

        // Установка позиции в userBackgroundCheckBox
        UserBackgroundCheckBoxSetting(activity, userBackgroundCheckBox)

        // Прослушка нажатий на userBackgroundCheckBox
        UserBackgroundCheckBoxListener(activity, userBackgroundCheckBox)

        // Установка ползунка светлого затемнителя в нужное положение
        UserSeekBarSetting(activity, view.findViewById(R.id.seekBarLight), true)

        // Установка ползунка темного затемнителя в нужное положение
        UserSeekBarSetting(activity, view.findViewById(R.id.seekBarDark), false)

        // Прослушка изменения ползунка светлого затемнителя
        UserSeekBarListener(activity, view.findViewById(R.id.seekBarLight), true)

        // Прослушка изменения ползунка темного затемнителя
        UserSeekBarListener(activity, view.findViewById(R.id.seekBarDark), false)

        // Прослушка нажатий на BackgroundDarker (Ficha Settings Logo)
        FichaAchievements().playSettingsLogoFicha(activity)

        // Прослушка нажатий на userBackgroundLightSelector
        UserBackgroundSelectorListener(activity, userBackgroundLightSelector, "background_light")

        // Прослушка нажатий на userBackgroundDarkSelector
        UserBackgroundSelectorListener(activity, userBackgroundDarkSelector, "background_dark")

        // Отслеживание нажатий на кнопку условий использования
        view.findViewById<View>(R.id.terms_of_use_btn).setOnClickListener {
            val cdd = CustomDialog(activity, CustomDialogType.ABOUT)
            cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
            cdd.show()
        }

        // Отслеживание нажатий на кнопку оставления отзыва
        view.findViewById<View>(R.id.leave_a_review).setOnClickListener { LeaveReview(activity) }

        val aboutVersionText = view.findViewById<TextView>(R.id.about_version_text)
        try {
            var newVersionText = getString(R.string.app_name) + " " + DeviceInfo.getAppVersion(view.context)
            if (CheckAppUpdate.isAppHaveUpdate) {
                newVersionText += " " + getString(R.string.an_update_is_available)
            }
            aboutVersionText.text = newVersionText
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        // Отслеживание нажатий на кнопку гитхаба
        view.findViewById<View>(R.id.GitHub_btn).setOnClickListener {
            startActivity(Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases")
            ))
        }
        view.findViewById<View>(R.id.GitHub_btn).setOnLongClickListener {
            FichaAchievements().playWindowsFicha(activity)
            true
        }

        view.findViewById<View>(R.id.admin_btn).setOnClickListener {
            startActivity(Intent(view.context, Admin_panel::class.java))
        }
    }
}