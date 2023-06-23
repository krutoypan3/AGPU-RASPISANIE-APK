package ru.agpu.artikproject.background_work.main_show.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentSelectGroup
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentSelectTraining
import ru.agpu.artikproject.presentation.layout.MainActivity

class FragmentSelectGroupDirectionFaculty: Fragment(R.layout.fragment_main_activity_select_group_or_direction) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.fac_btn).setOnClickListener {
            FragmentRecyclerviewShow.SELECTED_LIST = 2
            MainActivity.FRAGMENT = MainActivity.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            MainActivity.fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, FragmentRecyclerviewShow::class.java, null)
                .commit()
        }
        view.findViewById<View>(R.id.dir_btn).setOnClickListener {
            MainActivity.FRAGMENT = MainActivity.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            MainActivity.fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, FragmentSelectTraining::class.java, null)
                .commit()
        }
        view.findViewById<View>(R.id.grp_btn).setOnClickListener {
            MainActivity.FRAGMENT = MainActivity.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            MainActivity.fragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container_view, FragmentSelectGroup::class.java, null)
                .commit()
        }
    }
}