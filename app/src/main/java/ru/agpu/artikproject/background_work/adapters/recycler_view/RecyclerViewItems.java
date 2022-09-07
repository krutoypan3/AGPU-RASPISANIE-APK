package ru.agpu.artikproject.background_work.adapters.recycler_view;

import androidx.annotation.NonNull;

public class RecyclerViewItems {

    private final String mainText;

    // Image name (Without extension)
    private final String imageResourceUrl;
    private final String subText;


    public RecyclerViewItems(String subText, String imageResourceUrl, String subText2) {
        this.mainText = subText;
        this.imageResourceUrl = imageResourceUrl;
        this.subText = subText2;
    }

    public String getMainText() { return mainText; }
    public String getSubText() { return subText; }

    public String getImageResourceUrl() {
        return imageResourceUrl;
    }

    @NonNull
    @Override
    public String toString()  {
        return this.mainText +" (Population: "+ this.subText +")";
    }
}
