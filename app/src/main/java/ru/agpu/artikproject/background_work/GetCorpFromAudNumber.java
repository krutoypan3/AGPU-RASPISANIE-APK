package ru.agpu.artikproject.background_work;

import android.app.Activity;

import java.util.Arrays;

import ru.agpu.artikproject.R;
import ru.agpu.artikproject.background_work.main_show.tool_bar.recycler_view_lists.buildings.LoadBuildingsList;

public class GetCorpFromAudNumber {
    public int getCorp(Activity act, String aud){
        if(Arrays.asList(act.getResources().getString(R.string.adress_zaochka_aud).replace(", ", ",").split(",")).contains(aud))
            return LoadBuildingsList.BUILDING_ZAOCHKA;
        else if(Arrays.asList(act.getResources().getString(R.string.adress_spf_aud).replace(", ", ",").split(",")).contains(aud))
            return LoadBuildingsList.BUILDING_SPF;
        else if(Arrays.asList(act.getResources().getString(R.string.adress_foc_aud).replace(", ", ",").split(",")).contains(aud))
            return LoadBuildingsList.BUILDING_FOC;
        else if(Arrays.asList(act.getResources().getString(R.string.adress_tehfak_aud).replace(", ", ",").split(",")).contains(aud))
            return LoadBuildingsList.BUILDING_TEHFAK;
        else if(Arrays.asList(act.getResources().getString(R.string.adress_obshaga_aud).replace(", ", ",").split(",")).contains(aud))
            return LoadBuildingsList.BUILDING_DORMITORY;
        else if(Arrays.asList(act.getResources().getString(R.string.adress_istfak_aud).replace(", ", ",").split(",")).contains(aud))
            return LoadBuildingsList.BUILDING_ISTFAK;
        return 0;
    }
}
