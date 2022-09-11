package ru.agpu.artikproject.background_work.start_activity_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.GetDirectionsList;
import ru.agpu.artikproject.background_work.Starter_MainActivity;
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences;
import ru.oganesyanartem.core.data.repository.groups_list.GroupsListImpl;
import ru.oganesyanartem.core.domain.models.GroupsListItem;
import ru.oganesyanartem.core.domain.repository.GroupsListRepository;
import ru.oganesyanartem.core.domain.usecase.groups_list.GroupsListGetUseCase;

/**
 * Фрагмент, который определяет первый запуск приложения и, если это первый старт, запускает
 * следующий фрагмент.
 */
public class FragmentStarting extends Fragment {

    public FragmentStarting() {
        super(R.layout.fragment_start_activity_starting);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Если это первый запуск приложения
        if (MySharedPreferences.get(getContext(), "IsFirstAppStart", true)) {
            new GetDirectionsList().getDirectionsFromFirebase();
            // Запускаем фрагмент с приветствием
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container_view, FragmentWelcome.class, null).commit();
        } else { // Иначе показываем анимацию запуска и переходим в приложение
            ImageView loading_ico = view.findViewById(R.id.loading_ico);
            new Starter_MainActivity((Activity) view.getContext(), loading_ico).start();
        }

        Observable<List<GroupsListItem>> observable = Observable.create(subscriber -> { // Создаем observable, который будет выполняться в отдельном потоке
            GroupsListRepository groupsListRepository = new GroupsListImpl(view.getContext());
            subscriber.onNext(new GroupsListGetUseCase(groupsListRepository).execute());
            subscriber.onComplete();
        });

        observable
                .subscribeOn(Schedulers.newThread()) // Выбираем ядро на котором будет выполняться наш observable
                .observeOn(AndroidSchedulers.mainThread()) // Выбираем ядро на котором будет выполняться наш код после observable
                .subscribe();
    }
}