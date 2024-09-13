package com.project.happyforus;

import android.content.pm.ApplicationInfo;

import java.util.ArrayList;
import java.util.List;

public class AppDataManager {
    private static AppDataManager instance;

    private String dbServer;
    private String dbUsername;
    private String dbPassword;
    private String dbName;
    private String dbTable;
    private List<ApplicationInfo> monitoredApps;

    private AppDataManager() {
        // Initialize monitored apps list
        monitoredApps = new ArrayList<>();
    }

    // Get the singleton instance
    public static synchronized AppDataManager getInstance() {
        if (instance == null) {
            instance = new AppDataManager();
        }
        return instance;
    }

    // Setters and getters for database connection details
    public String getDbServer() {
        return dbServer;
    }

    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbTable() {
        return dbTable;
    }

    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    // Monitored apps methods
    public List<ApplicationInfo> getMonitoredApps() {
        return monitoredApps;
    }

    public void setMonitoredApps(List<ApplicationInfo> monitoredApps) {
        this.monitoredApps = monitoredApps;
    }

    public void addMonitoredApp(ApplicationInfo appInfo) {
        if (!monitoredApps.contains(appInfo)) {
            monitoredApps.add(appInfo);
        }
    }

    public void clearMonitoredApps() {
        monitoredApps.clear();
    }
}
