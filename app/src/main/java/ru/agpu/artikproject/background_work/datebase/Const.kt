package ru.agpu.artikproject.background_work.datebase

object Const {
    object FragmentDirection {
        const val BACK_TO_SELECT_GROUP_DIRECTION_FACULTY = 1
        const val BACK_TO_BUILDINGS_SHOW = 3
        const val BACK_TO_MAIN_SHOW = 2
        const val BACK_TO_GROUP = 101
        const val BACK_TO_WELCOME = 102
    }
    object Prefs {
        const val PREF_SELECTED_ITEM = "selectedItem"
        const val PREF_SELECTED_ITEM_ID = "selectedItem_id"
        const val PREF_SELECTED_ITEM_TYPE = "selectedItem_type"

        const val PREF_START_RASP = "start_rasp"
        const val PREF_IF_FIRST_APP_START = "IsFirstAppStart"
    }
    object SwipeDirections {
        const val LEFT = "left"
        const val RIGHT = "right"
        const val UP = "up"
        const val BOTTOM = "bottom"
    }
    object Calendar {
        const val NAME = "Расписание АГПУ"
    }
}