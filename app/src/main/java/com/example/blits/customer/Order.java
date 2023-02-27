package com.example.blits.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blits.BuildConfig;
import com.example.blits.R;
import com.example.blits.model.ModelUser;
import com.example.blits.model.PesananModel;
import com.example.blits.network.NetworkService;
import com.example.blits.network.RestService;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.ui.SweetDialogs;
import com.example.blits.ui.TopSnakbar;
import com.example.blits.util.CommonRespon;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Order extends AppCompatActivity {

    @BindView(R.id.mBtnPesanan)
    Button mBtnPesanan;
    private MapView map = null;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder, geocoderPicker;
    GeoPoint point, pointPicker;
    Marker startMarker, pickerMarker;
    ModelUser modelUser;
    List<Address> addresses, addressesPicker;
    SweetAlertDialog sweetAlertDialog;
    double latMe, lngMe;
    public final Retrofit restService = RestService.getRetrofitInstance();

    TextInputEditText edtJemput, edtAntar;
    ImageView btnMyLocation, btnBack;
    SSLContext ssl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        ButterKnife.bind(this);
        btnMyLocation = findViewById(R.id.myLocation);
        btnBack = findViewById(R.id.arrowBack);

        try {
            ssl = SSLContext.getInstance("SSL");
            ssl.init(null, trustAllCerts, null);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edtJemput = findViewById(R.id.jemput);
        edtAntar = findViewById(R.id.antar);
        edtAntar.setEnabled(false);
        edtJemput.setEnabled(false);
        Context ctx = this.getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET
        });

        map = findViewById(R.id.mapview);
        map.getTileProvider().clearTileCache();
        Configuration.getInstance().setCacheMapTileCount((short) 12);
        Configuration.getInstance().setCacheMapTileOvershoot((short) 12);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        map.setTileSource(new XYTileSource("HttpMapnik", 0, 19, 256, ".png", new String[]{
                "http://a.tile.openstreetmap.org/",
                "http://b.tile.openstreetmap.org/",
                "http://c.tile.openstreetmap.org/"
        },
                "© BLITS contributors"));

        map.getController().setZoom(17.0);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        geocoderPicker = new Geocoder(this, Locale.getDefault());

        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);
        map.setBuiltInZoomControls(false);
        map.invalidate();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
            }
        });

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                pointPicker = new GeoPoint(p.getLatitude(), p.getLongitude());

                pickerMarker = new Marker(map);
                pickerMarker.setPosition(pointPicker);
                pickerMarker.setIcon(getResources().getDrawable(R.drawable.ic_send));
                pickerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                pickerMarker.setId("sender");
                pickerMarker.setTitle("TITIK ANTAR");
                map.getOverlays().add(pickerMarker);

                for (int i = 0; i < map.getOverlays().size(); i++) {
                    Overlay overlay = map.getOverlays().get(i);
                    if (overlay instanceof Marker && (pickerMarker.getId().equals("sender"))) {
                        map.getOverlays().remove(overlay);
                    }
                }

                try {
                    addressesPicker = geocoderPicker.getFromLocation(p.getLatitude(), p.getLongitude(), 1);
                    edtAntar.setText(addressesPicker.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                map.getController().setCenter(pointPicker);

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        map.getOverlays().add(new MapEventsOverlay(mapEventsReceiver));
        this.initView();
    }

    void initView() {
        mBtnPesanan.setOnClickListener(view -> this.Pesanan());
    }

    void Pesanan() {
        if (edtJemput.getText().toString().isEmpty())
            TopSnakbar.showWarning(this, "Maaf anda belum memilih tujuan");
        else if (edtAntar.getText().toString().isEmpty())
            TopSnakbar.showWarning(this, "Maaf anda belum memilih tujuan");
        else {
            PesananModel model = new PesananModel();

            Random rand = new Random();
            int kode = rand.nextInt(99999999);

            String kodeData = "BLITS-" + kode;

            model.setGuid_user(modelUser.getGuid());
            model.setTujuan(addressesPicker.get(0).getAddressLine(0));
            model.setTitik_jemput(addresses.get(0).getAddressLine(0));
            model.setStatus_pesanan(0);
            model.setTujuan_lat(String.valueOf(addressesPicker.get(0).getLatitude()));
            model.setTujuan_long(String.valueOf(addressesPicker.get(0).getLongitude()));
            model.setTitik_jemput_lat(String.valueOf(addresses.get(0).getLatitude()));
            model.setTitik_jemput_long(String.valueOf(addresses.get(0).getLongitude()));
            model.setKode_pesanan(kodeData);
            showLoadingIndicator();
            restService.create(NetworkService.class).createPesanan(model)
                    .enqueue(new Callback<CommonRespon>() {
                        @Override
                        public void onResponse(retrofit2.Call<CommonRespon> call, Response<CommonRespon> response) {
                            hideLoadingIndicator();
                            if (response.body().getSuccess()) {
                                SweetDialogs.commonSuccessWithIntent(Order.this, "Berhasil Melakukan Pemesanan", string -> {
                                    gotoListOrder();
                                });
                            } else {
                                SweetDialogs.commonInvalidToken(Order.this, "Gagal Memuat Permintaan", response.body().getmRm());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<CommonRespon> call, Throwable t) {
                            hideLoadingIndicator();
                            onNetworkError(t.getLocalizedMessage());
                        }
                    });
            hideLoadingIndicator();
        }
    }

    public void onNetworkError(String cause) {
        Log.d("Error", cause);
        TopSnakbar.showWarning(this, cause);
    }

    @Override
    public void onBackPressed() {
        Animatoo.animateSlideRight(this);
        super.onBackPressed();
    }

    void gotoListOrder() {
        Intent i = new Intent(this, MainCustomer.class);
        i.putExtra("key", this.getClass().getSimpleName());
        startActivity(i);
        finish();
    }

    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        latMe = location.getLatitude();
                        lngMe = location.getLongitude();

                        point = new GeoPoint(location.getLatitude(), location.getLongitude());

                        startMarker = new Marker(map);
                        startMarker.setPosition(point);
                        startMarker.setIcon(getResources().getDrawable(R.drawable.ic_pick));
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                        startMarker.setTitle("TITIK JEMPUT");
                        startMarker.setId("picker");
                        map.getOverlays().add(startMarker);

                        map.getController().setCenter(point);

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            edtJemput.setText(addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).setFastestInterval(1000).setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location locationOne = locationResult.getLastLocation();
                                latMe = locationOne.getLatitude();
                                lngMe = locationOne.getLongitude();

                                point = new GeoPoint(location.getLatitude(), location.getLongitude());

                                startMarker = new Marker(map);
                                startMarker.setPosition(point);
                                startMarker.setIcon(getResources().getDrawable(R.drawable.ic_pick));
                                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                startMarker.setTitle("TITIK JEMPUT");
                                startMarker.setId("picker");
                                map.getOverlays().add(startMarker);

                                map.getController().setCenter(point);

                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    edtJemput.setText(addresses.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
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
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void showLoadingIndicator() {
        sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        SweetDialogs.Loading(this, sweetAlertDialog, "Memuat...", 1);
    }

    public void hideLoadingIndicator() {
        SweetDialogs.Loading(this, sweetAlertDialog, "Memuat...", 2);
    }

    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};
}