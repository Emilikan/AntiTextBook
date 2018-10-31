package com.example.antitextbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Settings extends Fragment implements View.OnClickListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button button1 = (Button) rootView.findViewById(R.id.infoApp);

        button1.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        // написать открытие фрагмента fragment_info_about_app
    }

}
