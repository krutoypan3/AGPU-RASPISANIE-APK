package ru.agpu.artikproject.presentation.layout.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.GetDirectionsList
import ru.agpu.artikproject.background_work.MainActivityStarter
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_IF_FIRST_APP_START
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences
import ru.agpu.artikproject.databinding.FragmentStartActivityStartingBinding
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl
import ru.oganesyanartem.core.domain.models.GroupsListItem
import ru.oganesyanartem.core.domain.repository.GroupsListRepository
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetUseCase

/**
 * Фрагмент запуска приложения
 */
class StartingFragment: Fragment(R.layout.fragment_start_activity_starting) {
    private var binding: FragmentStartActivityStartingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStartActivityStartingBinding.inflate(inflater, container, false)

        // Если это первый запуск приложения
        if (MySharedPreferences.getPref(context, PREF_IF_FIRST_APP_START, true)) {
            GetDirectionsList().directionsFromDatabase
            // Запускаем фрагмент с приветствием
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                WelcomeFragment::class.java, null
            ).commit()
        } else { // Иначе показываем анимацию запуска и переходим в приложение
            MainActivityStarter(requireActivity() as Activity, binding?.loadingIco!!).start()
        }
        Observable.create { subscriber: ObservableEmitter<List<GroupsListItem?>> ->  // Создаем observable, который будет выполняться в отдельном потоке
            val groupsListRepository: GroupsListRepository = GroupsListImpl(requireContext())
            subscriber.onNext(GroupsListGetUseCase(groupsListRepository).execute())
            subscriber.onComplete()
        }.subscribeOn(Schedulers.newThread()) // Выбираем ядро на котором будет выполняться наш observable
            .observeOn(AndroidSchedulers.mainThread()) // Выбираем ядро на котором будет выполняться наш код после observable
            .subscribe()

        return binding?.root
    }
}