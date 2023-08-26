package ru.agpu.artikproject.background_work.start_activity_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_GROUP
import ru.agpu.artikproject.presentation.layout.StartActivity

class FragmentGroupHelp: Fragment(R.layout.fragment_start_activity_group_help) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StartActivity.FRAGMENT = BACK_TO_GROUP
    }
}