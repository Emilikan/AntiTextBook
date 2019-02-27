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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import static java.lang.String.valueOf;

public class Schedule2 extends Fragment {

    private Button changingSchedule;
    private FrameLayout frameLayout;
    private EditText couples_mon1,couples_mon2,couples_mon3,couples_mon4,couples_th1,couples_th2,couples_th3,couples_th4,couples_wed1,couples_wed2,couples_wed3,couples_wed4,couples_thur1,couples_thur2,couples_thur3,couples_thur4,couples_fri1,couples_fri2,couples_fri3,couples_fri4,couples_sat1,couples_sat2,couples_sat3,couples_sat4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String couples_mon1 = preferences.getString("couples_mon1", "0");
        String couples_mon2 = preferences.getString("couples_mon2", "0");
        String couples_mon3 = preferences.getString("couples_mon3", "0");
        String couples_mon4 = preferences.getString("couples_mon4", "0");

        String couples_th1 = preferences.getString("couples_th1", "0");
        String couples_th2 = preferences.getString("couples_th2", "0");
        String couples_th3 = preferences.getString("couples_th3", "0");
        String couples_th4 = preferences.getString("couples_th4", "0");

        String couples_wed1 = preferences.getString("couples_wed1", "0");
        String couples_wed2 = preferences.getString("couples_wed2", "0");
        String couples_wed3 = preferences.getString("couples_wed3", "0");
        String couples_wed4 = preferences.getString("couples_wed4", "0");

        String couples_thur1 = preferences.getString("couples_thur1", "0");
        String couples_thur2 = preferences.getString("couples_thur2", "0");
        String couples_thur3 = preferences.getString("couples_thur3", "0");
        String couples_thur4 = preferences.getString("couples_thur4", "0");

        String couples_fri1 = preferences.getString("couples_fri1", "0");
        String couples_fri2 = preferences.getString("couples_fri2", "0");
        String couples_fri3 = preferences.getString("couples_fri3", "0");
        String couples_fri4 = preferences.getString("couples_fri4", "0");

        String couples_sat1 = preferences.getString("couples_sat1", "0");
        String couples_sat2 = preferences.getString("couples_sat2", "0");
        String couples_sat3 = preferences.getString("couples_sat3", "0");
        String couples_sat4 = preferences.getString("couples_sat4", "0");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule2, container, false);

        frameLayout = rootView.findViewById(R.id.schedule2);

        couples_mon1.setText(R.string.couples_mon1);
        couples_mon2.setText(R.string.couples_mon2);
        couples_mon3.setText(R.string.couples_mon3);
        couples_mon4.setText(R.string.couples_mon4);

        couples_th1.setText(R.string.couples_th1);
        couples_th2.setText(R.string.couples_th2);
        couples_th3.setText(R.string.couples_th3);
        couples_th4.setText(R.string.couples_th4);

        couples_wed1.setText(R.string.couples_wed1);
        couples_wed2.setText(R.string.couples_wed2);
        couples_wed3.setText(R.string.couples_wed3);
        couples_wed4.setText(R.string.couples_wed4);

        couples_thur1.setText(R.string.couples_thur1);
        couples_thur2.setText(R.string.couples_thur2);
        couples_thur3.setText(R.string.couples_thur3);
        couples_thur4.setText(R.string.couples_thur4);

        couples_fri1.setText(R.string.couples_fri1);
        couples_fri2.setText(R.string.couples_fri2);
        couples_fri3.setText(R.string.couples_fri3);
        couples_fri4.setText(R.string.couples_fri4);

        couples_sat1.setText(R.string.couples_sat1);
        couples_sat2.setText(R.string.couples_sat2);
        couples_sat3.setText(R.string.couples_sat3);
        couples_sat4.setText(R.string.couples_sat4);

        changingSchedule =  rootView.findViewById(R.id.buttonChangeSchedule_couples);
        changingSchedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Нажата кнопка", Toast.LENGTH_SHORT).show();

                //сохранение расписания
                couples_mon1 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_mon1);
                couples_mon2 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_mon2);
                couples_mon3 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_mon3);
                couples_mon4 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_mon4);

                couples_th1 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_th1);
                couples_th2 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_th2);
                couples_th3 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_th3);
                couples_th4 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_th4);

                couples_wed1 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_wed1);
                couples_wed2 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_wed2);
                couples_wed3 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_wed3);
                couples_wed4 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_wed4);

                couples_thur1 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_thur1);
                couples_thur2 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_thur2);
                couples_thur3 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_thur3);
                couples_thur4 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_thur4);

                couples_fri1 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_fri1);
                couples_fri2 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_fri2);
                couples_fri3 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_fri3);
                couples_fri4 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_fri4);

                couples_sat1 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_sat1);
                couples_sat2 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_sat2);
                couples_sat3 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_sat3);
                couples_sat4 = Objects.requireNonNull(getActivity()).findViewById(R.id.couples_sat4);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("couples_mon1", valueOf(couples_mon1));
                editor.putString("couples_mon2", valueOf(couples_mon2));
                editor.putString("couples_mon3", valueOf(couples_mon3));
                editor.putString("couples_mon4", valueOf(couples_mon4));

                editor.putString("couples_th1", valueOf(couples_th1));
                editor.putString("couples_th2", valueOf(couples_th2));
                editor.putString("couples_th3", valueOf(couples_th3));
                editor.putString("couples_th4", valueOf(couples_th4));

                editor.putString("couples_wed1", valueOf(couples_wed1));
                editor.putString("couples_wed2", valueOf(couples_wed2));
                editor.putString("couples_wed3", valueOf(couples_wed3));
                editor.putString("couples_wed4", valueOf(couples_wed4));

                editor.putString("couples_thur1", valueOf(couples_thur1));
                editor.putString("couples_thur2", valueOf(couples_thur2));
                editor.putString("couples_thur3", valueOf(couples_thur3));
                editor.putString("couples_thur4", valueOf(couples_thur4));

                editor.putString("couples_fri1", valueOf(couples_fri1));
                editor.putString("couples_fri2", valueOf(couples_fri2));
                editor.putString("couples_fri3", valueOf(couples_fri3));
                editor.putString("couples_fri4", valueOf(couples_fri4));

                editor.putString("couples_sat1", valueOf(couples_sat1));
                editor.putString("couples_sat2", valueOf(couples_sat2));
                editor.putString("couples_sat3", valueOf(couples_sat3));
                editor.putString("couples_sat4", valueOf(couples_sat4));

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
