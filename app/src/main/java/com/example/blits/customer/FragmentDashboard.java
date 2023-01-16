package com.example.blits.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.blits.adapter.AdapterDashboardDriver;
import com.example.blits.model.DriverModel;
import com.example.blits.model.ModelUser;
import com.example.blits.R;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentDashboard extends Fragment {

    LinearLayout emptyDataDisplay;

    TextView mCountDriver, fullnameData, mKodePesanan, mStatusPesanan;
    LinearLayout mCardPesanan;
    RecyclerView recyclerViewDashboard;
    RecyclerView.Adapter recyclerViewDashboardAdapter;


    ModelUser modelUser;
    SweetAlertDialog sweetAlertDialog;
    private RequestQueue requestQueue;
    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(getActivity());

        emptyDataDisplay = v.findViewById(R.id.emptyDataDisplay);

        mCountDriver = v.findViewById(R.id.mCountDriver);
        fullnameData = v.findViewById(R.id.fullname);
        mCardPesanan = v.findViewById(R.id.mCardPesanan);
        mKodePesanan = v.findViewById(R.id.mKodePesanan);
        mStatusPesanan = v.findViewById(R.id.mStatusPesanan);

        requestQueue = Volley.newRequestQueue(getActivity());
        sweetAlertDialog = new SweetAlertDialog(getActivity());
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        fullnameData.setText(modelUser.getFullname());
        recyclerViewDashboard = v.findViewById(R.id.data_dashboard_customer);
//        this.getCountDriver();
        getListPenanan();

        FragmentDashboard.this.ListDriver();

        return v;
    }

    private void ListDriver() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getListDriver()
                .enqueue(new Callback<DriverResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<DriverResponse> call, Response<DriverResponse> response) {
                        hideLoadingIndicator();
                        if (response.body().getmStatus()) {
                            Log.d("goblok" , "tolol");
                            onDataReady(response.body().getData());
                        } else {
                            SweetDialogs.commonInvalidToken(getActivity(), "Gagal Memuat Permintaan", response.body().getmRm());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<DriverResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    void onDataReady(List<DriverModel> model) {
        mCountDriver.setText(String.valueOf(model.size()));
        List<DriverModel> drivers = new ArrayList<>();

        for(int i = 0; i < model.size(); i++) {
            if (Integer.parseInt(model.get(i).getStatus_driver()) == 0 ){
                drivers.add(model.get(i));
            }

        }
        Log.d("driver",String.valueOf(drivers.size()));
//        emptyDataDisplay.setVisibility(View.VISIBLE);
        if (drivers.size() == 0) {

            Log.d("driver", String.valueOf(drivers.size()));
            emptyDataDisplay.setVisibility(View.VISIBLE);
        }else {
            recyclerViewDashboard.setHasFixedSize(true);
            recyclerViewDashboard.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerViewDashboard.clearFocus();
            recyclerViewDashboardAdapter = new AdapterDashboardDriver(getActivity(), drivers);
            recyclerViewDashboard.setAdapter(recyclerViewDashboardAdapter);
        }
    }

    private void getListPenanan() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getPesanan(modelUser.getGuid())
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {
                        hideLoadingIndicator();
                        if (response.body().getmStatus()) {
                            List<PesananModel> orders = response.body().getData();
                            Log.d("orders", new Gson().toJson(orders));

                            mCardPesanan.setVisibility(View.GONE);
                            if (!orders.isEmpty()) {
                                mCardPesanan.setVisibility(View.VISIBLE);
                                mKodePesanan.setText(orders.get(0).getKode_pesanan());
                                if(orders.get(0).getStatus_pesanan() == 0) {
                                    mStatusPesanan.setText("Menunggu");
                                }
                                if(orders.get(0).getStatus_pesanan() == 1) {
                                    mStatusPesanan.setText("Jemput");
                                }
                                if(orders.get(0).getStatus_pesanan() == 2) {
                                    mStatusPesanan.setText("Antar");
                                }
                                if(orders.get(0).getStatus_pesanan() == 3) {
                                    mCardPesanan.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            SweetDialogs.commonInvalidToken(getActivity(), "Gagal Memuat Permintaan", response.body().getmRm());
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PesananResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
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
}