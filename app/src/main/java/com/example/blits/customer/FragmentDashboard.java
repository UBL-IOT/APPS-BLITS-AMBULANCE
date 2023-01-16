package com.example.blits.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.blits.adapter.AdapterDashboardDriver;
import com.example.blits.model.DriverModel;
import com.example.blits.model.ModelUser;
import com.example.blits.R;
import com.example.blits.network.NetworkService;
import com.example.blits.network.RestService;
import com.example.blits.response.DriverResponse;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.ui.SweetDialogs;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentDashboard extends Fragment {

    RecyclerView recyclerViewDashboard;
    RecyclerView.Adapter recyclerViewDashboardAdapter;

    TextView fullnameData;

    ModelUser modelUser;
    SweetAlertDialog sweetAlertDialog;
    private RequestQueue requestQueue;
    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        sweetAlertDialog = new SweetAlertDialog(getActivity());
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        fullnameData = v.findViewById(R.id.fullname);
        fullnameData.setText(modelUser.getFullname());
        recyclerViewDashboard = v.findViewById(R.id.data_dashboard_customer);

        ListDriver();
        
        return v;
    }

    private void ListDriver() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getListDriver()
                .enqueue(new Callback<DriverResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<DriverResponse> call, Response<DriverResponse> response) {
                        hideLoadingIndicator();
                        if(response.body().getmStatus()) {
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

    private void getCountDriver() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getListDriver()
                .enqueue(new Callback<DriverResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<DriverResponse> call, Response<DriverResponse> response) {
                        hideLoadingIndicator();
                        if(response.body().getmStatus())
                            onDataReady(response.body().getData());
                        else
                            SweetDialogs.commonInvalidToken(getActivity() ,"Gagal Memuat Permintaan" ,response.body().getmRm());
                    }

                    @Override
                    public void onFailure(retrofit2.Call<DriverResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    void onDataReady(List<DriverModel> model){
        recyclerViewDashboard.setHasFixedSize(true);
        recyclerViewDashboard.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewDashboard.clearFocus();
        recyclerViewDashboardAdapter = new AdapterDashboardDriver(getActivity(), model);
        recyclerViewDashboard.setAdapter(recyclerViewDashboardAdapter);
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