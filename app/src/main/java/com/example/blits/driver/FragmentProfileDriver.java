package com.example.blits.driver;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.blits.R;
import com.example.blits.access.SignIn;
import com.example.blits.service.App;

public class FragmentProfileDriver extends Fragment {

    LinearLayout btnSignOut;
    Dialog dialogSignOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_driver, container, false);

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