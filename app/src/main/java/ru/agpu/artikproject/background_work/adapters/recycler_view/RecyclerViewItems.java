package ru.agpu.artikproject.background_work.adapters.recycler_view;

import androidx.annotation.NonNull;

public class RecyclerViewItems {

    private String mainText;

    // Image name (Without extension)
    private final String imageResourceId;
    private String subText;

    public RecyclerViewItems(String subText, String imageResourceId, String subText2) {
        this.mainText = subText;
        this.imageResourceId = imageResourceId;
        this.subText = subText2;
    }

    public String getSubText() {
        return subText;
    }

    public void setSubText(String subText) {
        this.subText = subText;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getImageResourceUrl() {
        return imageResourceId;
    }

    @NonNull
    @Override
    public String toString()  {
        return this.mainText +" (Population: "+ this.subText +")";
    }

}
