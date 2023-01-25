package com.example.blits.driver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.blits.R;
import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.example.blits.network.NetworkService;
import com.example.blits.network.RestService;
import com.example.blits.response.PesananResponse;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.ui.SweetDialogs;
import com.example.blits.util.CommonRespon;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.osmdroid.util.Distance;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentDashboardDriver extends Fragment {

    LinearLayout emptyDataDisplay, dataAvailable;
    TextView fullnameData, mOrderCode, mCustomerName, mCustomerPhone, mPickUpAddress, mDeliverAddress, mTime, mDistance, mTxtSubmit;
    String userGuid;
    ModelUser modelUser;
    CardView mPickUpButton;


    Dialog dialog;
    SweetAlertDialog sweetAlertDialog;


    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_driver, container, false);
        dialog = new Dialog(getActivity());
        sweetAlertDialog = new SweetAlertDialog(getActivity());

        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        userGuid = modelUser.getGuid();

        emptyDataDisplay = v.findViewById(R.id.emptyDataDisplay);
        dataAvailable = v.findViewById(R.id.dataAvailable);

        fullnameData = v.findViewById(R.id.fullname);
        mOrderCode = v.findViewById(R.id.mOrderCode);
        mTxtSubmit = v.findViewById(R.id.mTxtSubmit);
        mPickUpButton = v.findViewById(R.id.mPickUpButton);
        mCustomerName = v.findViewById(R.id.mCustomerName);
        mCustomerPhone = v.findViewById(R.id.mCustomerPhone);
        mPickUpAddress = v.findViewById(R.id.mPickUpAddress);
        mDeliverAddress = v.findViewById(R.id.mDeliverAddress);
        mTime = v.findViewById(R.id.mTime);
        mDistance = v.findViewById(R.id.mDistance);

        fullnameData.setText(modelUser.getFullname());

        getOrders(userGuid);

        return v;
    }


    private void getOrders(String userGuid) {
        showLoadingIndicator();
        restService.create(NetworkService.class).getPesananByDriver(userGuid)
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {
                        if (response.body().getmStatus()) {
                            if (response.body().getData().size() > 0) {
                                onDataReady(response.body().getData().get(0));
                            } else {
                                emptyDataDisplay.setVisibility(View.VISIBLE);
                                dataAvailable.setVisibility(View.GONE);
                            }
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

    private void pickOrders(String userGuid, int statusPesanan) {
        PesananModel model = new PesananModel();
        model.setStatus_pesanan(statusPesanan);
        showLoadingIndicator();
        restService.create(NetworkService.class).pickOrder(userGuid, model)
                .enqueue(new Callback<CommonRespon>() {
                    @Override
                    public void onResponse(retrofit2.Call<CommonRespon> call, Response<CommonRespon> response) {
                        if (response.body().getSuccess()) {
                            SweetDialogs.commonSuccessWithIntent(getActivity(), "Berhasil Memuat Permintaan", string -> {
                                startActivity(new Intent(getActivity(), MainDriver.class));
                            });
//                            startActivity(new Intent(getActivity() , MainDriver.class));
                        } else {
                            SweetDialogs.commonWarning(getActivity(), "Warning", "Gagal Memuat Permintaan", true);
                        }
                        hideLoadingIndicator();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<CommonRespon> call, Throwable t) {
                        Log.d("pesannya", t.getLocalizedMessage());
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }


    private void onDataReady(PesananModel order) {
//        Log.d("datanya" , ""+ Distance.getProjectionFactorToLine());
        int StatusPesanan;
        mOrderCode.setText(order.getKode_pesanan());
        mCustomerName.setText(order.getData_user().getFullname());
        mCustomerPhone.setText(order.getData_user().getNo_telpon());
        mPickUpAddress.setText(order.getTitik_jemput());
        mDeliverAddress.setText(order.getTujuan());
        if (order.getStatus_pesanan() == 1) {
            mTxtSubmit.setText("PICK ORDER");
            StatusPesanan = 2;
        } else {
            mTxtSubmit.setText("SELESAI");
            StatusPesanan = 3;
        }
        mPickUpButton.setOnClickListener(view -> SweetDialogs.confirmDialog(getActivity(), "Apakah Anda Yakin ?", "Melakukan Pick Order", "Berhasil Melakukan Checkout!", string -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        pickOrders(order.getGuid(), StatusPesanan);
                    }
                })
        );
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