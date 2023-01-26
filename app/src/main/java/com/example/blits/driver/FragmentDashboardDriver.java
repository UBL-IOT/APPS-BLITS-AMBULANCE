package com.example.blits.driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.provider.Settings;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.osmdroid.util.Distance;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentDashboardDriver extends Fragment {

    LinearLayout emptyDataDisplay, dataAvailable, pickOrderHint, dropOrderHint;
    TextView fullnameData, mOrderCode, mCustomerName, mCustomerPhone, mPickUpAddress, mDeliverAddress, mTime, mDistance, mTxtSubmit;
    String userGuid;
    ModelUser modelUser;
    CardView mPickUpButton;

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;

    Dialog dialog;
    SweetAlertDialog sweetAlertDialog;
    double latMe, lngMe;

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
        pickOrderHint = v.findViewById(R.id.pickOrderMaps);
        dropOrderHint = v.findViewById(R.id.dropOrderMaps);

        fullnameData.setText(modelUser.getFullname());

        getOrders(userGuid);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        return v;
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        latMe = location.getLatitude();
                        lngMe = location.getLongitude();
                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location locationOne = locationResult.getLastLocation();
                                latMe = locationOne.getLatitude();
                                lngMe = locationOne.getLongitude();
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
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
        int StatusPesanan;
        mOrderCode.setText(order.getKode_pesanan());
        mCustomerName.setText(order.getData_user().getFullname());
        mCustomerPhone.setText(order.getData_user().getNo_telpon());

        String pickOrder = order.getTitik_jemput();
        String dropOrder = order.getTujuan();

        if (pickOrder.length() > 40) {
            pickOrder = pickOrder.substring(0, 39) + "...";
            mPickUpAddress.setText(pickOrder);
        } else {
            mPickUpAddress.setText(order.getTitik_jemput());
        }

        if (dropOrder.length() > 40) {
            dropOrder = dropOrder.substring(0, 39) + "...";
            mDeliverAddress.setText(dropOrder);
        } else {
            mDeliverAddress.setText(order.getTujuan());
        }

        double dataLatPick = Double.parseDouble(order.getTitik_jemput_lat());
        double dataLongPick = Double.parseDouble(order.getTitik_jemput_long());

        double dataLatDrop = Double.parseDouble(order.getTujuan_lat());
        double dataLongDrop = Double.parseDouble(order.getTujuan_long());

        pickOrderHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?saddr=" + latMe + "," + lngMe + "&daddr=" + dataLatPick + "," + dataLongPick;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        dropOrderHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?saddr=" + latMe + "," + lngMe + "&daddr=" + dataLatDrop + "," + dataLongDrop;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

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