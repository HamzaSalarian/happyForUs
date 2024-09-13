package com.project.happyforus;

public class NotificationRequest {
    private String dbServer;
    private String dbUsername;
    private String dbPassword;
    private String dbName;
    private String dbTable;
    private NotificationItem notificationItem;

    public NotificationRequest(String dbServer, String dbUsername, String dbPassword, String dbName, String dbTable, NotificationItem notificationItem) {
        this.dbServer = dbServer;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbName = dbName;
        this.dbTable = dbTable;
        this.notificationItem = notificationItem;
    }

    // Getters and setters
    public String getDbServer() { return dbServer; }
    public void setDbServer(String dbServer) { this.dbServer = dbServer; }

    public String getDbUsername() { return dbUsername; }
    public void setDbUsername(String dbUsername) { this.dbUsername = dbUsername; }

    public String getDbPassword() { return dbPassword; }
    public void setDbPassword(String dbPassword) { this.dbPassword = dbPassword; }

    public String getDbName() { return dbName; }
    public void setDbName(String dbName) { this.dbName = dbName; }

    public String getDbTable() { return dbTable; }
    public void setDbTable(String dbTable) { this.dbTable = dbTable; }

    public NotificationItem getNotificationItem() { return notificationItem; }
    public void setNotificationItem(NotificationItem notificationItem) { this.notificationItem = notificationItem; }
}
