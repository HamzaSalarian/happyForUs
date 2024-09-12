package com.project.happyforus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<ApplicationInfo> appInfo;
    private PackageManager pm;

    public AppListAdapter(List<ApplicationInfo> appInfo, PackageManager pm){
        this.appInfo = appInfo;
        this.pm = pm;
    }

    @NonNull
    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListAdapter.ViewHolder holder, int position) {
        ApplicationInfo aInfo = appInfo.get(position);

        holder.appName.setText(aInfo.loadLabel(pm));
        holder.appIcon.setImageDrawable(aInfo.loadIcon(pm));

    }

    @Override
    public int getItemCount() {
        return appInfo.size();
    }


    public List<ApplicationInfo> getMonitoredApps() {
        return appInfo;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView appName;
        ImageView appIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
        }
    }
}
