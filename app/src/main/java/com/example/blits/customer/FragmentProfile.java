package com.example.blits.customer;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.blits.R;
import com.example.blits.access.SignIn;
import com.example.blits.model.ModelUser;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;
import com.example.blits.service.Utils;

public class FragmentProfile extends Fragment {

    LinearLayout btnSignOut;
    Dialog dialogSignOut;
    TextView fullnameData, emailData, addressData, phoneData, createdData;

    private RequestQueue requestQueue;
    ModelUser modelUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        requestQueue = Volley.newRequestQueue(getActivity());
        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());

        fullnameData = v.findViewById(R.id.fullname);
        emailData = v.findViewById(R.id.email);
        addressData = v.findViewById(R.id.address);
        phoneData = v.findViewById(R.id.phone);
        createdData = v.findViewById(R.id.created);

        fullnameData.setText(modelUser.getFullname());
        emailData.setText(modelUser.getEmail());
        addressData.setText(modelUser.getAlamat());
        phoneData.setText(modelUser.getNo_telpon());
        createdData.setText(Utils.convertMongoDateWithoutTime(modelUser.getCreated_at()));

        dialogSignOut = new Dialog(getActivity(), R.style.dialogStyle);
        dialogSignOut.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSignOut.setCancelable(false);

        btnSignOut = v.findViewById(R.id.signOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.getPref().clear();
                startActivity(new Intent(getActivity(), SignIn.class));
            }
        });

        return v;
    }
}