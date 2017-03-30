package com.boonya.ben.firebasecamerademo.model;

/**
 * Created by Boonya Kitpitak on 3/29/17.
 */

public class ImageInfo {
    private String ImageDecription;
    private String ImageName;
    private String ImageUrl;

    public ImageInfo() {

    }

    public String getImageDecription() {
        return ImageDecription;
    }

    public void setImageDecription(String imageDecription) {
        ImageDecription = imageDecription;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
