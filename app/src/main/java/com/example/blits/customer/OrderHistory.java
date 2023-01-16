package com.example.blits.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.blits.R;

public class OrderHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}