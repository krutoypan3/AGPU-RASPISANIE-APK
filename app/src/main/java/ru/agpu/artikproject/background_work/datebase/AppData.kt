package ru.agpu.artikproject.background_work.datebase

import android.view.animation.Animation
import androidx.fragment.app.FragmentManager
import ru.agpu.artikproject.background_work.adapters.list_view.ListViewItems
import javax.inject.Singleton

/**
 * Данный класс служит базой данных для хранения данных только в момент работы приложения
 * для использования разными классами
 */
@Singleton
object AppData {
    object Animations {
        var animRotate: Animation? = null
        var animUehalVp: Animation? = null
        var animUehalVl: Animation? = null
        var animUehalVlPriehalSprava: Animation? = null
        var animUehalVpPriehalSleva: Animation? = null
        var animPriehalSprava: Animation? = null
        var animUehalVpravo: Animation? = null
        var animPriehalSleva: Animation? = null
        var animScale: Animation? = null
        var animRotate_ok: Animation? = null
    }

    object AppDate {
        var weekId = 0
        var weekDay = 0
    }

    object FragmentData {
        var FRAGMENT: Int = 0
        var IS_MAIN_SHOWED = true
        var myFragmentManager: FragmentManager? = null
    }

    object Groups {
        var groupListed: ArrayList<ListViewItems>? = null
        var groupListedType: Array<String> = emptyArray()
        var groupListedId: Array<String> = emptyArray()
    }
    object Rasp {
        var selectedItem: String? = null
        var selectedItemType: String? = null
        var selectedItemId: String? = null
    }
}