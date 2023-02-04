package com.example.blits.driver;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blits.R;
import com.example.blits.adapter.AdapterHistoryOrderCustomer;
import com.example.blits.adapter.AdapterHistoryOrderDriver;
import com.example.blits.adapter.AdapterOrderCustomer;
import com.example.blits.adapter.AdapterOrderDriver;
import com.example.blits.model.DriverModel;
import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.example.blits.network.NetworkService;
import com.example.blits.network.RestService;
import com.example.blits.response.DriverResponse;
import com.example.blits.response.PesananResponse;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.ui.SweetDialogs;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentOrder extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    SweetAlertDialog sweetAlertDialog;
    ModelUser modelUser;
    LinearLayout dataAvailable , emptyDataDisplay;
    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_driver, container, false);

        sweetAlertDialog = new SweetAlertDialog(getActivity());
        mRecyclerView = v.findViewById(R.id.mRecyclerView);
        dataAvailable = v.findViewById(R.id.dataAvailable);
        emptyDataDisplay = v.findViewById(R.id.emptyDataDisplay);
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        this.ListHistoryPesanan();

        return v;
    }

    private void ListHistoryPesanan() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getHistoryPesananByDriver(modelUser.getGuid())
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {
                        hideLoadingIndicator();
                        if (response.body().getmStatus())
                            onDataReady(response.body().getData());
                        else
                            SweetDialogs.commonWarning(getActivity(), "Warning", "Gagal Memuat Permintaan", false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PesananResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    void onDataReady(List<PesananModel> model) {
//        List<PesananModel> models = new ArrayList<>();
//        for(PesananModel data : model){
//            if(data.getStatus_pesanan() != 3)
//                models.add(data);
//        }
        if(model.isEmpty()) {
            emptyDataDisplay.setVisibility(View.VISIBLE);
            dataAvailable.setVisibility(View.GONE);
        }else {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.clearFocus();
            adapter = new AdapterHistoryOrderDriver(getActivity(), model);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void showLoadingIndicator() {
        sweetAlertDialog.show();
    }

    public void hideLoadingIndicator() {
        sweetAlertDialog.dismiss();
    }

    public void onNetworkError(String cause) {
        SweetDialogs.endpointError(getActivity());
    }

}