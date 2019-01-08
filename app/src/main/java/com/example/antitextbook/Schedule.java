package com.example.antitextbook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.valueOf;
import java.util.Objects;

public class Schedule extends Fragment {
    private Button changingSchedule;
    private FrameLayout frameLayout;
    private TextView lessons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        frameLayout = rootView.findViewById(R.id.schedule);

        changingSchedule =  rootView.findViewById(R.id.buttonChangeSchedule);
        changingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Нажата кнопка", Toast.LENGTH_SHORT).show();

                //сохранение расписания
                lessons = getActivity().findViewById(R.id.lessons);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Save_lessons", valueOf(lessons));
                editor.apply();

            }
        });

        return rootView;
    }
    public void changeTextView(View view){
        EditText editText = view.findViewById(R.id.lesson_mon1);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setKeyListener(null);
    }
    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);
            changingSchedule.setBackgroundResource(R.drawable.dark_cards);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
