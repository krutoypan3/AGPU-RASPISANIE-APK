package ru.agpu.artikproject.presentation.layout.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY

class SelectGroupDirectionFacultyFragment: Fragment(R.layout.fragment_main_activity_select_group_or_direction) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.fac_btn).setOnClickListener {
            RecyclerviewShowFragment.SELECTED_LIST = 2
            FRAGMENT = BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, RecyclerviewShowFragment::class.java, null)
                ?.commit()
        }
        view.findViewById<View>(R.id.dir_btn).setOnClickListener {
            FRAGMENT = BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, SelectTrainingFragment::class.java, null)
                ?.commit()
        }
        view.findViewById<View>(R.id.grp_btn).setOnClickListener {
            FRAGMENT = BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, SelectGroupFragment::class.java, null)
                ?.commit()
        }
    }
}