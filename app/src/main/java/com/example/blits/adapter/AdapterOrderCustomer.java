package com.example.blits.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blits.R;
import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.util.Utils;

import java.util.List;

public class AdapterOrderCustomer extends RecyclerView.Adapter<AdapterOrderCustomer.ViewHolder> {

    private Context context;
    private List<PesananModel> models;
    private AdapterOrderCustomer.onSelected listener;
    ModelUser modelUser;

    public interface onSelected {
        void onDetailDriver(PesananModel data);
    }

    public AdapterOrderCustomer(Context context, List<PesananModel> models , onSelected listener) {
        this.context = context;
        this.models = models;
        this.listener = listener ;
    }

    @NonNull
    @Override
    public AdapterOrderCustomer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_pesanan, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PesananModel data = models.get(position);
        holder.mTime.setText(Utils.convertMongoDate(data.getCreated_at()));
        holder.mKodePesanan.setText(data.getKode_pesanan());
        holder.alamatAntar = data.getTujuan();
        holder.alamatJemput = data.getTitik_jemput();

        holder.mFullnameCustomer.setText(modelUser.getFullname());

        if (holder.alamatAntar.length() > 30) {
            holder.alamatAntar = holder.alamatAntar.substring(0, 20) + "...";
            holder.mAlamatAntar.setText(holder.alamatAntar);
        } else {
            holder.mAlamatAntar.setText(data.getTujuan());
        }

        if (holder.alamatJemput.length() > 30) {
            holder.alamatJemput = holder.alamatJemput.substring(0, 20) + "...";
            holder.mAlamatJemput.setText(holder.alamatJemput);
        } else {
            holder.mAlamatJemput.setText(data.getTitik_jemput());
        }

        if (data.getStatus_pesanan() == 0) {
            holder.mStatus.setText("Menunggu");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_pending);
        } else if(data.getStatus_pesanan() == 1){
            holder.mStatus.setText("Jemput");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_waiting);
        } else if(data.getStatus_pesanan() == 2){
            holder.mStatus.setText("Antar");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_proccess);
        } else {
            holder.mStatus.setText("Selesai");
            holder.mIndicator.setImageResource(R.drawable.shape_indicator_success);
        }

        if (data.getGuid_driver() != null){
            holder.mDriver.setEnabled(true);
        }else{
            holder.mDriver.setEnabled(false);
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

        String alamatAntar, alamatJemput;
        TextView mStatus, mTime, mAlamatJemput, mAlamatAntar, mFullnameCustomer, mKodePesanan;
        ImageView mIndicator;
        CardView mDriver;

        public ViewHolder(View itemView) {
            super(itemView);

            mStatus = itemView.findViewById(R.id.mStatus);
            mTime = itemView.findViewById(R.id.mTime);
            mDriver = itemView.findViewById(R.id.mDriver);
            mIndicator = itemView.findViewById(R.id.indicator);
            mAlamatAntar = itemView.findViewById(R.id.addressAntar);
            mAlamatJemput = itemView.findViewById(R.id.adressJemput);
            mFullnameCustomer = itemView.findViewById(R.id.fullnameCustomer);
            mKodePesanan = itemView.findViewById(R.id.kodePesanan);
        }
    }

}
