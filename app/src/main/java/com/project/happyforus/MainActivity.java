package com.project.happyforus;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private EditText dbServer, dbUsername, dbPassword, dbName;
    private Button saveButton, cancelButton, continueButton;
    private ImageButton addAppButton;
    private RecyclerView recyclerViewApps;
    private AppListAdapter appAdapter;

    private static final int REQUEST_CODE_SELECT_APPS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        dbServer = findViewById(R.id.db_server);
        dbUsername = findViewById(R.id.db_username);
        dbPassword = findViewById(R.id.db_password);
        dbName = findViewById(R.id.db_name);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_button);
        continueButton = findViewById(R.id.continue_button);
        addAppButton = findViewById(R.id.add_app_button);
        recyclerViewApps = findViewById(R.id.recycler_view_apps);

        // Load saved preferences from AppDataManager Singleton
        loadSavedPreferences();

        // Initialize monitored apps list and adapter
        appAdapter = new AppListAdapter(AppDataManager.getInstance().getMonitoredApps(), getPackageManager());
        recyclerViewApps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewApps.setAdapter(appAdapter);

        // Save button click listener
        saveButton.setOnClickListener(v -> saveSettings());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }


        // Cancel button click listener
        cancelButton.setOnClickListener(v -> {
            // Clear all input fields
            dbServer.setText("");
            dbUsername.setText("");
            dbPassword.setText("");
            dbName.setText("");
        });

        // Add app button click listener
        addAppButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AllAppsActivity.class);

            // Pass the currently monitored apps to AllAppsActivity
            ArrayList<String> selectedPackageNames = new ArrayList<>();
            for (ApplicationInfo appInfo : AppDataManager.getInstance().getMonitoredApps()) {
                selectedPackageNames.add(appInfo.packageName);
            }
            i.putStringArrayListExtra(AllAppsActivity.SELECTED_APPS_KEY, selectedPackageNames);

            startActivityForResult(i, REQUEST_CODE_SELECT_APPS);
        });

        // Continue button click listener
        continueButton.setOnClickListener(v -> {

            if (!isNotificationServiceEnabled()) {
                // Request Notification Listener permission
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
                startActivity(intent);
                Toast.makeText(this, "Please enable Notification Access", Toast.LENGTH_SHORT).show();
            } else {
                // Start Notification Listener if permission is granted
                Toast.makeText(this, "Starting Notification Listener", Toast.LENGTH_SHORT).show();
                startNotificationListener();
            }
        });
    }



    private boolean isNotificationServiceEnabled() {
        String packageName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (String name : names) {
                final ComponentName cn = ComponentName.unflattenFromString(name);
                if (cn != null && TextUtils.equals(packageName, cn.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startNotificationListener() {
        ComponentName cn = new ComponentName(this, MyNotificationListener.class);
        NotificationListenerService.requestRebind(cn);
    }

    private void loadSavedPreferences() {
        // Load saved values from AppDataManager Singleton
        AppDataManager dataManager = AppDataManager.getInstance();

        // Set database connection fields
        dbServer.setText(dataManager.getDbServer());
        dbUsername.setText(dataManager.getDbUsername());
        dbPassword.setText(dataManager.getDbPassword());
        dbName.setText(dataManager.getDbName());

        // Load monitored apps
        List<ApplicationInfo> monitoredApps = dataManager.getMonitoredApps();
        if (monitoredApps != null && !monitoredApps.isEmpty()) {
            appAdapter.notifyDataSetChanged();  // Update the RecyclerView to reflect loaded apps
        } else {
            Log.d("MainActivity", "No monitored apps found.");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isNotificationServiceEnabled()) {
            startNotificationListener();
        }
    }




    private void saveSettings() {
        // Get the user input
        String dbServerInput = dbServer.getText().toString().trim();
        String dbUsernameInput = dbUsername.getText().toString().trim();
        String dbPasswordInput = dbPassword.getText().toString().trim();
        String dbNameInput = dbName.getText().toString().trim();

        // Call the API to test the DB connection
        ApiService apiService = RetrofitClient.getClient("replace your ip/").create(ApiService.class);
        Call<ConnectionResponse> call = apiService.testDbConnection(dbServerInput, dbUsernameInput, dbPasswordInput, dbNameInput);

        call.enqueue(new retrofit2.Callback<ConnectionResponse>() {
            @Override
            public void onResponse(Call<ConnectionResponse> call, retrofit2.Response<ConnectionResponse> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    // Connection successful, save the user input to AppDataManager Singleton
                    AppDataManager dataManager = AppDataManager.getInstance();
                    dataManager.setDbServer(dbServerInput);
                    dataManager.setDbUsername(dbUsernameInput);
                    dataManager.setDbPassword(dbPasswordInput);
                    dataManager.setDbName(dbNameInput);

                    // Save monitored apps
                    dataManager.setMonitoredApps(appAdapter.getMonitoredApps());

                    Toast.makeText(MainActivity.this, "Database connection successful. Settings and monitored apps saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Database connection failed: " + response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ConnectionResponse> call, Throwable t) {
                // Handle API failure
                Log.d("MainActivity", "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to connect to server: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_APPS && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> selectedPackageNames = data.getStringArrayListExtra("selected_apps");

                if (selectedPackageNames != null) {
                    // Clear the previously selected apps
                    AppDataManager.getInstance().getMonitoredApps().clear();

                    // Loop through the selected package names and fetch ApplicationInfo
                    PackageManager pm = getPackageManager();
                    for (String packageName : selectedPackageNames) {
                        try {
                            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                            AppDataManager.getInstance().addMonitoredApp(appInfo);  // Add the app info to the monitored apps list
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    appAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
