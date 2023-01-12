package com.example.blits.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blits.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentOrderold extends Fragment {

    private TabLayout tabLayout;
//    private ViewPager viewPager;
//    public SectionsPagerAdapter mSectionsPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);

//        tabLayout = v.findViewById(R.id.tabLayout);
//        tabLayout.addTab(tabLayout.newTab().setText("Android"));
//        tabLayout.addTab(tabLayout.newTab().setText("Play"));
//        final ViewPager viewPager = v.findViewById(R.id.viewPager);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        //mRecyclerView.setLayoutManager(mLayoutManager);
//        viewPager.setAdapter(new PagerAdapter(getActivity().getSupportFragmentManager(), tabLayout.getTabCount()));
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        tabLayout.setSelected(true);
//        viewPager.setCurrentItem(0);
//        Log.d("tab" , ""+ tabLayout.getTabCount());
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        return v;

//        AdapterTabPesanan adapterTabPesanan = new AdapterTabPesanan(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
//        adapterTabPesanan.addFragment(new FragmentProgress(), "PESANAN");
//        adapterTabPesanan.addFragment(new FragmentSuccess(), "RIWAYAT");
//        viewPager.setAdapter(adapterTabPesanan);

//        return v;
    }

//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm, int tabCount) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // returning current tabs using switch case
//            switch (position){
//                case 0:
//                    FragmentProgress tab0 = new FragmentProgress();
//                    return tab0;
//                case 1:
//                    FragmentSuccess tab1 = new FragmentSuccess();
//                    return tab1;
//
//                default:
//                    return null;
//            }
//        }
//        @Override
//        public int getCount() {
//            // Show 7 total pages.
//            return 1;
//        }
//    }


}
