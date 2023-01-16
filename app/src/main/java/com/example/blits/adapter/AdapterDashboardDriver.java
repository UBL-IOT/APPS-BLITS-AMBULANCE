package com.example.blits.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blits.model.DriverModel;
import com.example.blits.R;

import java.util.List;

public class AdapterDashboardDriver extends RecyclerView.Adapter<AdapterDashboardDriver.ViewHolder> {

    private Context context;
    private List<DriverModel> driverList;

    public AdapterDashboardDriver(Context context, List<DriverModel> driverList) {
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public AdapterDashboardDriver.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_driver_dashboard, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DriverModel driver = driverList.get(position);
        holder.fullnameDriver.setText(driver.getNama_driver());
        holder.mPlat.setText(driver.getNo_plat());
    }

    @Override
    public int getItemCount() {
        if (driverList == null)
            return 0;
        else
            return driverList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullnameDriver, mPlat;

        public ViewHolder(View itemView) {
            super(itemView);

            fullnameDriver = itemView.findViewById(R.id.fullname);
            mPlat = itemView.findViewById(R.id.mPlat);
        }
    }

}
