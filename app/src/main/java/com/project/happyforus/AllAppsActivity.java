package com.project.happyforus;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class AllAppsActivity extends AppCompatActivity {

    private SelectAppsAdapter adapter;
    private RecyclerView rv;
    private List<ApplicationInfo> apps;
    private Button done;
    public static final String SELECTED_APPS_KEY = "selected_apps";
    private ArrayList<String> previouslySelectedApps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);

        // Initialize the RecyclerView
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Initialize PackageManager and fetch the installed apps
        PackageManager pm = getPackageManager();
        apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        // Get the previously selected apps from the intent
        previouslySelectedApps = getIntent().getStringArrayListExtra(SELECTED_APPS_KEY);
        if (previouslySelectedApps == null) {
            previouslySelectedApps = new ArrayList<>();
        }

        // Set the adapter with the previously selected apps
        adapter = new SelectAppsAdapter(apps, pm, previouslySelectedApps);
        rv.setAdapter(adapter);

        done = findViewById(R.id.save_button);
        done.setOnClickListener(v -> {
            List<ApplicationInfo> selectedApps = adapter.getSelectedApps();
            ArrayList<String> selectedPackageNames = new ArrayList<>();

            for (ApplicationInfo app : selectedApps) {
                selectedPackageNames.add(app.packageName);
            }

            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra(SELECTED_APPS_KEY, selectedPackageNames);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
