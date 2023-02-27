package com.example.blits.access;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import android.os.StrictMode;
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
import com.example.blits.assets.TermCondition;
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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class SignUp extends AppCompatActivity {

    Button gotoLogin, gotoRegist;
    TextInputEditText edtFullname, edtPassword, edtPhone;
    TextView termCondition;
    CheckBox checkBox;
    CardView takePhotoKTP, takePhotoSelfie, takeKTPData, takeSelfieData;
    ImageView ktpData, selfieData;
    File file_ktp, file_selfie;
    Uri selectedImage;
    private RequestQueue requestQueue;
    String pathSelfie, pathKtp;
    ProgressDialog progressDialog;
    String fotoKtp , fotoSelfie;
    final int requestCodeKtp = 10;
    final int requestCodeSelfie = 20;
    final int openCameraKtp = 101;
    final int openGalleryKtp = 102;
    final int openCameraSelfie = 201;
    final int openGallerySelfie = 202;

    int openCamera = 0;
    int openGallery = 0;

    Bitmap bitmapKTP, bitmapSelfie;
    private String imageFile;
    boolean uploadKtp , uploadSelfie ;
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_sign_up);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        builder.detectFileUriExposure();
        requestQueue = Volley.newRequestQueue(this);

        edtFullname = findViewById(R.id.fullname);
        edtFullname.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        edtPassword = findViewById(R.id.password);
        edtPhone = findViewById(R.id.phone);
        takePhotoKTP = findViewById(R.id.takeKTP);
        takePhotoSelfie = findViewById(R.id.takeSelfie);
        takeKTPData = findViewById(R.id.takeKTPData);
        takeSelfieData = findViewById(R.id.takeSelfieData);
        ktpData = findViewById(R.id.dataKTP);
        selfieData = findViewById(R.id.dataSelfie);
        uploadKtp = false ;
        uploadSelfie = false ;

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
                String dataPassword = edtPassword.getText().toString();
                String dataPhone = edtPhone.getText().toString();

                if (dataFullname.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Nama lengkap tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPassword.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "Password tidak boleh kosong ...", R.style.toastStyleWarning).show();
                } else if (dataPhone.isEmpty()) {
                    StyleableToast.makeText(SignUp.this, "No. Telepon tidak boleh kosong ...", R.style.toastStyleWarning).show();

                } else if (!uploadKtp) {
                    StyleableToast.makeText(SignUp.this, "Mohon untuk memasukkan foto Ktp ...", R.style.toastStyleWarning).show();

                }else if (!uploadSelfie) {
                    StyleableToast.makeText(SignUp.this, "Mohon untuk memasukkan foto Selfie dengan Ktp ...", R.style.toastStyleWarning).show();

                }
                else {
                    storeFtp();
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



        takePhotoKTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takeFotoFunction(10);
                } else {
                    ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        takePhotoSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(SignUp.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takeFotoFunction(20);
                } else {
                    ActivityCompat.requestPermissions(SignUp.this, new String[]{Manifest.permission.CAMERA}, 200);
                }
            }
        });
    }

    private void takeFotoFunction(int requestCode) {

        if (requestCode == requestCodeKtp) {
            openCamera = openCameraKtp;
            openGallery = openGalleryKtp;
        } else if (requestCode == requestCodeSelfie) {
            openCamera = openCameraSelfie;
            openGallery = openGallerySelfie;
        }

        addPermission();

        final CharSequence[] options = {"Ambil Foto", "Pilih dari Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Tambahkan Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (options[which].equals("Ambil Foto")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, openCamera);
                } else if (options[which].equals("Pilih dari Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, openGallery);
                } else if (options[which].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/blits/default";    // it will return root directory of internal storage
        File root = new File(path);
        if (!root.exists()) {
            root.mkdirs();       // create folder if not exist
        }
        if (resultCode != RESULT_CANCELED) {
            File fileKtp = null;
            File fileSelfie = null;

            if (requestCode == openCameraKtp) {
                fileKtp = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : fileKtp.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        fileKtp = temp;
                        break;
                    }
                }

                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapKTP = BitmapFactory.decodeFile(fileKtp.getAbsolutePath(), bitmapOptions);
//                    bitmapKTP = getResizedBitmap(bitmapKTP, 1000);
                    ktpData.setImageBitmap(bitmapKTP);
                    takePhotoKTP.setVisibility(View.GONE);
                    takeKTPData.setVisibility(View.VISIBLE);
                    BitMapToString(bitmapKTP);
                    pathKtp = Environment.getExternalStorageDirectory() + File.separator + "blits" + File.separator + "default";
//                    file.delete();
                    OutputStream outFile = null;
                    file_ktp = new File(pathKtp, "cameraktp.jpg");
                    try {
                        outFile = new FileOutputStream(file_ktp);
                        bitmapKTP.compress(Bitmap.CompressFormat.JPEG, 20, outFile);
                        uploadKtp = true ;
//                        outFile.flush();
//                        outFile.close();
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

            } else if (requestCode == openGalleryKtp) {
                selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                pathKtp = cursor.getString(columnIndex);
                cursor.close();
                bitmapKTP = (BitmapFactory.decodeFile(pathKtp));
//                bitmapKTP = getResizedBitmap(bitmapKTP, 1000);
                file_ktp = new File(pathKtp);
                Log.w("path of", pathKtp + "");
                ktpData.setImageBitmap(bitmapKTP);
                takePhotoKTP.setVisibility(View.GONE);
                takeKTPData.setVisibility(View.VISIBLE);
                BitMapToString(bitmapKTP);
                uploadKtp = true ;
            } else if (requestCode == openCameraSelfie) {
                fileSelfie = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : fileSelfie.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        fileSelfie = temp;
                        break;
                    }
                }

                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapSelfie = BitmapFactory.decodeFile(fileSelfie.getAbsolutePath(), bitmapOptions);
//                    bitmapSelfie = getResizedBitmap(bitmapSelfie, 1000);
                    selfieData.setImageBitmap(bitmapSelfie);
                    takePhotoSelfie.setVisibility(View.GONE);
                    takeSelfieData.setVisibility(View.VISIBLE);
                    BitMapToString(bitmapSelfie);
                    pathSelfie = Environment.getExternalStorageDirectory() + File.separator + "blits" + File.separator + "default";
//                    file.delete();
                    OutputStream outFile = null;
                    file_selfie = new File(pathSelfie, "cameraselfie.jpg");

                    try {
                        outFile = new FileOutputStream(file_selfie);
                        bitmapSelfie.compress(Bitmap.CompressFormat.JPEG, 20, outFile);
                        uploadSelfie = true ;
//                        outFile.flush();
//                        outFile.close();
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

            } else if (requestCode == openGallerySelfie) {
                selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePath, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePath[0]);
                pathSelfie = cursor.getString(columnIndex);
                cursor.close();
                bitmapSelfie = (BitmapFactory.decodeFile(pathSelfie));
//                bitmapSelfie = getResizedBitmap(bitmapSelfie, 400);
                file_selfie = new File(pathSelfie);
                Log.w("path of", pathSelfie + "");
                selfieData.setImageBitmap(bitmapSelfie);
                takePhotoSelfie.setVisibility(View.GONE);
                takeSelfieData.setVisibility(View.VISIBLE);
                BitMapToString(bitmapSelfie);
                uploadSelfie = true ;
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

    private void registFunction(String dataFullname, String dataPassword, String dataPhone, String fotoKtp, String fotoSelfie) {
        HashMap<String, String> parameter = new HashMap<String, String>();

        parameter.put("fullname", dataFullname);
        parameter.put("password", dataPassword);
        parameter.put("no_telpon", dataPhone);
        parameter.put("foto_selfie", fotoSelfie);
        parameter.put("foto_ktp", fotoKtp);
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
                Log.d("errornya" , error+"");
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

    public void storeFtp() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;


        FTPClient ftpClient = new FTPClient();

        try {

            ftpClient.connect("103.167.112.188", 21);
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Connect failed");
                return;
            }

            boolean isLoggin = ftpClient.login("blits", "Bl1t5");
            showServerReply(ftpClient);

            if (!isLoggin) {
                System.out.println("Could not login to the server");
                return;
            }

            isLoggin = ftpClient.changeWorkingDirectory("/uploads");
            showServerReply(ftpClient);
            BufferedInputStream buffIn = null;
            if (isLoggin) {
                System.out.println("Successfully changed working directory.");

                buffIn = new BufferedInputStream(new FileInputStream(file_ktp));
                 fotoKtp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "_ktp" + file_ktp.getName();
                ftpClient.storeFile(fotoKtp, buffIn);

                buffIn = new BufferedInputStream(new FileInputStream(file_selfie));
                 fotoSelfie = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + "_selfie" + file_selfie.getName();
                ftpClient.storeFile(fotoSelfie, buffIn);
                registFunction(edtFullname.getText().toString(), edtPassword.getText().toString(), edtPhone.getText().toString(), fotoKtp, fotoSelfie);
            } else {
                System.out.println("Failed to change working directory. See server's reply.");
            }

            buffIn.close();
            ftpClient.logout();
            ftpClient.disconnect();

        } catch (IOException e) {
            Log.d("Erornya", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}