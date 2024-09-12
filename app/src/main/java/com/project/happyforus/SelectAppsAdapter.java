package com.project.happyforus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SelectAppsAdapter extends RecyclerView.Adapter<SelectAppsAdapter.ViewHolder> {

    private List<ApplicationInfo> apps;
    private PackageManager pm;
    private SparseBooleanArray selectedAppStates; // Track the selection state of each app
    private List<String> previouslySelectedApps;  // Track previously selected apps

    public SelectAppsAdapter(List<ApplicationInfo> apps, PackageManager pm, List<String> previouslySelectedApps) {
        this.apps = apps;
        this.pm = pm;
        this.selectedAppStates = new SparseBooleanArray(); // Initialize selection states
        this.previouslySelectedApps = previouslySelectedApps;  // Track previously selected apps

        // Initialize selectedAppStates list with previously selected apps
        for (int i = 0; i < apps.size(); i++) {
            ApplicationInfo app = apps.get(i);
            if (previouslySelectedApps.contains(app.packageName)) {
                selectedAppStates.put(i, true); // Mark app as selected
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_apps_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationInfo appInfo = apps.get(position);

        // Set app name and icon
        holder.appName.setText(appInfo.loadLabel(pm));
        holder.appIcon.setImageDrawable(appInfo.loadIcon(pm));

        // Set the checkbox state based on the selection state in the SparseBooleanArray
        holder.appCheckBox.setOnCheckedChangeListener(null); // Remove listener before setting the state
        holder.appCheckBox.setChecked(selectedAppStates.get(position, false));

        // Handle checkbox clicks
        holder.appCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedAppStates.put(position, isChecked); // Update the selection state
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    // Return the selected apps
    public List<ApplicationInfo> getSelectedApps() {
        List<ApplicationInfo> selectedApps = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            if (selectedAppStates.get(i, false)) {
                selectedApps.add(apps.get(i));
            }
        }
        return selectedApps;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        CheckBox appCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appCheckBox = itemView.findViewById(R.id.app_check_box);
        }
    }
}
