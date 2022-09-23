package ru.agpu.artikproject.background_work.widget

data class WidgetGridViewItem(
    val timeRange: String,
    val separator: Int,
    val backgroundConstraint: Int,
    val itemName: String,
    val itemPrepodAndTime: String,
    val itemGroup: String
)