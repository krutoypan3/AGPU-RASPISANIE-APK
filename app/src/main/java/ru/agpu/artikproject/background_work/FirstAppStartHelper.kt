package ru.agpu.artikproject.background_work

import android.app.Activity
import androidx.appcompat.content.res.AppCompatResources
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import ru.agpu.artikproject.R

class FirstAppStartHelper(activity: Activity) {
    private val outerCircleColor: Int = R.color.bottom_nav_bar_color
    private val targetCircleColor: Int = R.color.textColorPrimary
    private val titleTextColor: Int = R.color.textColorPrimary
    private val descriptionTextColor: Int = R.color.textColorPrimary
    private val textColor: Int = R.color.textColorPrimary
    private val outerCircleAlpha = 0.90f
    private val targetRadius = 25

    init {
        TapTargetSequence(activity)
            .targets(
                TapTarget.forView(
                        activity.findViewById(R.id.details_page_Home_page),
                        activity.getString(R.string.Home_page),
                        activity.getString(R.string.Home_page_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_today_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor),
                TapTarget.forView(
                        activity.findViewById(R.id.subtitle),
                        activity.getString(R.string.Current_date),
                        activity.getString(R.string.Current_date_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_date_range_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor),
                TapTarget.forView(
                        activity.findViewById(R.id.rasp_search_edit),
                        activity.getString(R.string.rasp_find),
                        activity.getString(R.string.rasp_find_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_search_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor),
                TapTarget.forView(
                        activity.findViewById(R.id.details_page_Faculties_list),
                        activity.getString(R.string.faculties_list),
                        activity.getString(R.string.faculties_list_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_search_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor),
                TapTarget.forView(
                        activity.findViewById(R.id.details_page_Schedule),
                        activity.getString(R.string.Schedule),
                        activity.getString(R.string.Schedule_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_menu_book_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor),
                TapTarget.forView(
                        activity.findViewById(R.id.details_page_Audiences),
                        activity.getString(R.string.Audiences),
                        activity.getString(R.string.Audiences_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_location_city_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor),
                TapTarget.forView(
                        activity.findViewById(R.id.details_page_Settings),
                        activity.getString(R.string.settings),
                        activity.getString(R.string.settings_description)
                    )
                    .titleTextColor(titleTextColor)
                    .outerCircleAlpha(outerCircleAlpha)
                    .descriptionTextColor(descriptionTextColor)
                    .outerCircleColor(outerCircleColor)
                    .targetCircleColor(targetCircleColor)
                    .icon(AppCompatResources.getDrawable(activity, R.drawable.ic_baseline_settings_24))
                    .targetRadius(targetRadius)
                    .cancelable(false)
                    .textColor(textColor)
            )
            .listener(object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {}
                override fun onSequenceStep(lastTarget: TapTarget, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget) {}
            }).start()
    }
}
