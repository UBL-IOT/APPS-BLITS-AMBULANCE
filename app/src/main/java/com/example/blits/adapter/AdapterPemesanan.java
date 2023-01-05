package com.example.blits.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blits.R;
import com.example.blits.model.DriverModel;
import com.example.blits.model.PesananModel;
import com.example.blits.util.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterPemesanan extends RecyclerView.Adapter<AdapterPemesanan.ViewHolder> {

    private Context context;
    private List<PesananModel> models;
    private final int limit = 4;
    private AdapterPemesanan.onSelected listener;
    public interface onSelected {
        void onDetailDriver(PesananModel data);
    }

    public AdapterPemesanan(Context context, List<PesananModel> models , onSelected listener) {
        this.context = context;
        this.models = models;
        this.listener = listener ;
    }

    @NonNull
    @Override
    public AdapterPemesanan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_pesanan, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PesananModel data = models.get(position);
        holder.mTime.setText(Utils.convertMongoDate(data.getCreated_at()));
        if (data.getStatus_pesanan()== 0) {
            holder.mStatus.setText("Menunggu");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_unactive);
        }else if(data.getStatus_pesanan()== 1){
            holder.mStatus.setText("Dalam Perjalanan");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_orange);
        }else {
            holder.mStatus.setText("Selesai");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_active);
        }

        if (data.getGuid_driver() != null){
            holder.mDriver.setEnabled(true);
        }
        holder.mDriver.setOnClickListener(view -> listener.onDetailDriver(data));

    }

    @Override
    public int getItemCount() {
        if (models == null)
            return 0;
        else
            return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mStatus, mTime ,mDriver;
        ImageView mIndicator;

        public ViewHolder(View itemView) {
            super(itemView);

            mStatus = itemView.findViewById(R.id.mStatus);
            mTime = itemView.findViewById(R.id.mTime);
            mDriver = itemView.findViewById(R.id.mDriver);
            mIndicator = itemView.findViewById(R.id.indicator);

        }
    }

}
