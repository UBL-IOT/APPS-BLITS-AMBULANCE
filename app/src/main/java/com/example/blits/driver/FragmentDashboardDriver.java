package com.example.blits.driver;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blits.R;
import com.example.blits.model.ModelUser;
import com.example.blits.service.App;
import com.example.blits.service.GsonHelper;
import com.example.blits.service.Prefs;

public class FragmentDashboardDriver extends Fragment {

    TextView fullnameData;

    ModelUser modelUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard_driver, container, false);

        modelUser = (ModelUser) GsonHelper.parseGson(App.getPref().getString(Prefs.PREF_STORE_PROFILE, ""), new ModelUser());
        fullnameData = v.findViewById(R.id.fullname);
        fullnameData.setText(modelUser.getFullname());

        return v;
    }
}