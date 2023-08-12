package ru.agpu.artikproject.background_work.start_activity_fragments

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.agpu.artikproject.R
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl
import ru.oganesyanartem.core.domain.models.GroupsListItem
import ru.oganesyanartem.core.domain.repository.GroupsListRepository
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetUseCase
import ru.agpu.artikproject.background_work.GetDirectionsList
import ru.agpu.artikproject.background_work.StarterMainActivity
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences


class FragmentStarting: Fragment(R.layout.fragment_start_activity_starting) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Если это первый запуск приложения
        if (MySharedPreferences.getPref(context, "IsFirstAppStart", true)) {
            GetDirectionsList().directionsFromDatabase
            // Запускаем фрагмент с приветствием
            parentFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                FragmentWelcome::class.java, null
            ).commit()
        } else { // Иначе показываем анимацию запуска и переходим в приложение
            val loading_ico = view.findViewById<ImageView>(R.id.loading_ico)
            StarterMainActivity(view.context as Activity, loading_ico).start()
        }
        Observable.create { subscriber: ObservableEmitter<List<GroupsListItem?>> ->  // Создаем observable, который будет выполняться в отдельном потоке
                val groupsListRepository: GroupsListRepository = GroupsListImpl(view.context)
                subscriber.onNext(GroupsListGetUseCase(groupsListRepository).execute())
                subscriber.onComplete()
            }.subscribeOn(Schedulers.newThread()) // Выбираем ядро на котором будет выполняться наш observable
            .observeOn(AndroidSchedulers.mainThread()) // Выбираем ядро на котором будет выполняться наш код после observable
            .subscribe()
    }
}