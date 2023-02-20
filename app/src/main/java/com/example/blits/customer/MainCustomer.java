package com.example.blits.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainCustomer extends AppCompatActivity {

    private static final String TAG = MainCustomer.class.getSimpleName();
    boolean backPress = false;
    public final Retrofit restService = RestService.getRetrofitInstance();

    Fragment fragment;
    FragmentManager fragmentManager;
    BottomAppBar bottomAppBars;
    FloatingActionButton btnOrder;
    SweetAlertDialog sweetAlertDialog;
    ModelUser modelUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
//        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
        Intent iin = getIntent();
        Bundle extras = iin.getExtras();

        if (extras != null) {
            String key = (String) extras.get("key");
            if (key.equals(EditProfile.class.getSimpleName()))
                loadFragment(new FragmentProfile());
            if (key.equals(Order.class.getSimpleName()))
                loadFragment(new FragmentOrder());
        } else {
            loadFragment(new FragmentDashboard());
        }

        bottomAppBars = findViewById(R.id.bottomAppBar);
        btnOrder = findViewById(R.id.order);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainCustomer.this, Order.class));
            }
        });

        bottomAppBars.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragment = null;
                switch (item.getItemId()) {
                    case R.id.dashboard_customer:
                        cekPesanan();
                        fragment = new FragmentDashboard();
                        break;
                    case R.id.order_customer:
                        cekPesanan();
                        fragment = new FragmentOrder();
                        break;
                    case R.id.me_customer:
                        cekPesanan();
                        fragment = new FragmentProfile();
                        break;
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else {
                    Log.e(TAG, "Error Creating Fragment");
                }

                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.fragment_container, fragment).commit();
        t.addToBackStack(null);
    }

    void cekPesanan() {
        showLoadingIndicator();
        restService.create(NetworkService.class).getPesanan(modelUser.getGuid())
                .enqueue(new Callback<PesananResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<PesananResponse> call, Response<PesananResponse> response) {

                        if (response.body().getmStatus())
                            onDataReady(response.body().getData());
                        else
                            SweetDialogs.commonWarning(MainCustomer.this, "Warning", "Gagal Memuat Permintaan", false);
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
        if (model.size() > 0)
            if (model.get(0).getStatus_pesanan() != 3)
                btnOrder.setEnabled(false);
            else
                btnOrder.setEnabled(true);
    }

    public void onNetworkError(String cause) {
        Log.d("Error", cause);
        SweetDialogs.endpointError(this);
    }

    @Override
    public void onBackPressed() {
        if (backPress) {
            super.onBackPressed();
            return;
        }

        this.backPress = true;
        StyleableToast.makeText(MainCustomer.this, "Tekan sekali lagi untuk keluar ...", R.style.toastStyleWarning).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPress = false;
            }
        }, 2000);
    }

    public void showLoadingIndicator() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        SweetDialogs.Loading(this,sweetAlertDialog,"Memuat...", 1);
    }

    public void hideLoadingIndicator() {
        SweetDialogs.Loading(this,sweetAlertDialog,"Memuat...", 2);
    }
}