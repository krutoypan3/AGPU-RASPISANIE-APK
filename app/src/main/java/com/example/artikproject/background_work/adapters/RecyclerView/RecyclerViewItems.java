package com.example.artikproject.background_work.adapters.RecyclerView;

import androidx.annotation.NonNull;

public class RecyclerViewItems {

    private String mainText;

    // Image name (Without extension)
    private String imageName;
    private String subText;

    public RecyclerViewItems(String subText, String imageName, String subText2) {
        this.mainText = subText;
        this.imageName = imageName;
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @NonNull
    @Override
    public String toString()  {
        return this.mainText +" (Population: "+ this.subText +")";
    }

}
