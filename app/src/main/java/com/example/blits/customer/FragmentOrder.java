package com.example.blits.customer;

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

public class FragmentOrder extends Fragment implements AdapterOrderCustomer.onSelected {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;

    LinearLayout emptyDataDisplay, dataAvailable;

    ModelUser modelUser;
    Dialog dialog;
    ImageButton closePopup;
    TextView fullnameriverData, platDriverData, orderHistory;
    SweetAlertDialog sweetAlertDialog;
    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        dialog = new Dialog(getActivity());
        mRecyclerView = v.findViewById(R.id.mRecyclerView);

        emptyDataDisplay = v.findViewById(R.id.emptyDataDisplay);
        dataAvailable = v.findViewById(R.id.dataAvailable);

        orderHistory = v.findViewById(R.id.history);
        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderHistory.class));
            }
        });

        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        ListPesanan();

        return v;
    }

    private void ListPesanan() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getPesanan(modelUser.getGuid())
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {

                        if (response.body().getmStatus())
                            onDataReady(response.body().getData());
                        else
                            SweetDialogs.commonWarning(getActivity(), "Warning", "Gagal Memuat Permintaan", false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PesananResponse> call, Throwable t) {
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
        hideLoadingIndicator();
    }

    private void getDriver(String guid_driver) {
        showLoadingIndicator();
        restService.create(NetworkService.class).getDriverByGuid(guid_driver)
                .enqueue(new Callback<DriverResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<DriverResponse> call, Response<DriverResponse> response) {
                        showDetailDriver(response.body().getData().get(0));
                    }

                    @Override
                    public void onFailure(retrofit2.Call<DriverResponse> call, Throwable t) {

                        onNetworkError(t.getLocalizedMessage());
                    }
                });

        hideLoadingIndicator();
    }

    private void showDetailDriver(DriverModel data) {
        dialog.setContentView(R.layout.popup_detaildriver);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        fullnameriverData = dialog.findViewById(R.id.fullnameDriver);
        platDriverData = dialog.findViewById(R.id.platDriver);

        fullnameriverData.setText(data.getNama_driver());
        platDriverData.setText(data.getNo_plat());

        closePopup = dialog.findViewById(R.id.closePopKeuntungan);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    void onDataReady(List<PesananModel> model) {
        List<PesananModel> models = new ArrayList<>();

        for (PesananModel data : model) {
            if (data.getStatus_pesanan() != 3)
                models.add(data);
        }

        if (models.isEmpty()) {
            emptyDataDisplay.setVisibility(View.VISIBLE);
            dataAvailable.setVisibility(View.GONE);
        } else {
            emptyDataDisplay.setVisibility(View.GONE);
            dataAvailable.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.clearFocus();
            adapter = new AdapterOrderCustomer(getActivity(), models, FragmentOrder.this);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void showLoadingIndicator() {
        sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        SweetDialogs.Loading(getActivity(),sweetAlertDialog,"Memuat...", 1);
    }

    public void hideLoadingIndicator() {
        SweetDialogs.Loading(getActivity(),sweetAlertDialog,"Memuat...", 2);
    }

    public void onNetworkError(String cause) {
        SweetDialogs.endpointError(getActivity());
    }

    @Override
    public void onDetailDriver(PesananModel data) {
        getDriver(data.getGuid_driver());
    }
}