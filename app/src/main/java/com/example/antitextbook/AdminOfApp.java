package com.example.antitextbook;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static com.example.antitextbook.Constants.*;
import static com.example.antitextbook.MainActivity.fragmentIs;


public class AdminOfApp extends Fragment {
    /**
     * Класс именно админки для разработчиков. Является некоторой прослойкой. (Класс входа называется Server)
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_of_app, container, false);
        fragmentIs = a0;
        // кнопка выхода
        Button singOut = rootView.findViewById(R.id.singOutOfAdmin);
        singOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("saveMeAdmin", "false");
                editor.apply();

                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = MainSettings.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentIs = a0;
            }
        });

        CardView cloud = rootView.findViewById(R.id.buttonToCloud); // переход на загрузку книги
        cloud.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = new Cloud();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentIs = a3;
            }
        });

        CardView admin = rootView.findViewById(R.id.buttonToAdmin); // переход на админку
        admin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChooseTrueBooks();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fragmentIs = a3;
            }
        });
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
