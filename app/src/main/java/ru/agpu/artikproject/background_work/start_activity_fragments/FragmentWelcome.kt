package ru.agpu.artikproject.background_work.start_activity_fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import ru.agpu.artikproject.R
import ru.agpu.artikproject.presentation.layout.MainActivity

class FragmentWelcome: Fragment(R.layout.fragment_start_activity_welcome) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Я только поступил
        val iJustGotInBtn = view.findViewById<View>(R.id.I_just_got_in_btn)
        iJustGotInBtn.setOnClickListener {
            iJustGotInBtn.isClickable = false
            iJustGotInBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, FragmentGroup::class.java, null)
                .commit()
        }

        // Я студент
        val imStudentBtn = view.findViewById<View>(R.id.Im_a_student_btn)
        imStudentBtn.setOnClickListener {
            imStudentBtn.isClickable = false
            imStudentBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, FragmentGroup::class.java, null)
                .commit()
        }

        // Я преподаватель
        val imTeacherBtn = view.findViewById<View>(R.id.I_am_a_teacher_btn)
        imTeacherBtn.setOnClickListener {
            imTeacherBtn.isClickable = false
            imTeacherBtn.startAnimation(AnimationUtils.loadAnimation(view.context, R.anim.scale))
            val intent = Intent(view.context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}