package ru.agpu.artikproject.background_work.rasp_show

class WeekShowResize {
    /**
     * Метод отвечающий за уменьшение размера расписания в недельном режиме
     */
    fun sizeDec() {
        WeekShow.table_size--
        resize()
    }

    /**
     * Метод отвечающий за увеличение размера расписания в недельном режиме
     */
    fun sizeAdd() {
        WeekShow.table_size++
        resize()
    }

    /**
     * Метод отвечающий за обновление измененного размера расписания в недельном режиме
     */
    fun resize() {
        for (i in 0..59) {
            WeekShow.qqty?.get(i)?.textSize = WeekShow.table_size.toFloat()
        }

        var fk = 0
        val maxRazmer = intArrayOf(0, 0, 0, 0, 0, 0, 0)
        for (ff in 0..59) {
            if ((ff % 10 == 0) and (ff != 0)) {
                fk++
            }
            if ((WeekShow.qqty?.get(ff)?.text?.length ?: 0) * WeekShow.table_size / 4 > maxRazmer[fk]) {
                maxRazmer[fk] = (WeekShow.qqty?.get(ff)?.text?.length ?: 0) * WeekShow.table_size / 4
            }
        }

        fk = 0
        for (ff in 0..59) {
            if ((ff % 10 == 0) and (ff != 0)) {
                fk++
            }

            if (maxRazmer[fk] < 120) {
                maxRazmer[fk] = 120
            }
            WeekShow.qqty?.get(ff)?.minHeight = maxRazmer[fk]
        }
    }
}