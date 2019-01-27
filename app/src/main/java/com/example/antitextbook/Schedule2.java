package com.example.antitextbook;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static java.lang.String.valueOf;

public class Schedule2 extends Fragment {

    private Button changingSchedule;
    private FrameLayout frameLayout;
    private TextView pairs;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule2, container, false);

        frameLayout = rootView.findViewById(R.id.schedule2);

        changingSchedule =  rootView.findViewById(R.id.buttonChangeSchedule_couples);
        changingSchedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Нажата кнопка", Toast.LENGTH_SHORT).show();

                //сохранение расписания
                pairs = Objects.requireNonNull(getActivity()).findViewById(R.id.pairs);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Save_pairs", valueOf(pairs));
                editor.apply();
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
