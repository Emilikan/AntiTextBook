package com.example.antitextbook;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для перелистывания между входом как админ школы и как админ приложения
 */

/**
 * ___________ooo__________________________________________oooooooooo
 * ________ooooooooo_____________________________________oooooooooooooooo
 * ______ooooooooooooo_________________________________oooooooooooooooooooo
 * ____ooooooooooooooooo_______________oooooooo_____ooooooooooooooooooooooooo
 * ___ooooooo_____oooooooo________oooooooooooooooooooooooooo_______oooooooooooo
 * ___¦oooo______oo_ooooooooooooooooooooooooooooooooooooooo_____ooooooooooooooo¦
 * __¦ooooooo_ooo_____ooooooooooooooooooooooooooooooooooo¦_____oooooooooooooooooo
 * _oooooooooo_____oo__oooooooooooooooooooooooooooooooooo___o_oooooooooooooooooo¦
 * _¦ooooooooo__ooo______ooooooooooooooooooooooooooooooo¦_____ooooooooooooooooooo
 * _¦ooooooooooo_____oo____ooooooooooooooooooooooooooooo¦_o_o___oooooooooooooooooo
 * _oooooooooo____ooo___o___¦oooooooooooooooooooooooooooo_____ooooooooooooooooooo¦
 * _¦ooooooooooooo___ooo____oooooooooooooooooooooooooooooo_o__ooooooooooooooooooo¦
 * _ooooooooooooooooooo____¦oooooooooooooooooooooooooooooo¦______oooooooooooooooo¦
 * __¦oooooooooooooooo__oo_ooo¦_oooooooooooooooooooooooooooo___ooooo_____o____ooo¦
 * __oooooooooooooooo______¦oo_ooooooooooooooooooooooo_¦_ooo¦___ooo_ooooo______¦o¦
 * ___¦ooooooooooooo___oo___ooooooooooooooooooooooooooo_ooooo¦_o__oooo______o___o¦
 * ___oooo_o__oooo___ooo______ooooooooooooooooooooooooooooooo¦_______o____o____¦o
 * ____oooo__o_o____oo_____o___¦ooooooooooooooooooooooooooooo____o____ooooo___ooo
 * _____oooooo____oo____________oo_ooooooooooooo¦ooooooooooo¦_o______o___ooooooo¦
 * ______ooo___o_oo_____o____o__ooooooooooooo_o¦_o_ooooooooo____ooooo_____o___oo¦
 * _______oo___oo_____o____o___¦ooooooooooooooo¦_oo_oooooo¦_______oooooo_____¦oo
 * ________ooooo____oo__________¦oooooooooooooo¦¦ooo_oooooo_o_o_______ooooo_ooo
 * _________oo____oo____¦_____oo¦o_oooooooooooo¦¦ooo¦ooooo_____oooo_______oooo
 * __________oo_ooo____o_____¦oo¦oooooooooooo_o¦ooooo_________o__oooooo_oooo
 * ___________ooo_____¦¦_____oo¦¦oooooooooooooo_ooooo¦_o__o_o_______ooooooo_o
 * ____________oo____o____o_¦oo¦¦o_oooooooooooo_ooooo¦_______ooo__o__oooo_oooo
 * _____________ooo_¦¦______¦oo¦¦oooooooooooooo_¦ooooo__o______ooo__oooo_ooooo¦
 * ______________oooo___o___ooo¦¦ooooooooooo_o¦__ooooo¦__ooo____oooooo_ooooooo¦
 * ________________ooo_____¦ooo_¦ooooooooooooo¦__¦ooooo____ooo__oooo_ooooooooo¦
 * _________________oooo___¦ooo__o_ooooooooooo¦___ooooo__o___oooooo_¦ooooooooo¦
 * ____________________oo___ooo¦_ooooooooooooo_o_____ooo__o____oooo_¦o_ooooooo¦
 * _______________________oooooo_oooooooooo_o¦__o___ooooo_____oooo¦_oo¦ooooooo¦
 * _______________________oooo¦__oooooooooooo¦______¦oooo¦___¦_ooo¦¦o¦¦ooooooo¦
 * ______________________¦ooo¦___o__ooooooooo¦___o___ooooo___ooooo¦¦o¦¦oooooooo
 * ______________________oooo___¦¦ooooooooooo________¦oooo__oooooo_oo¦ooooooo¦
 * ______________________ooo¦___o¦oooooooooo¦____o____oooo__oooooo¦o¦_ooo¦oooo
 * _____________________¦ooo¦__¦o¦oooooooooo¦__o___o__¦oo¦_¦__ooo¦_¦_¦oo¦¦ooo
 * _____________________¦ooo___oo¦¦o_ooooo_o¦_________ooo__¦ooooo¦__¦ooo_oo¦
 * ______________________oo¦___oo¦¦ooooooooo________oooo¦_ooooooo__¦oooo¦oo
 * ______________________¦o¦__¦oo¦¦ooooooooo____oooooooo_¦__ooooo_¦oooo_¦¦
 * _____________o_________o¦__¦ooo_oooooooo¦______ooo__ooooooooo¦_oooo_¦¦
 * ____________ooo_________o__¦ooo_oooooooo¦__o_____oooooooooooo_¦ooo__¦oo¦
 * __________ooo_oooo______¦__¦ooo¦oooooooo________¦ooooooooooo___o__ooooo
 * ________ooo__o__ooo________¦ooo¦¦o_ooooo________¦__oooooooo¦__ooooooooo
 * _______oo__ooooo__o________¦ooo¦¦oooooo¦________oooooooooooo__oooooooo¦_______oo
 * ______oo_oooooooo¦_o________ooo¦¦oooo_o¦___o____oooooooooo___¦oo_ooooo______ooo
 * ____ooo_ooooooooo__oo__oo___oooo_oooooo¦_______¦__ooooooo¦___oo¦¦oooo¦___ooooo
 * __ooo_oooooooooo__o_oooo____¦ooo¦oooooo¦_______¦oooooooooo__¦oo_ooooo__oooo
 * _ooo_oooooooooo____oooooooo_¦ooo¦¦oooooo_______ooooooooo¦___oo_¦oooo¦_oooooooooo
 * oo__oooooooooo_ooo__ooooooo¦¦ooo¦_oooooo¦___o__ooooooooo¦_oo_ooooooooooooooooooo
 * o_ooooooooooo_¦ooooo_ooooooo_oooo_¦oooooo______¦oooooooo¦__ooooo oooooooooooooo
 * _oooooooooo¦__oooooooooooooo¦ooooo__oooooo_ooooooooooooooooooooooooooooooooooo
 * ¦oooooooooo__¦oooooooooooooooooooooo_oooooo_ooooooooooooooooooooooooooooooooo_oo
 * ¦ooooooooo___oooooooooooooooooooooooooo_oo_ooooooooooooooooooooooooooooooooo_ooo
 *
 *
 * МОЯ ОБОРОНА
 */

public class MainSettings extends Fragment {
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmentTitles = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        void addFragment(Fragment fragment, String name) {
            fragmentList.add(fragment);
            fragmentTitles.add(name);
        }
    }



    ViewPager viewPager;
    TabLayout tabLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_settings, container, false);
        viewPager = rootView.findViewById(R.id.viewpager);
        tabLayout = rootView.findViewById(R.id.tabs);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        viewPagerAdapter.addFragment(new AdminForSchool(), "Для школы");
        viewPagerAdapter.addFragment(new Server(), "Админка");

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
