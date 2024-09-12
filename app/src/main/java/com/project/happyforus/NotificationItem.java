package com.project.happyforus;

public class NotificationItem {
    private String app_package_name;
    private String notification_title;
    private String notification_text;

    public NotificationItem(String app_package_name, String notification_title, String notification_text) {
        this.app_package_name = app_package_name;
        this.notification_title = notification_title;
        this.notification_text = notification_text;
    }

    // Getters and setters


    public String getApp_package_name() {
        return app_package_name;
    }

    public void setApp_package_name(String app_package_name) {
        this.app_package_name = app_package_name;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }
}
