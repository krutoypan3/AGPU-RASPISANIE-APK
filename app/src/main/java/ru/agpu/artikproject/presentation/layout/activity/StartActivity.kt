package ru.agpu.artikproject.presentation.layout.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_GROUP
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_WELCOME
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite
import ru.agpu.artikproject.presentation.layout.fragment.GroupFragment
import ru.agpu.artikproject.presentation.layout.fragment.WelcomeFragment
import ru.agpu.artikproject.background_work.theme.Theme

/**
 * Стартовое активити - в нем определяется что будет происходить при нажатии на кнопку назад в
 * разных фрагментах, а так же здесь хранятся различные временные данные необходимые фрагментам
 */
class StartActivity : AppCompatActivity() {
    override fun onBackPressed() {
        when (FRAGMENT) {
            BACK_TO_GROUP -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                GroupFragment::class.java, null
            ).commit()
            BACK_TO_WELCOME -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                WelcomeFragment::class.java, null
            ).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Realm.init(this)

//        // Обновить список факультетов и групп
//        Single.just(RetrofitToRealm().loadToDatabaseSemanticGroup())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .onErrorResumeNext {
//                Log.e("RetrofitApi", it.message!!)
//                Single.just(false)
//            }
//            .subscribe()

        // Исключительно в качестве теста -------------------------------------------
        // Загрузить расписание группы с id 2365
//        Single.just(true)
//            .observeOn(Schedulers.newThread())
//            .subscribeOn(Schedulers.newThread())
//            .doOnSubscribe { RetrofitToRealm().loadRaspisForGroup(2365) }
//            .onErrorResumeNext {
//                it.printStackTrace()
//                Single.just(false)
//            }
//            .subscribe()
        // --------------------------------------------------------------------------


        DataBaseSqlite(applicationContext).init() // Подключаемся к базе данных
        Theme.setting(this) // Применяем тему к приложению
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        }

    companion object {
        var FRAGMENT = 0 // Номер открытого фрагмента
        var SELECTED_GROUP = "" // Выбранная группа
    }
}
