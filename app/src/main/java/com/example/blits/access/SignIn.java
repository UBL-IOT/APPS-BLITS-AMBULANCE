package com.example.blits.access;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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
    ProgressDialog progressDialog;

    ModelUser modeluser;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_sign_in);
        modeluser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        if(App.getPref().getBoolean(Prefs.PREF_FIRST_TIME,false)){
            if (Utils.isLoggedIn()) {
                int roleUser = Integer.parseInt(modeluser.getRole());
                if (roleUser == 3) {
                    startActivity(new Intent(SignIn.this, MainDriver.class));
                } else if (roleUser == 2) {
                    startActivity(new Intent(SignIn.this, MainCustomer.class));
                }
            }
        }



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
                    StyleableToast.makeText(SignIn.this, "Telepon tidak boleh kosong ...", R.style.toastStyleWarning).show();
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
                            boolean statusMsg = response.getBoolean("status");

                            if (statusMsg == true) {
                                JSONObject data = response.getJSONObject("data");
                                JSONObject dataUser = data.getJSONObject("user");

                                String roleUser = dataUser.getString("role");
                                App.getPref().put(Prefs.PREF_FIRST_TIME, true);
                                App.getPref().put(Prefs.PREF_IS_LOGEDIN, true);
                                Utils.storeProfile(dataUser.toString());
                                Log.d("profilepresp", dataUser.toString());

                                if (roleUser.equals("3")) {
                                    startActivity(new Intent(SignIn.this, MainDriver.class));
                                } else if (roleUser.equals("2")) {
                                    startActivity(new Intent(SignIn.this, MainCustomer.class));
                                }else{
                                    StyleableToast.makeText(SignIn.this, msg, R.style.toastStyleWarning).show();
                                }
                            } else {
                                StyleableToast.makeText(SignIn.this, msg, R.style.toastStyleWarning).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Log.d("Erornya", error.getMessage());
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

//    public void showPermissionGranted(String permission) {
//        TextView feedbackView = getFeedbackViewForPermission(permission);
//        feedbackView.setText(R.string.permission_granted_feedback);
//        feedbackView.setTextColor(ContextCompat.getColor(this, R.color.permission_granted));
//    }
//
//    public void showPermissionDenied(String permission, boolean isPermanentlyDenied) {
//        TextView feedbackView = getFeedbackViewForPermission(permission);
//        feedbackView.setText(isPermanentlyDenied ? R.string.permission_permanently_denied_feedback
//                : R.string.permission_denied_feedback);
//        feedbackView.setTextColor(ContextCompat.getColor(this, R.color.permission_denied));
//    }

    //    private TextView getFeedbackViewForPermission(String name) {
//        TextView feedbackView;
//
//        switch (name) {
//            case Manifest.permission.CAMERA:
//                feedbackView = cameraPermissionFeedbackView;
//                break;
//            case Manifest.permission.READ_CONTACTS:
//                feedbackView = contactsPermissionFeedbackView;
//                break;
//            case Manifest.permission.RECORD_AUDIO:
//                feedbackView = audioPermissionFeedbackView;
//                break;
//            default:
//                throw new RuntimeException("No feedback view for this permission");
//        }
//
//        return feedbackView;
//    }
    public static boolean isLoggedIn() {
        return App.getPref().getBoolean(Prefs.PREF_IS_LOGEDIN, false);
    }
}