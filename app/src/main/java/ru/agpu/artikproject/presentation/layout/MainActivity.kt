package ru.agpu.artikproject.presentation.layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.CheckAppUpdate
import ru.agpu.artikproject.background_work.CheckInternetConnection.getState
import ru.agpu.artikproject.background_work.FirstAppStartHelper
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animPriehalSleva
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animPriehalSprava
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animRotate
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animRotate_ok
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animScale
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVl
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVlPriehalSprava
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVp
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVpPriehalSleva
import ru.agpu.artikproject.background_work.datebase.AppData.Animations.animUehalVpravo
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekDay
import ru.agpu.artikproject.background_work.datebase.AppData.AppDate.weekId
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.FRAGMENT
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.IS_MAIN_SHOWED
import ru.agpu.artikproject.background_work.datebase.AppData.FragmentData.myFragmentManager
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItem
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemId
import ru.agpu.artikproject.background_work.datebase.AppData.Rasp.selectedItemType
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_BUILDINGS_SHOW
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_MAIN_SHOW
import ru.agpu.artikproject.background_work.datebase.Const.FragmentDirection.BACK_TO_SELECT_GROUP_DIRECTION_FACULTY
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_IF_FIRST_APP_START
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM_ID
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_SELECTED_ITEM_TYPE
import ru.agpu.artikproject.background_work.datebase.Const.Prefs.PREF_START_RASP
import ru.agpu.artikproject.background_work.datebase.MySharedPreferences.getPref
import ru.agpu.artikproject.background_work.main_show.BottomNavigationViewListener
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentRecyclerviewShow.Companion.SELECTED_LIST
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentScheduleShow
import ru.agpu.artikproject.background_work.main_show.fragments.FragmentSelectGroupDirectionFaculty
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList
import ru.agpu.artikproject.background_work.service.PlayService
import ru.agpu.artikproject.background_work.settings_layout.ficha.FichaAchievements
import ru.agpu.artikproject.background_work.site_parse.GetRasp
import ru.agpu.artikproject.background_work.theme.CustomBackground.getBackground
import ru.agpu.artikproject.background_work.theme.CustomBackground.getBackgroundDarker
import ru.oganesyanartem.core.data.repository.CurrentWeekDayImpl
import ru.oganesyanartem.core.data.repository.current_week_id.CurrentWeekIdImpl
import ru.oganesyanartem.core.domain.repository.CurrentWeekIdRepository
import ru.oganesyanartem.core.domain.usecase.CurrentWeekDayGetUseCase
import ru.oganesyanartem.core.domain.usecase.CurrentWeekIdGetUseCase

class MainActivity: AppCompatActivity() {
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.i("onKeyMultiple", "kc=$keyCode")
        FichaAchievements().playKeysFicha(this, keyCode)
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        when (FRAGMENT) {
            BACK_TO_SELECT_GROUP_DIRECTION_FACULTY -> {
                FRAGMENT = BACK_TO_MAIN_SHOW
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container_view,
                    FragmentSelectGroupDirectionFaculty::class.java, null
                ).commit()
            }

            BACK_TO_MAIN_SHOW -> if (!IS_MAIN_SHOWED) {
                IS_MAIN_SHOWED = true
                bottomNavigationView?.selectedItemId = R.id.details_page_Home_page
            }

            BACK_TO_BUILDINGS_SHOW -> {
                FRAGMENT = BACK_TO_MAIN_SHOW
                SELECTED_LIST = 1
                supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .replace(R.id.fragment_container_view, FragmentRecyclerviewShow::class.java, null)
                    .commit()
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Установка нового фона и затемнителя | Должно быть после setContentView
        findViewById<View>(R.id.main_activity_layout).background = getBackground(applicationContext)
        findViewById<View>(R.id.background_darker).setBackgroundColor(
            getBackgroundDarker(applicationContext)
        )
        val policy = ThreadPolicy.Builder().permitAll().build() // Без этих двух строк
        StrictMode.setThreadPolicy(policy) // мы не можем подключиться к базе данных MSSQL так как потокам становится плохо
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // Инициализируем анимации
        animScale = AnimationUtils.loadAnimation(applicationContext, R.anim.scale)
        animRotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        animUehalVp = AnimationUtils.loadAnimation(applicationContext, R.anim.uehal_vpravo)
        animUehalVl = AnimationUtils.loadAnimation(applicationContext, R.anim.uehal_vlevo)
        animUehalVpravo = AnimationUtils.loadAnimation(applicationContext, R.anim.uehal_vpravo_daleko)
        animUehalVlPriehalSprava = AnimationUtils.loadAnimation(applicationContext, R.anim.uehal_vlevo_priehal_sprava)
        animUehalVpPriehalSleva = AnimationUtils.loadAnimation(applicationContext, R.anim.uehal_vpravo_priehal_sleva)
        animPriehalSprava = AnimationUtils.loadAnimation(applicationContext, R.anim.priehal_sprava)
        animPriehalSleva = AnimationUtils.loadAnimation(applicationContext, R.anim.priehal_sleva)
        animRotate_ok = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_ok)

        weekDay = CurrentWeekDayGetUseCase(CurrentWeekDayImpl()).execute()
        myFragmentManager = supportFragmentManager
        bottomNavigationView = findViewById(R.id.bottom_navigatin_view)
        BottomNavigationViewListener(this) // Слушатель нажатий на нижний тулбар
        CheckAppUpdate(this, false).start() // Запуск проверки обновлений при входе в приложение
        LoadBuildingsList(this).start() // Загрузка данных об строениях в адаптер
        startService(Intent(applicationContext, PlayService::class.java)) // ЗАПУСК СЛУЖБЫ

        // Выгружаем данные о последнем открытом расписании в главное активити
        selectedItem = getPref(
            context = applicationContext,
            name = PREF_SELECTED_ITEM,
            default_value = ""
        )
        selectedItemType = getPref(
            context = applicationContext,
            name = PREF_SELECTED_ITEM_TYPE,
            default_value = ""
        )
        selectedItemId = getPref(
            context = applicationContext,
            name = PREF_SELECTED_ITEM_ID,
            default_value = ""
        )
        val observable = Observable.create { subscriber: ObservableEmitter<Int> ->  // Создаем observable, который будет выполняться в отдельном потоке
                val currentWeekIdRepository: CurrentWeekIdRepository = CurrentWeekIdImpl(applicationContext)
                val currentWeekIdGetUseCase = CurrentWeekIdGetUseCase(currentWeekIdRepository)
                subscriber.onNext(currentWeekIdGetUseCase.execute())
                subscriber.onComplete()
            }
        disposable = observable
            .subscribeOn(Schedulers.newThread()) // Выбираем ядро на котором будет выполняться наш observable
            .observeOn(AndroidSchedulers.mainThread()) // Выбираем ядро на котором будет выполняться наш код после observable
            .subscribe { newWeekId: Int ->  // Код, выполняющийся после observable
                weekId = newWeekId
            }
        if (intent.getBooleanExtra(PREF_START_RASP, false)) {
            if (getState(applicationContext)) {
                selectedItem = intent.getStringExtra(PREF_SELECTED_ITEM)
                selectedItemType = intent.getStringExtra(PREF_SELECTED_ITEM_TYPE)
                selectedItemId = intent.getStringExtra(PREF_SELECTED_ITEM_ID)
                GetRasp(
                    selectedItemId = selectedItemId ?: "",
                    selectedItemType = selectedItemType ?: "",
                    selectedItem =selectedItem ?: "",
                    weekIdUpd = weekId,
                    context = applicationContext
                ).start()
            }
        }
        val isFirstAppStart = getPref(
            context = applicationContext,
            name = PREF_IF_FIRST_APP_START,
            default_value = true
        )
        if (!isFirstAppStart && selectedItem != "") {
            IS_MAIN_SHOWED = false
            FRAGMENT = BACK_TO_MAIN_SHOW
            bottomNavigationView?.selectedItemId = R.id.details_page_Schedule
            myFragmentManager?.beginTransaction()
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ?.replace(R.id.fragment_container_view, FragmentScheduleShow::class.java, null)
                ?.commit()
        }
        findViewById<View>(R.id.main_app_text).setOnClickListener {
            if (IS_MAIN_SHOWED) FirstAppStartHelper(this)
        }
    }

    private var disposable: Disposable? = null
    private var bottomNavigationView: BottomNavigationView? = null
}