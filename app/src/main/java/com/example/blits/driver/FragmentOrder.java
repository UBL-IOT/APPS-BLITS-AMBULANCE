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

    LinearLayout emptyDataDisplay, dataAvailable;

    ModelUser modelUser;
    Dialog dialog;
    ImageButton closePopup;
    TextView fullnameriverData, platDriverData, orderHstory;
    SweetAlertDialog sweetAlertDialog;
    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        dialog = new Dialog(getActivity());
        sweetAlertDialog = new SweetAlertDialog(getActivity());
        mRecyclerView = v.findViewById(R.id.mRecyclerView);

        emptyDataDisplay = v.findViewById(R.id.emptyDataDisplay);
        dataAvailable = v.findViewById(R.id.dataAvailable);

        orderHstory = v.findViewById(R.id.history);
        orderHstory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderHistory.class));
            }
        });

        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        ListPesanan(modelUser.getGuid());
        return v;
    }

    private void ListPesanan(String userGuid) {
        showLoadingIndicator();
        restService.create(NetworkService.class).getPesananByDriver(userGuid)
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {
                        if (response.body().getmStatus()) {
                            onDataReady(response.body().getData());
                        } else {
                            SweetDialogs.commonWarning(getActivity(), "Warning", "Gagal Memuat Permintaan", false);
                        }
                        hideLoadingIndicator();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PesananResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    void onDataReady(List<PesananModel> model) {
        List<PesananModel> models = new ArrayList<>();
        for(PesananModel data : model){
            if(data.getStatus_pesanan() != 3)
                models.add(data);
        }
        if(models.isEmpty()) {
            emptyDataDisplay.setVisibility(View.VISIBLE);
            dataAvailable.setVisibility(View.GONE);
        }
        else {
            emptyDataDisplay.setVisibility(View.GONE);
            dataAvailable.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.clearFocus();
            adapter = new AdapterOrderDriver(getActivity(), models);
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