package com.example.blits.access;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
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
import com.example.blits.R;
import com.example.blits.model.ModelUser;
import com.example.blits.service.BaseURL;
import com.google.android.material.textfield.TextInputEditText;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    Button gotoLogin, gotoRegist;
    TextInputEditText edtFullname, edtUsername, edtPassword, edtEmail, edtPhone;

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

        setContentView(R.layout.activity_sign_up);

        requestQueue = Volley.newRequestQueue(this);

        edtFullname = findViewById(R.id.fullname);
        edtFullname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        edtEmail = findViewById(R.id.email);
        edtPhone = findViewById(R.id.phone);

        gotoLogin = findViewById(R.id.do_login);
        gotoRegist = findViewById(R.id.do_regist);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });

        gotoRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataFullname = edtFullname.getText().toString();
                String dataUsername = edtUsername.getText().toString();
                String dataPassword = edtPassword.getText().toString();
                String dataEmail = edtEmail.getText().toString();
                String dataPhone = edtPhone.getText().toString();

                if (dataFullname.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Nama lengkap tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataUsername.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Username tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPassword.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Password tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataEmail.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Email tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPassword.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "No. Telepon tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else {
                    registFunction(dataFullname, dataUsername, dataPassword, dataEmail, dataPhone);
                }
            }
        });
    }

    private void registFunction(String dataFullname, String dataUsername, String dataPassword, String dataEmail, String dataPhone) {
        HashMap<String, String> parameter = new HashMap<String, String>();

        parameter.put("fullname", dataFullname);
        parameter.put("username", dataUsername);
        parameter.put("password", dataPassword);
        parameter.put("email", dataEmail);
        parameter.put("no_telpon", dataPhone);
        parameter.put("role", "2");

        showDialog();

        final JsonObjectRequest request = new JsonObjectRequest(BaseURL.register, new JSONObject(parameter),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String msg = response.getString("message");
                            boolean statusMsg = response.getBoolean("status");

                            if (statusMsg == true) {
                                StyleableToast.makeText(SignUp.this, msg, R.style.toastStyleSuccess).show();
                                startActivity(new Intent(SignUp.this, SignIn.class));
                            } else {
                                StyleableToast.makeText(SignUp.this, msg, R.style.toastStyleWarning).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
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