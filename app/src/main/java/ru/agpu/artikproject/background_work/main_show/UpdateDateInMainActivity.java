package ru.agpu.artikproject.background_work.main_show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.data.repository.weeks_list.WeeksListImpl;
import ru.agpu.artikproject.domain.models.WeeksListItem;
import ru.agpu.artikproject.domain.repository.FullWeekListRepository;
import ru.agpu.artikproject.domain.usecase.weeks_list.WeeksListGetByWeekIdUseCase;
import ru.agpu.artikproject.presentation.layout.MainActivity;

public class UpdateDateInMainActivity extends Thread {

    private final Activity act;

    /**
     * Устанавливает дату на главном экране
     *
     * @param act Активити
     */
    public UpdateDateInMainActivity(Activity act) {
        this.act = act;
    }

    @Override
    public void run() {
        try {
            FullWeekListRepository fullWeekListRepository = new WeeksListImpl(act.getApplicationContext());
            List<WeeksListItem> weekList = new WeeksListGetByWeekIdUseCase(
                    fullWeekListRepository,
                    MainActivity.week_id
            ).execute();
            if (!weekList.isEmpty()) {
                String s_po = weekList.get(0).getStartDate() + " " + weekList.get(0).getEndDate();
                act.runOnUiThread(() -> {
                    try {
                        TextView current_week = act.findViewById(R.id.subtitle);
                        current_week.setText(s_po);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                @SuppressLint("SimpleDateFormat") String day = (new SimpleDateFormat("dd.MM.yyyy, EEEE")).format(ChangeDay.chosenDateCalendar.getTime());
                String today_date = act.getString(R.string.Curent_day) + " " + day;
                act.runOnUiThread(() -> {
                    try {
                        TextView today = act.findViewById(R.id.main_activity_text);
                        today.setText(today_date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
