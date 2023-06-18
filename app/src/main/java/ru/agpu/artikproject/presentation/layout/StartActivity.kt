package ru.agpu.artikproject.presentation.layout

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.datebase.DataBaseSqlite
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentGroup
import ru.agpu.artikproject.background_work.start_activity_fragments.FragmentWelcome
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
                FragmentGroup::class.java, null
            ).commit()
            BACK_TO_WELCOME -> supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container_view,
                FragmentWelcome::class.java, null
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


        DataBaseSqlite.getSqliteDatabase(applicationContext) // Подключаемся к базе данных
        Theme.setting(this) // Применяем тему к приложению
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_activity)
        }

    companion object {
        @JvmField // Это нужно для совместимости кода Kotlin и Java
        var FRAGMENT = 0 // Номер открытого фрагмента
        @JvmField
        var SELECTED_GROUP = "" // Выбранная группа
        const val BACK_TO_GROUP = 2
        const val BACK_TO_WELCOME = 5
    }
}
