package ru.agpu.artikproject.background_work.admin_panel

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements

class FragmentShowListDirections: Fragment(R.layout.fragment_admin_panel_show_list_directions) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val openAllFichaCB = view.findViewById<CheckBox>(R.id.open_all_ficha)
        openAllFichaCB.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked) {
                true -> FichaAchievements.putAllFicha(context)
                false -> FichaAchievements.removeAllFicha(context)
            }
        }
    }
}