package ru.agpu.artikproject.presentation.layout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
import ru.agpu.artikproject.databinding.FragmentMainActivitySelectGroupOrDirectionBinding

/**
 * Экран поиска расписания по группе/факультету/направлению
 */
class SelectGroupDirectionFacultyFragment: Fragment(R.layout.fragment_main_activity_select_group_or_direction) {
    private var binding: FragmentMainActivitySelectGroupOrDirectionBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainActivitySelectGroupOrDirectionBinding.inflate(inflater, container, false)
        binding?.facBtn?.setOnClickListener {
            RecyclerviewShowFragment.SELECTED_LIST = 2
            FRAGMENT = BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, RecyclerviewShowFragment::class.java, null)
                ?.commit()
        }
        binding?.dirBtn?.setOnClickListener {
            FRAGMENT = BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, SelectTrainingFragment::class.java, null)
                ?.commit()
        }
        binding?.grpBtn?.setOnClickListener {
            FRAGMENT = BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, SelectGroupFragment::class.java, null)
                ?.commit()
        }
        return binding?.root
    }
}