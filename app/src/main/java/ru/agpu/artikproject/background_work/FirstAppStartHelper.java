package ru.agpu.artikproject.background_work;

import android.app.Activity;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import ru.agpu.artikproject.R;

public class FirstAppStartHelper {
    int outerCircleColor = R.color.bottom_nav_bar_color;
    int targetCircleColor = R.color.textColorPrimary;
    int titleTextColor = R.color.textColorPrimary;
    int descriptionTextColor = R.color.textColorPrimary;
    int textColor = R.color.textColorPrimary;
    float outerCircleAlpha = 0.90f;
    int targetRadius = 25;

    public FirstAppStartHelper(Activity activity) {
        new TapTargetSequence(activity)
                .targets(
                        TapTarget.forView(activity.findViewById(R.id.details_page_Home_page),
                                        activity.getString(R.string.Home_page),
                                        activity.getString(R.string.Home_page_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_today_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor),
                        TapTarget.forView(activity.findViewById(R.id.subtitle),
                                        activity.getString(R.string.Current_date),
                                        activity.getString(R.string.Current_date_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_date_range_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor),
                        TapTarget.forView(activity.findViewById(R.id.rasp_search_edit),
                                        activity.getString(R.string.rasp_find),
                                        activity.getString(R.string.rasp_find_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_search_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor),
                        TapTarget.forView(activity.findViewById(R.id.details_page_Faculties_list),
                                        activity.getString(R.string.faculties_list),
                                        activity.getString(R.string.faculties_list_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_search_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor),
                        TapTarget.forView(activity.findViewById(R.id.details_page_Schedule),
                                        activity.getString(R.string.Schedule),
                                        activity.getString(R.string.Schedule_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_menu_book_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor),
                        TapTarget.forView(activity.findViewById(R.id.details_page_Audiences),
                                        activity.getString(R.string.Audiences),
                                        activity.getString(R.string.Audiences_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_location_city_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor),
                        TapTarget.forView(activity.findViewById(R.id.details_page_Settings),
                                        activity.getString(R.string.settings),
                                        activity.getString(R.string.settings_description))
                                .titleTextColor(titleTextColor)
                                .outerCircleAlpha(outerCircleAlpha)
                                .descriptionTextColor(descriptionTextColor)
                                .outerCircleColor(outerCircleColor)
                                .targetCircleColor(targetCircleColor)
                                .icon(activity.getDrawable(R.drawable.ic_baseline_settings_24))
                                .targetRadius(targetRadius)
                                .cancelable(false)
                                .textColor(textColor))
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                        // Perform action for the current target
                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                    }
                }).start();
    }

}
