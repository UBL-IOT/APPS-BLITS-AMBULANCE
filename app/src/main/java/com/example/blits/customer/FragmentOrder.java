package com.example.blits.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.blits.R;
import com.example.blits.adapter.AdapterDashboardDriver;
import com.example.blits.adapter.AdapterPemesanan;
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
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentOrder extends Fragment implements AdapterPemesanan.onSelected {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager horizontalLayout;

    TextView fullnameData;

    List<ModelUser> modelDashboard;
    ModelUser modelUser;
    SweetAlertDialog sweetAlertDialog;
    private RequestQueue requestQueue;
    public final Retrofit restService = RestService.getRetrofitInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        sweetAlertDialog = new SweetAlertDialog(getActivity());
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        mRecyclerView = v.findViewById(R.id.mRecyclerView);
        ListPesanan();
        return v;
    }

    private void ListPesanan() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getPesanan(modelUser.getGuid())
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {
                        hideLoadingIndicator();
//                        onDataReady(response.body());
                        Log.d("datanyainiwoi" , new Gson().toJson(response.body()));
                        if(response.body().getmStatus())
                            onDataReady(response.body().getData());
                        else
                            SweetDialogs.commonInvalidToken(getActivity() ,"Gagal Memuat Permintaan" ,response.body().getmRm());
//
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PesananResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    private void getDriver(String guid_driver) {
        Log.d("guid drivery" , guid_driver);
        showLoadingIndicator();
        restService.create(NetworkService.class).getDriverByGuid(guid_driver)
                .enqueue(new Callback<DriverResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<DriverResponse> call, Response<DriverResponse> response) {
                        hideLoadingIndicator();
                        Log.d("datanyainiwois" , new Gson().toJson(response.body()));

//                        if(response.body().getmStatus())
//                            onDataReady(response.body().getData());
//                        else
//                            SweetDialogs.commonInvalidToken(getActivity() ,"Gagal Memuat Permintaan" ,response.body().getmRm());
//
                    }

                    @Override
                    public void onFailure(retrofit2.Call<DriverResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    void onDataReady(List<PesananModel> model){
        Log.d("datadriver" , new Gson().toJson(model));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.clearFocus();
        adapter = new AdapterPemesanan(getActivity(), model, FragmentOrder.this);
        mRecyclerView.setAdapter(adapter);
    }

    public void showLoadingIndicator() {
        sweetAlertDialog.show();
    }

    public void hideLoadingIndicator() {
        sweetAlertDialog.dismiss();
    }

    public void onNetworkError(String cause) {
        Log.d("Error", cause);
        SweetDialogs.endpointError(getActivity());
    }

    @Override
    public void onDetailDriver(PesananModel data) {
//        Log.d("datadriver" ,new Gson().toJson(data)) ;
        getDriver(data.getGuid_driver());
    }
}