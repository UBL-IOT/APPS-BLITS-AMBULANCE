package com.example.blits.assets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.blits.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoard extends AppCompatActivity {

    private ViewPager screenPager;

    IntroAdapter introAdapter;

    TabLayout tabIndicator;
    Button btnNext;
    Button btnGetStarted;
    Animation btnAnim;
    TextView tvSkip;

    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (restorePrefData()) {
            startActivity(new Intent(OnBoard.this, Splash.class));
        }

        setContentView(R.layout.activity_on_board);

        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        tvSkip = findViewById(R.id.tv_skip);
        screenPager = findViewById(R.id.screen_viewpager);

        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        final List<IntroModel> mList = new ArrayList<>();
        mList.add(new IntroModel("BLITS AMBULANS", "Layanan masyarakat kota Bandar Lampung, sebagai sarana pemesanan ambulans milik daerah secara digital.", R.drawable.icon_text_light));
        mList.add(new IntroModel("KELOLA DATA AMBULANS", "Kami menyediakan layanan untuk mengelola armada ambulans dengan status yang dimiliki kendaraan untuk beroperasi.", R.drawable.kelola_ambulans));
        mList.add(new IntroModel("KELOLA DATA PENGEMUDI", "Layanan kami memiliki fitur yang dapat digunakan untuk mendata pengemudi yang dimiliki daerah untuk beroperasi.", R.drawable.kelola_driver));
        mList.add(new IntroModel("LAKUKAN PEMESANAN", "Lakukan pemesanan melalui mobile aplikasi yang terintegrasi, anda dapat mengkases layanan kami kapanpun dan dimanapun.", R.drawable.lakukan_pemesanan));

        introAdapter = new IntroAdapter(this, mList);
        screenPager.setAdapter(introAdapter);

        tabIndicator.setupWithViewPager(screenPager);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size() - 1) {
                    loaddLastScreen();
                }
            }
        });

        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnBoard.this, Splash.class));
                savePrefsData();
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }

    private void loaddLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        btnGetStarted.setAnimation(btnAnim);
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend", true);
        editor.commit();
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnend", false);
        return isIntroActivityOpnendBefore;
    }

}