package ru.agpu.artikproject.background_work;

import android.app.Activity;

import java.util.Arrays;

import ru.agpu.artikproject.R;

public class GetCorpFromAudNumber {
    public int getCorp(Activity act, String aud){
        if(Arrays.asList(act.getResources().getString(R.string.adress_zaochka_aud).replace(", ", ",").split(",")).contains(aud)){
            return 1;
        }
        else if(Arrays.asList(act.getResources().getString(R.string.adress_spf_aud).replace(", ", ",").split(",")).contains(aud)){
            return 2;
        }
        else if(Arrays.asList(act.getResources().getString(R.string.adress_ebd_aud).replace(", ", ",").split(",")).contains(aud)){
            return 3;
        }
        else if(Arrays.asList(act.getResources().getString(R.string.adress_foc_aud).replace(", ", ",").split(",")).contains(aud)){
            return 4;
        }
        else if(Arrays.asList(act.getResources().getString(R.string.adress_tehfak_aud).replace(", ", ",").split(",")).contains(aud)){
            return 5;
        }
        else if(Arrays.asList(act.getResources().getString(R.string.adress_obshaga_aud).replace(", ", ",").split(",")).contains(aud)){
            return 6;
        }
        return 0;
    }
}
