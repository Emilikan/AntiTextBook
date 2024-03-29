package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Objects;

import static com.example.antitextbook.Constants.a0;
import static com.example.antitextbook.Constants.a13;
import static com.example.antitextbook.MainActivity.fragmentIs;

/**
 * Класс библиотеки (какая неожиданность XD)
 * Некая прослойка. Проста обработка переходов
 */

/**
 * ░░░░░░░░░░░░░▄▄▄▄▄▄▄▄▄▄▄▄░░░░░░░░░░░░░
 * ░░░░░░░░░▄▄▀▀░░░░░░░░░░░░▀▀▄▄░░░░░░░░░
 * ░░░░░░▄█▀░░░░░░░▄▄▄▄▄▄░░░░░░░▀▄▄░░░░░░
 * ░░░░▄█▀░░░░░░░░░▀████▀░░░░░░░░░▀█▄░░░░
 * ░░░▄▀░░░░░░░░░░░░░██░░░░░░░░░░░░░▀▄░░░
 * ░░█▀░░░░░░░░░░░▄▄▄██▄▄▄░░░░░░░░░░░▀█░░
 * ░▄▀░░░░░░░░░░░░░░▀██▀░░░░░░░░░░░░░░▀▄░
 * ░█░░░░░░░░░░░░░░░▄██▄░░░░░░░░░░░░░░░█░
 * █░░░░░░░░░░░░░░░░████░░░▄▄▀▀▀▀▀█▄░░░░█
 * █░░░░░░▄█▀▀▀▀█▄▄░▀▀███▄█░░░░░░░░▀█░░░█
 * █░░░░░█▀░░░░░░░▀█▄▄░▀▀█▄░░░░░░░░░█▄░░█
 * █░░░░██░░░░░░░░▄░░▀█▄░░▀░░░░░░░░░█▀░░█
 * ░█░░░░█▄░░░░░░░▀█▄▄░▀▀█▄░░░░░░░░▄█░░█░
 * ░▀▄░░░▀▀▄░░░░░░░████▄▄░▀▀▀█▄▄▄█▀▀░░▄▀░
 * ░░█▄░░░░▀▄░░░░░░██████░░░░░░░░░░░░▄▀░░
 * ░░░▀▄░░░░░░░░▄█████████▄░░░░░░░░░▄▀░░░
 * ░░░░░█▄░░░░░▄████████████▄░░░░░▄▀░░░░░
 * ░░░░░░▀▀▄▄░░██████████████░░▄▄▀░░░░░░░
 * ░░░░░░░░░▀▀████████████████▀▀░░░░░░░░░
 * ░░░░░░░░░░░░░▀▀▀▀▀▀▀▀▀▀▀▀░░░░░░░░░░░░░
 */

public class Library extends Fragment {

    private FrameLayout frameLayout;
    private CardView download;
    private CardView choose;

    @SuppressLint("CutPasteId")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);

        frameLayout = rootView.findViewById(R.id.library);
        choose = rootView.findViewById(R.id.choose);
        download = rootView.findViewById(R.id.download);

        setTheme();

        choose = rootView.findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = DownloadFromCloud.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentIs = a13;
            }
        });

        download = rootView.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = Storage.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentIs = a13;
            }
        });
        return rootView;
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);

            choose.setBackgroundResource(R.drawable.dark_cards);
            download.setBackgroundResource(R.drawable.dark_cards);

            //chooseText.setTextColor(R.color.colorDarkBlue);
            //downloadText.setTextColor(R.color.colorDarkText);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
