package ru.agpu.artikproject.background_work.adapters.recycler_view;

import androidx.annotation.NonNull;

public class RecyclerViewItems {

    private String mainText;

    // Image name (Without extension)
    private final String imageResourceId;
    private String subText;

    private String subText2;
    private String subText3;
    private String subText4;

    public RecyclerViewItems(String subText, String imageResourceId, String subText2) {
        this.mainText = subText;
        this.imageResourceId = imageResourceId;
        this.subText = subText2;
    }
    public RecyclerViewItems(String subText, String imageResourceId, String subText2, String subText3, String subText4) {
        this.mainText = subText;
        this.imageResourceId = imageResourceId;
        this.subText2 = subText2;
        this.subText3 = subText3;
        this.subText4 = subText4;
    }

    public String getMainText() { return mainText; }
    public String getSubText() { return subText; }
    public String getSubText2() { return subText2; }
    public String getSubText3() { return subText3; }
    public String getSubText4() { return subText4; }

    public void setSubText(String subText) {
        this.subText = subText;
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
