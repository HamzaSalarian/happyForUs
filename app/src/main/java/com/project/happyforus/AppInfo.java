package com.project.happyforus;

import android.graphics.drawable.Drawable;

public class AppInfo {

    private String name;
    private Drawable appIcon;

    public AppInfo(String name, Drawable appIcon) {
        this.name = name;
        this.appIcon = appIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}


