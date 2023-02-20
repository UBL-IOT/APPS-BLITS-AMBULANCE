package com.example.blits.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.blits.R;
import com.example.blits.adapter.AdapterHistoryOrderCustomer;
import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.example.blits.network.NetworkService;
import com.example.blits.network.RestService;
import com.example.blits.response.PesananResponse;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.ui.SweetDialogs;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderHistory extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter adapter;
    SweetAlertDialog sweetAlertDialog;
    ModelUser modelUser;
    LinearLayout dataAvailable , emptyDataDisplay;

    public final Retrofit restService = RestService.getRetrofitInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mRecyclerView = findViewById(R.id.mRecyclerView);
        dataAvailable = findViewById(R.id.dataAvailable);
        emptyDataDisplay = findViewById(R.id.emptyDataDisplay);
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        this.ListHistoryPesanan();
    }

    private void ListHistoryPesanan() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getHistoryPesananByUser(modelUser.getGuid())
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {
                        hideLoadingIndicator();
                        if (response.body().getmStatus())
                            onDataReady(response.body().getData());
                        else
                            SweetDialogs.commonWarning(OrderHistory.this, "Warning", "Gagal Memuat Permintaan", false);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<PesananResponse> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
        hideLoadingIndicator();
    }

    void onDataReady(List<PesananModel> model) {
        if(model.isEmpty()) {
            emptyDataDisplay.setVisibility(View.VISIBLE);
            dataAvailable.setVisibility(View.GONE);
        }else {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.clearFocus();
            adapter = new AdapterHistoryOrderCustomer(this, model);
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void showLoadingIndicator() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        SweetDialogs.Loading(this,sweetAlertDialog,"Memuat...", 1);
    }

    public void hideLoadingIndicator() {
        SweetDialogs.Loading(this,sweetAlertDialog,"Memuat...", 2);
    }

    public void onNetworkError(String cause) {
        SweetDialogs.endpointError(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}