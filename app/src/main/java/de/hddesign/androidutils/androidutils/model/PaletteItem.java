package de.hddesign.androidutils.androidutils.model;

import java.io.Serializable;

public class PaletteItem implements Serializable {

    private int index;
    private String title;
    private String imageUrl;

    public PaletteItem(int index, String title, String imageUrl) {
        this.index = index;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
