package com.example.blits.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.blits.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class MainCustomer extends AppCompatActivity {

    private static final String TAG = MainCustomer.class.getSimpleName();
    boolean backPress = false;

    Fragment fragment;
    FragmentManager fragmentManager;
    BottomAppBar bottomAppBars;
    FloatingActionButton btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        bottomAppBars = findViewById(R.id.bottomAppBar);
        btnOrder = findViewById(R.id.order);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainCustomer.this, Order.class));
            }
        });

        loadFragment(new FragmentDashboard());
        bottomAppBars.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragment = null;
                switch (item.getItemId()) {
                    case R.id.dashboard_customer:
                        fragment = new FragmentDashboard();
                        break;
                    case R.id.order_customer:
                        fragment = new FragmentOrder();
                        break;
                    case R.id.me_customer:
                        fragment = new FragmentProfile();
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
        StyleableToast.makeText(MainCustomer.this, "Tekan sekali lagi untuk keluar ...", R.style.toastStyleWarning).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPress = false;
            }
        }, 2000);
    }
}