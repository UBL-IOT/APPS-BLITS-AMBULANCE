package com.example.blits.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.blits.R;
import com.example.blits.access.SignIn;
import com.example.blits.service.App;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetDialogs {

    public interface onDialogClosed{
        void onClosed(String string);
    }



    public static void commonWarning(Activity context, String title, String content, boolean close) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialog.setCancelable(true);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            if(close)
                context.finish();
        });
        dialog.show();

        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);

        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void locationDisabledWarning(Activity context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("Lokasi Tidak aktif");
        dialog.setContentText("Lokasi Anda tidak aktif, aktifkan lokasi terlebih dahulu untuk melanjutkan");
        dialog.setConfirmText("Settings");
        dialog.setCustomImage(CustomDrawable.googleMaterialDrawable(
                context, R.color.colorPrimaryDark, 36,
                GoogleMaterial.Icon.gmd_gps_off
        ));
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
            context.finish();
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void commonError(Activity context, String content, boolean close) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("Gagal memuat permintaan");
        dialog.setContentText(content);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            if(close)
                context.finish();
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void endpointError(Activity context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("Oops!");
        dialog.setContentText("Internet tidak stabil atau server mengalami gangguan, silahkan coba lagi");
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
//            context.finishAffinity();
        });
        try {
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }
    public static void commonError(Activity context, String title, String content, onDialogClosed listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            listener.onClosed("closed");
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void commonLogout(Activity context, String title, String content, onDialogClosed listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            listener.onClosed("closed");
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void commonInvalidToken(Activity context, String title, String content) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText(title);
        dialog.setContentText(content);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            App.getPref().clear();
            context.startActivity(new Intent(context, SignIn.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            context.finish();
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void commonSuccess(Activity context, String body, boolean close) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("Berhasil Memuat permintaan");
        dialog.setContentText(body);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            if(close)
                sweetAlertDialog.dismissWithAnimation();
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void commonSuccessWithIntent(Activity context, String body, onDialogClosed listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText("Berhasil Memuat permintaan");
        dialog.setContentText(body);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
                sweetAlertDialog.dismissWithAnimation();
                listener.onClosed("Sukses");
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void commonWarningWithIntent(Activity context,String title, String body, onDialogClosed listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialog.setCancelable(false);
        dialog.setTitleText(title);
        dialog.setContentText(body);
        dialog.setConfirmText("OK");
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            listener.onClosed("Sukses");
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
    }

    public static void Loading(Activity context,SweetAlertDialog pDialog, String body , int code) {
//        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(body);
        pDialog.setCancelable(false);
        pDialog.show();
        if(code == 1)
            pDialog.show();
        else
            pDialog.dismiss();
    }

    public static void confirmDialog(Activity context, String Title , String body, String suksesBody, onDialogClosed listener) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(Title);
        dialog.setContentText(body);
        dialog.setCancelText("Tidak");
        dialog.setConfirmText("Ya");
        dialog.showCancelButton(true);
        dialog.setCancelable(false);
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            listener.onClosed(suksesBody);
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        Button btnCc = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.cancel_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
        btnCc.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnCc.setScaleX((float) 1);
        btnCc.setScaleY((float) 0.8);
        btnCc.setTextSize((float) 16.5);
    }

    public static void validasiRekening(Activity context, String Title , String body, String suksesBody, onDialogClosed listener , onDialogClosed editRekening) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(Title);
        dialog.setContentText(body);
        dialog.setCancelText("Ubah");
        dialog.setConfirmText("Lanjutkan");
        dialog.showCancelButton(true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            listener.onClosed(suksesBody);
        });
        dialog.setCancelClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            editRekening.onClosed("");
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        Button btnCc = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.cancel_button);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
        btnCc.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnCc.setScaleX((float) 1);
        btnCc.setScaleY((float) 0.8);
        btnCc.setTextSize((float) 16.5);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void validasiKur(Activity context, String Title , String body, String suksesBody, onDialogClosed listener , onDialogClosed editRekening) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText(Title);
        dialog.setContentText(body);
        dialog.setCancelText("Tidak");
        dialog.setConfirmText("Iya");
        dialog.showCancelButton(true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setConfirmClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            listener.onClosed(suksesBody);
        });
        dialog.setCancelClickListener(sweetAlertDialog -> {
            sweetAlertDialog.dismissWithAnimation();
            editRekening.onClosed("");
        });
        dialog.show();
        Button btnC = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.confirm_button);
        Button btnCc = (Button) dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.cancel_button);
        TextView text = dialog.findViewById(com.ontbee.legacyforks.cn.pedant.SweetAlert.R.id.title_text);
        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        text.setSingleLine(false);
        text.setLines(2);
        btnC.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnC.setScaleX((float) 1);
        btnC.setScaleY((float) 0.8);
        btnC.setTextSize((float) 16.5);
        btnCc.setBackground(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dialogbtn));
        btnCc.setScaleX((float) 1);
        btnCc.setScaleY((float) 0.8);
        btnCc.setTextSize((float) 16.5);
    }
}
