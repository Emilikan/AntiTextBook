package com.example.antitextbook;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Regestration extends Fragment {

    private String userName;
    private String userSchool;
    private String userCass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_regestration, container, false);

        userName = ((EditText) rootView.findViewById(R.id.userName)).getText().toString();
        userSchool = ((EditText) rootView.findViewById(R.id.userSchool)).getText().toString();
        userCass = ((EditText) rootView.findViewById(R.id.userClass)).getText().toString();

        Button save = rootView.findViewById(R.id.saveInfoAboutUser);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // сохранение данных в SharedPreference
            }
        });

        return rootView;
    }

}
