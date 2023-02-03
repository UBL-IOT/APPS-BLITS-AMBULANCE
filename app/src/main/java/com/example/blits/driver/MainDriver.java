package com.example.blits.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import com.example.blits.R;
import com.example.blits.customer.MainCustomer;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class MainDriver extends AppCompatActivity {

    private static final String TAG = MainCustomer.class.getSimpleName();
    boolean backPress = false;

    Fragment fragment;
    FragmentManager fragmentManager;
    BottomAppBar bottomAppBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver);

        bottomAppBars = findViewById(R.id.bottomAppBar);

        loadFragment(new FragmentDashboardDriver());
        bottomAppBars.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragment = null;
                switch (item.getItemId()) {
                    case R.id.dashboard_driver:
                        fragment = new FragmentDashboardDriver();
                        break;
                    case R.id.order_driver:
                        fragment = new FragmentOrder();
                        break;
                    case R.id.me_driver:
                        fragment = new FragmentProfileDriver();
                        break;
                }

                if (fragment != null) {
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                } else {
                    Log.e(TAG, "Error Creating Fragment");
                }

                return false;
            }
        });
    }

    protected void onResume() {
        super.onResume();
//        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager f = getSupportFragmentManager();
        FragmentTransaction t = f.beginTransaction();
        t.replace(R.id.fragment_container, fragment).commit();
        t.addToBackStack(null);
    }

    @Override
    public void onBackPressed() {
        if (backPress) {
            super.onBackPressed();
            return;
        }

        this.backPress = true;
        StyleableToast.makeText(MainDriver.this, "Tekan sekali lagi untuk keluar ...", R.style.toastStyleWarning).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPress = false;
            }
        }, 2000);
    }
}