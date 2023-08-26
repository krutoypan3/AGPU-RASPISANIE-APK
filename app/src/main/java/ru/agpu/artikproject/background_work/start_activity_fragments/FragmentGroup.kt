package ru.agpu.artikproject.background_work.start_activity_fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_WELCOME
import ru.agpu.artikproject.presentation.layout.StartActivity

/**
 * Фрагмент, на котором предоставляется выбор: "Выбор группы" или "Выбор направления"
 */
class FragmentGroup: Fragment(R.layout.fragment_start_activity_group) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StartActivity.FRAGMENT = BACK_TO_WELCOME

        // Кнопка "Я знаю название группы"
        val groupNameBtn = view.findViewById<View>(R.id.I_know_group_name_btn)
        groupNameBtn.setOnClickListener {
            groupNameBtn.isClickable = false
            groupNameBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, FragmentSelectGroup::class.java, null)
                .commit()
        }

        // Кнопка "Я знаю направление подготовки"
        val directionBtn = view.findViewById<View>(R.id.I_know_the_direction_of_training_btn)
        directionBtn.setOnClickListener {
            directionBtn.isClickable = false
            directionBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, FragmentSelectTraining::class.java, null)
                .commit()
        }
    }
}