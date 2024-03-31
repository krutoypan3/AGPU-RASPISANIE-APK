package ru.agpu.artikproject.presentation.layout.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.GridLayout
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckAppUpdate
import ru.agpu.artikproject.background_work.CustomDialog
import ru.agpu.artikproject.background_work.CustomDialogType
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.background_work.debug.DeviceInfo
import ru.agpu.artikproject.background_work.debug.LeaveReview
import ru.agpu.artikproject.background_work.image_utils.ImageSelector
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaShow
import ru.agpu.artikproject.background_work.theme.Theme
import ru.agpu.artikproject.databinding.FragmentMainActivitySettingsShowBinding
import ru.agpu.artikproject.presentation.layout.activity.AdminPanelActivity

class SettingsFragment: Fragment(R.layout.fragment_main_activity_settings_show) {
    private var binding: FragmentMainActivitySettingsShowBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainActivitySettingsShowBinding.inflate(inflater, container, false)

        settingBackground()                                                             // Настройка фона для картинок с фонами
        binding?.root?.let { view -> FichaShow(view) }                                  // Проверка количества собранных пасхалок
        settingThemeRadioGroupListener()                                                // Настройка и прослушка нажатий на кнопки смены темы
        settingUserBackgroundCheckBoxListener()                                         // Настройка и прослушка нажатий на кнопку включения пользовательского фона
        settingUserSeekBarListener()                                                    // Настройка и прослушка нажатий на ползунок затемнения пользовательского фона
        binding?.root?.let { view -> FichaAchievements().playSettingsLogoFicha(view) }  // Прослушка нажатий на BackgroundDarker (Ficha Settings Logo)
        binding?.termsOfUseBtn?.setOnClickListener {                                    // Отслеживание нажатий на кнопку условий использования
            val cdd = CustomDialog(binding?.root?.context as Activity, CustomDialogType.ABOUT)
            cdd.window!!.setBackgroundDrawableResource(R.drawable.custom_dialog_background)
            cdd.show()
        }

        binding?.leaveAReview?.setOnClickListener { binding?.root?.let { view -> LeaveReview(view) } }

        try {
            val newVersionText = "${getString(R.string.app_name)} ${DeviceInfo.getAppVersion(requireContext())}"
            if (CheckAppUpdate.isAppHaveUpdate) binding?.aboutVersionText?.text = "$newVersionText ${getString(R.string.an_update_is_available)}"
        } catch (_: PackageManager.NameNotFoundException) { }

        // Отслеживание нажатий на кнопку гитхаба
        binding?.GitHubBtn?.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/krutoypan3/AGPU-RASPISANIE-APK/releases"))) }
        binding?.GitHubBtn?.setOnLongClickListener {
            binding?.root?.let { view -> FichaAchievements().playWindowsFicha(view) }
            true
        }

        binding?.adminBtn?.setOnClickListener { startActivity(Intent(context, AdminPanelActivity::class.java)) }

        return binding?.root
    }

    private fun settingUserSeekBarListener() {
        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (seekBar.id == R.id.seekBarLight) { // Если светлый ползунок
                    val color = Color.argb((seekBar.progress * 2.5).toInt(), 255, 255, 255) // Делаем затемнитель
                    binding?.backgroundLightImageSelectorDarker?.setBackgroundColor(color) // Затемняем мини-фон
                } else { // Если темный ползунок
                    val color = Color.argb((seekBar.progress * 2.5).toInt(), 0, 0, 0) // Делаем засветлитель
                    binding?.backgroundDarkImageSelectorDarker?.setBackgroundColor(color) // Затемняем мини-фон
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // После того как пользователь отпустит ползунок, сохраняем новые значения затемнителя
                if (seekBar.id == R.id.seekBarLight) // Если светлый ползунок - сохраняем засветлитель
                    MySharedPreferences.putPref(context, "light_darker_level", seekBar.progress)
                else  // Если темный ползунок - сохраняем затемнитель
                    MySharedPreferences.putPref(context, "dark_darker_level", seekBar.progress)
            }
        }
        binding?.seekBarLight?.setOnSeekBarChangeListener(listener)
        binding?.seekBarDark?.setOnSeekBarChangeListener(listener)

        // Начальная настройка ползунков
        // Ползунок светлый
        val levelLight = MySharedPreferences.getPref(context, "light_darker_level", 30) // Получаем сохраненный уровень ползунка (если его нет, то ставим 30%)
        binding?.seekBarLight?.progress = levelLight // Устанавливаем полученный уровень
        binding?.backgroundLightImageSelectorDarker?.setBackgroundColor(Color.argb((levelLight * 2.5).toInt(), 255, 255, 255)) // Затемняем мини-фон
        // Ползунок темный
        val levelDark = MySharedPreferences.getPref(context, "dark_darker_level", 30) // Получаем сохраненный уровень ползунка (если его нет, то ставим 30%)
        binding?.seekBarDark?.progress = levelDark // Устанавливаем полученный уровень
        binding?.backgroundDarkImageSelectorDarker?.setBackgroundColor(Color.argb((levelDark * 2.5).toInt(), 0, 0, 0)) // Затемняем мини-фон

    }

    /**
     * Настраивает слушатель нажатий на userBackgroundCheckBox
     */
    private fun settingUserBackgroundCheckBoxListener() {
        if (MySharedPreferences.getPref(context, "enable_background_user", false)) {
            binding?.myAppBackgroundCheckbox?.isChecked = true
        }
        binding?.myAppBackgroundCheckbox?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            MySharedPreferences.putPref(context, "enable_background_user", isChecked)
            if (isChecked) { // Если включаем видимость
                binding?.layoutChangeBackground?.visibility = View.VISIBLE // Делаем фон видимым
                binding?.layoutChangeBackground?.layoutParams?.width = GridLayout.LayoutParams.WRAP_CONTENT // Растягиваем по размеру элементов
                binding?.layoutChangeBackground?.layoutParams?.height = GridLayout.LayoutParams.WRAP_CONTENT // Растягиваем по размеру элементов
            } else { // Если выключаем видимость
                binding?.layoutChangeBackground?.visibility = View.GONE // Делаем фон невидимым
            }
            binding?.layoutChangeBackground?.requestLayout() // Обновляем элемент на слое
        }
    }

    private fun settingThemeRadioGroupListener() {
        when (Theme.getTheme(context)) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> binding?.themeSystem?.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding?.themeDark?.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> binding?.themeLight?.isChecked = true
        }
        binding?.themeRadioGroup?.setOnCheckedChangeListener { _: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.theme_system -> Theme.setTheme(context, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                R.id.theme_dark -> Theme.setTheme(context, AppCompatDelegate.MODE_NIGHT_YES)
                R.id.theme_light -> Theme.setTheme(context, AppCompatDelegate.MODE_NIGHT_NO)
            }
            Toast.makeText(context, R.string.theme_apply, Toast.LENGTH_SHORT).show()
        }
    }

    private fun settingBackground() {
        val backgroundLight = MySharedPreferences.getPref(context, "background_light", "")
        if (backgroundLight.isNotBlank()) { // Если светлая картинка есть
            binding?.backgroundLightImageSelector?.setImageURI(null) // Без обнуления новая картинка не встанет
            binding?.backgroundLightImageSelector?.setImageURI(Uri.parse(backgroundLight)) // Установка новой картинки
        }
        val backgroundDark = MySharedPreferences.getPref(context, "background_dark", "")
        if (backgroundDark.isNotBlank()) { // Если темная картинка есть
            binding?.backgroundDarkImageSelector?.setImageURI(null) // Без обнуления новая картинка не встанет
            binding?.backgroundDarkImageSelector?.setImageURI(Uri.parse(backgroundDark)) // Установка новой картинки
        }

        binding?.backgroundLightImageSelector?.setOnClickListener {
            val intent = Intent(context, ImageSelector::class.java)
            intent.putExtra("background", "background_light")
            activity?.startActivity(intent)
        }
        binding?.backgroundDarkImageSelector?.setOnClickListener {
            val intent = Intent(context, ImageSelector::class.java)
            intent.putExtra("background", "background_dark")
            activity?.startActivity(intent)
        }
    }
}