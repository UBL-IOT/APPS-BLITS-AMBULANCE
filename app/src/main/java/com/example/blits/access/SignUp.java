package com.example.blits.access;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.blits.R;
import com.example.blits.TermCondition;
import com.example.blits.model.ModelUser;
import com.example.blits.service.BaseURL;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class SignUp extends AppCompatActivity {

    Button gotoLogin, gotoRegist;
    TextInputEditText edtFullname, edtUsername, edtPassword, edtEmail, edtPhone;
    TextView termCondition;
    CheckBox checkBox;
    CardView takePhotoKTP, takePhotoSelfie, takeKTPData, takeSelfieData;
    ImageView ktpData, selfieData;

    private RequestQueue requestQueue;

    ProgressDialog progressDialog;

    Bitmap bitmapKTP, bitmapSelfie;
    private String imageFile;

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
//        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
//        edtEmail = findViewById(R.id.email);
        edtPhone = findViewById(R.id.phone);

        termCondition = findViewById(R.id.term_condition);
        termCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, TermCondition.class));
            }
        });

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
//                String dataUsername = edtUsername.getText().toString();
                String dataPassword = edtPassword.getText().toString();
//                String dataEmail = edtEmail.getText().toString();
                String dataPhone = edtPhone.getText().toString();

                if (dataFullname.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Nama lengkap tidak boleh kosong ...", R.style.toastStyleWarning).show();
//                } else if (dataUsername.isEmpty()) {
//                    StyleableToast.makeText(SignUp.this, "Username tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPassword.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Password tidak boleh kosong ...", R.style.toastStyleWarning).show();
//                } else if (dataEmail.isEmpty()) {
//                    StyleableToast.makeText(SignUp.this, "Email tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPassword.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "No. Telepon tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else {
                    registFunction(dataFullname, dataPassword, dataPhone);
                }
            }
        });

        checkBox = findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    gotoRegist.setEnabled(true);
                    gotoRegist.setBackground(getDrawable(R.drawable.button_onboard));
                } else {
                    gotoRegist.setEnabled(false);
                    gotoRegist.setBackground(getDrawable(R.drawable.button_onboard_disabled));
                }
            }
        });

        takePhotoKTP = findViewById(R.id.takeKTP);
        takePhotoSelfie = findViewById(R.id.takeSelfie);
        takeKTPData = findViewById(R.id.takeKTPData);
        takeSelfieData = findViewById(R.id.takeSelfieData);
        ktpData = findViewById(R.id.dataKTP);
        selfieData = findViewById(R.id.dataSelfie);

        takePhotoKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takeCameraFunctionKtp();
                } else {
                    ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
    }

    private void takeCameraFunctionKtp() {
        addPermission();
        final CharSequence[] options = {"Ambil Foto", "Pilih dari Gallery", "Cancle"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Tambahkan Foto");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Ambil Foto")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, 1);
                } else if (options[which].equals("Pilih dari Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[which].equals("Cancle")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                File file = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : file.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        file = temp;
                        break;
                    }
                }
                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapKTP = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapOptions);
                    bitmapKTP = getResizedBitmap(bitmapKTP, 400);
                    ktpData.setImageBitmap(bitmapKTP);
                    takePhotoKTP.setVisibility(View.GONE);
                    takeKTPData.setVisibility(View.VISIBLE);
                    BitMapToString(bitmapKTP);
                    String path = Environment.getExternalStorageDirectory() + File.separator + "blits" + File.separator + "default";
                    file.delete();
                    OutputStream outFile = null;
                    File file_a = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    try {
                        file_a.mkdirs();
                        outFile = new FileOutputStream(file_a);
                        bitmapKTP.compress(Bitmap.CompressFormat.JPEG, 20, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                bitmapKTP = (BitmapFactory.decodeFile(picturePath));
                bitmapKTP = getResizedBitmap(bitmapKTP, 400);
                Log.w("path of", picturePath + "");
                ktpData.setImageBitmap(bitmapKTP);
                selfieData.setImageBitmap(bitmapSelfie);
                takePhotoKTP.setVisibility(View.GONE);
                takeKTPData.setVisibility(View.VISIBLE);
                BitMapToString(bitmapKTP);
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 20, baos);
        byte[] b = baos.toByteArray();
        imageFile = Base64.encodeToString(b, Base64.DEFAULT);
        return imageFile;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void addPermission() {
        Dexter.withActivity(SignUp.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(SignUp.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void registFunction(String dataFullname, String dataPassword, String dataPhone) {
        HashMap<String, String> parameter = new HashMap<String, String>();

        parameter.put("fullname", dataFullname);
//        parameter.put("username", dataUsername);
        parameter.put("password", dataPassword);
//        parameter.put("email", dataEmail);
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