package ru.agpu.artikproject.background_work.settings_layout.ficha

enum class Ficha(
    val prefName: String,
    val securityLevel: FichaLevel = FichaLevel.PUBLIC
) {
    FICHA_GO_TO_HOME("ficha_go_to_home"),
    FICHA_WINDOWS("ficha_windows"),
    FICHA_KEYS("ficha_keys"),
    FICHA_KEYS_TWO("ficha_keys_two", FichaLevel.ULTRA),
    FICHA_ADMIN("ficha_admin"),
    FICHA_SETTING_LOGO("ficha_setting_logo"),
    FICHA_SETTING_LEONARDO("ficha_setting_leonardo"),
    FICHA_PARA_LAPSHIN("ficha_para_lapshin", FichaLevel.SECRET),
    FICHA_REFRESH("ficha_refresh"),
    FICHA_GOD("ficha_god"),
    FICHA_TODAY("ficha_today"),
    FICHA_BUILDING_ICO("ficha_building_ico"),
    FICHA_BUILDING_MAIN_TEXT("ficha_building_main_text"),
    FICHA_RICARDO("ficha_ricardo", FichaLevel.SECRET);

    companion object {
        /**
         * Получить список по уровню ниже чем входной параметр
         * @param level верхний уровень допуска фичи
         * @return список фич
         */
        fun filterByLevel(level: FichaLevel): List<Ficha> {
            return values().filter { it.securityLevel.value <= level.value }
        }
    }
}