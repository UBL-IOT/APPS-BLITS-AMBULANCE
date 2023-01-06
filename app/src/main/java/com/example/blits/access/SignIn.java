package com.example.blits.access;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.blits.customer.MainCustomer;
import com.example.blits.driver.MainDriver;
import com.example.blits.R;
import com.example.blits.model.ModelUser;
import com.example.blits.service.App;
import com.example.blits.service.BaseURL;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.service.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    Button gotoRegist, gotoDashboard;
    TextInputEditText no_telpon, edtPassword;

    ModelUser modeluser;
    private RequestQueue requestQueue;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        modeluser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        if (Utils.isLoggedIn()){
            int roleUser = Integer.parseInt(modeluser.getRole());

            if (roleUser == 3) {
                startActivity(new Intent(SignIn.this, MainDriver.class));
            } else if(roleUser == 2) {
                startActivity(new Intent(SignIn.this, MainCustomer.class));
            }
        }

        setContentView(R.layout.activity_sign_in);

        requestQueue = Volley.newRequestQueue(this);

        no_telpon = findViewById(R.id.no_telpon);
        edtPassword = findViewById(R.id.password);

        gotoRegist = findViewById(R.id.do_regist);
        gotoDashboard = findViewById(R.id.do_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);

        gotoRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        gotoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telepon = no_telpon.getText().toString();
                String dataPassword = edtPassword.getText().toString();

                if (telepon.isEmpty()) {
                    StyleableToast.makeText(SignIn.this, "Email tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPassword.isEmpty()) {
                    StyleableToast.makeText(SignIn.this, "Password tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else {
                    loginFunction(telepon, dataPassword);
                }
            }
        });
    }

    private void loginFunction(String no_telpon, String dataPassword) {
        HashMap<String, String> parameter = new HashMap<String, String>();

        parameter.put("no_telpon", no_telpon);
        parameter.put("password", dataPassword);

        showDialog();

        final JsonObjectRequest request = new JsonObjectRequest(BaseURL.login, new JSONObject(parameter),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String msg = response.getString("message");
                            Log.d("Messagenya" , msg);
                            boolean statusMsg = response.getBoolean("status");

                            if (statusMsg == true) {
                                JSONObject data = response.getJSONObject("data");
                                JSONObject dataUser = data.getJSONObject("user");

                                String roleUser = dataUser.getString("role");
                                App.getPref().put(Prefs.PREF_IS_LOGEDIN, true);
                                Utils.storeProfile(dataUser.toString());

                                if (roleUser.equals("3")) {
                                    startActivity(new Intent(SignIn.this, MainDriver.class));
                                } else if (roleUser.equals("2")) {
                                    startActivity(new Intent(SignIn.this, MainCustomer.class));
                                }
                            } else {
                                StyleableToast.makeText(SignIn.this, msg, R.style.toastStyleWarning).show();
                            }
                        } catch (JSONException e) {
                            Log.d("Erornya" , e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.d("Erornya" , error.getMessage());
                hideDialog();
            }
        });
        requestQueue.add(request);
    }

    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void hideDialog() {
        if (progressDialog.isShowing()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 3000);
            progressDialog.setContentView(R.layout.loading);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }
}