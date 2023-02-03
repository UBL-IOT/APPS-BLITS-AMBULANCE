package com.example.blits.access;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blits.R;
import com.example.blits.customer.MainCustomer;
import com.example.blits.customer.Order;
import com.example.blits.driver.MainDriver;
import com.example.blits.model.ModelUser;
import com.example.blits.network.NetworkService;
import com.example.blits.network.RestService;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.ui.SweetDialogs;
import com.example.blits.ui.TopSnakbar;
import com.example.blits.util.CommonRespon;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfile extends AppCompatActivity {
    @BindView(R.id.mNama)
    EditText mNama;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.address)
    EditText address;

    @BindView(R.id.mSubmit)
    Button mSubmit;
    public final Retrofit restService = RestService.getRetrofitInstance();
    ModelUser modelUser ;
    SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        ButterKnife.bind(this);
        sweetAlertDialog = new SweetAlertDialog(this);

        mSubmit.setOnClickListener(view -> this.editProfile());
    }

    void editProfile(){
        ModelUser model = new ModelUser();
        model.setFullname(mNama.getText().toString());
        model.setAlamat(address.getText().toString());
        model.setEmail(email.getText().toString());
        showLoadingIndicator();
        restService.create(NetworkService.class).updateProfile(modelUser.getGuid(),model)
                .enqueue(new Callback<CommonRespon>() {
                    @Override
                    public void onResponse(retrofit2.Call<CommonRespon> call, Response<CommonRespon> response) {
                        hideLoadingIndicator();
                        if(response.body().getSuccess()){
                            SweetDialogs.commonSuccessWithIntent(EditProfile.this, "Berhasil Memuat Permintaan", string -> {
                               gotoProfile();
                            });
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<CommonRespon> call, Throwable t) {
                        hideLoadingIndicator();
                        onNetworkError(t.getLocalizedMessage());
                    }
                });
    }

    public void onNetworkError(String cause) {
        Log.d("Error", cause);
        SweetDialogs.endpointError(this);
//        TopSnakbar.showWarning(this, cause);
    }

    public void gotoProfile(){
        Intent i = new Intent(EditProfile.this, MainCustomer.class);
        i.putExtra("key", this.getClass().getSimpleName());
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Animatoo.animateSlideRight(this);
        super.onBackPressed();
    }

    public void showLoadingIndicator() {
        sweetAlertDialog.show();
    }

    public void hideLoadingIndicator() {
        sweetAlertDialog.dismiss();
    }
}