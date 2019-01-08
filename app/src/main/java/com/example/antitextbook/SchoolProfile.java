package com.example.antitextbook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class SchoolProfile extends Fragment {
    private TextView schoolName;
    private TextView schoolEmail;
    private String oldEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_school_profile, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        oldEmail = preferences.getString("schoolEmail", null);
        schoolName = rootView.findViewById(R.id.NameOfSchool);
        schoolName.setText(preferences.getString("dbSchool", ""));

        schoolEmail = rootView.findViewById(R.id.EmailOfSchool);
        schoolEmail.setText(preferences.getString("schoolEmail", ""));
        // кнопка смены почты
        Button newEmailAddress = rootView.findViewById(R.id.sendEmailAgain);
        newEmailAddress.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(oldEmail != null && !oldEmail.equals("")){
                    Fragment fragment = new ChangeEmail();
                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Ошибка в email адресе")
                            .setCancelable(false)
                            .setNegativeButton("Ок, закрыть",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        // кнопка для загрузки своей книги
        Button uploadBooks = rootView.findViewById(R.id.UploadBookFromSchool);
        uploadBooks.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = UploadBookOfSchool.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        // кнопка отметиться под книгой
        Button allBooks = rootView.findViewById(R.id.AllBooksForCheck);
        allBooks.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = new Subscribe();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        // кнопка выйти
        Button singOut = rootView.findViewById(R.id.singOutFromSchoolProfile);
        singOut.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("kodOfEmail", null);
                editor.putString("dbSchool", null);
                editor.putString("schoolEmail", null);
                editor.putString("isOkOfKodFromEmail", "false");
                editor.putString("login", null);
                editor.apply();

                Fragment fragment = new MainSettings();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
