package ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings

import android.app.Activity
import ru.agpu.artikproject.R
import ru.agpu.artikproject.background_work.adapters.recycler_view.RecyclerViewItems

class LoadBuildingsList(val act: Activity): Thread() {

    companion object {
        var buildings_list = listOf<RecyclerViewItems>()

        const val BUILDING_ZAOCHKA = 1
        const val BUILDING_SPF = 2
        const val BUILDING_FOC = 3
        const val BUILDING_TEHFAK = 4
        const val BUILDING_DORMITORY = 5
        const val BUILDING_ISTFAK = 6
    }

    override fun run() {
        val list: MutableList<RecyclerViewItems> = ArrayList()
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_main),
                "https://i.ibb.co/M6n1bLj/photo-2022-05-09-17-54-18.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_main_aud)
            )
        )
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_zaochka),
                "https://i.ibb.co/42sQ0VB/photo-2022-05-09-17-55-37.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_zaochka_aud)
            )
        )
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_spf),
                "https://i.ibb.co/q56Z5Pv/photo-2022-05-09-17-55-30.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_spf_aud)
            )
        )
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_foc),
                "https://i.ibb.co/rmTf87S/photo-2022-05-09-17-54-44.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_foc_aud)
            )
        )
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_tehfak),
                "https://i.ibb.co/ZSVDrLS/photo-2022-05-09-17-55-06.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_tehfak_aud)
            )
        )
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_obshaga),
                "https://i.ibb.co/xM0mtwV/photo-2022-05-09-17-55-33.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_obshaga_aud)
            )
        )
        list.add(
            RecyclerViewItems(
                act.getString(R.string.adress_istfak),
                "https://i.ibb.co/TKzdj75/M3i8-I-sw4-Y4-HAV0-KWJggg1-J1wns17i-S3ul-Kud9a-MVd3-Lgj-LL8y-WEFc3yn-Qx6-XVKd-BZO-BFtn-Ctq87-Vhxq8-Z.jpg",
                act.getString(R.string.Audiences) + " : " + act.getString(R.string.adress_istfak_aud)
            )
        )
        buildings_list = list
    }
}