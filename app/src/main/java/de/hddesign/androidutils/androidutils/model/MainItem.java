package de.hddesign.androidutils.androidutils.model;

import java.io.Serializable;

public class MainItem implements Serializable {

    private int index;
    private String title;
    private int color;

    public MainItem(int index, String title, int color) {
        this.index = index;
        this.title = title;
        this.color = color;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
