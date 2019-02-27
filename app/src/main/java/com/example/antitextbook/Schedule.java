package com.example.antitextbook;

import android.content.SharedPreferences;
import android.graphics.Color;
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

public class qSchedule extends Fragment {
    private Button changingSchedule;
    private FrameLayout frameLayout;
    private EditText lesson_mon1,lesson_mon2,lesson_mon3,lesson_mon4,lesson_mon5,lesson_mon6,lesson_mon7, lesson_th1,lesson_th2,lesson_th3,lesson_th4,lesson_th5,lesson_th6,lesson_th7,lesson_wed1,lesson_wed2,lesson_wed3,lesson_wed4,lesson_wed5,lesson_wed6,lesson_wed7,lesson_thur1,lesson_thur2,lesson_thur3,lesson_thur4,lesson_thur5,lesson_thur6,lesson_thur7,lesson_fri1,lesson_fri2,lesson_fri3,lesson_fri4,lesson_fri5,lesson_fri6,lesson_fri7,lesson_sat1,lesson_sat2,lesson_sat3,lesson_sat4,lesson_sat5,lesson_sat6,lesson_sat7;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String lesson_mon1 = preferences.getString("lesson_mon1", "0");
        String lesson_mon2 = preferences.getString("lesson_mon2", "0");
        String lesson_mon3 = preferences.getString("lesson_mon3", "0");
        String lesson_mon4 = preferences.getString("lesson_mon4", "0");
        String lesson_mon5 = preferences.getString("lesson_mon5", "0");
        String lesson_mon6 = preferences.getString("lesson_mon6", "0");
        String lesson_mon7 = preferences.getString("lesson_mon7", "0");

        String lesson_th1 = preferences.getString("lesson_th1", "0");
        String lesson_th2 = preferences.getString("lesson_th2", "0");
        String lesson_th3 = preferences.getString("lesson_th3", "0");
        String lesson_th4 = preferences.getString("lesson_th4", "0");
        String lesson_th5 = preferences.getString("lesson_th5", "0");
        String lesson_th6 = preferences.getString("lesson_th6", "0");
        String lesson_th7 = preferences.getString("lesson_th7", "0");

        String lesson_wed1 = preferences.getString("lesson_wed1", "0");
        String lesson_wed2 = preferences.getString("lesson_wed2", "0");
        String lesson_wed3 = preferences.getString("lesson_wed3", "0");
        String lesson_wed4 = preferences.getString("lesson_wed4", "0");
        String lesson_wed5 = preferences.getString("lesson_wed5", "0");
        String lesson_wed6 = preferences.getString("lesson_wed6", "0");
        String lesson_wed7 = preferences.getString("lesson_wed7", "0");

        String lesson_thur1 = preferences.getString("lesson_thur1", "0");
        String lesson_thur2 = preferences.getString("lesson_thur2", "0");
        String lesson_thur3 = preferences.getString("lesson_thur3", "0");
        String lesson_thur4 = preferences.getString("lesson_thur4", "0");
        String lesson_thur5 = preferences.getString("lesson_thur5", "0");
        String lesson_thur6 = preferences.getString("lesson_thur6", "0");
        String lesson_thur7 = preferences.getString("lesson_thur7", "0");

        String lesson_fri1 = preferences.getString("lesson_fri1", "0");
        String lesson_fri2 = preferences.getString("lesson_fri2", "0");
        String lesson_fri3 = preferences.getString("lesson_fri3", "0");
        String lesson_fri4 = preferences.getString("lesson_fri4", "0");
        String lesson_fri5 = preferences.getString("lesson_fri5", "0");
        String lesson_fri6 = preferences.getString("lesson_fri6", "0");
        String lesson_fri7 = preferences.getString("lesson_fri7", "0");

        String lesson_sat1 = preferences.getString("lesson_sat1", "0");
        String lesson_sat2 = preferences.getString("lesson_sat2", "0");
        String lesson_sat3 = preferences.getString("lesson_sat3", "0");
        String lesson_sat4 = preferences.getString("lesson_sat4", "0");
        String lesson_sat5 = preferences.getString("lesson_sat5", "0");
        String lesson_sat6 = preferences.getString("lesson_sat6", "0");
        String lesson_sat7 = preferences.getString("lesson_sat7", "0");


    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        frameLayout = rootView.findViewById(R.id.schedule);


        lesson_mon1.setText(R.string.lesson_mon1);
        lesson_mon2.setText(R.string.lesson_mon2);
        lesson_mon3.setText(R.string.lesson_mon3);
        lesson_mon4.setText(R.string.lesson_mon4);
        lesson_mon5.setText(R.string.lesson_mon5);
        lesson_mon6.setText(R.string.lesson_mon6);
        lesson_mon7.setText(R.string.lesson_mon7);

        lesson_th1.setText(R.string.lesson_th1);
        lesson_th2.setText(R.string.lesson_th2);
        lesson_th3.setText(R.string.lesson_th3);
        lesson_th4.setText(R.string.lesson_th4);
        lesson_th5.setText(R.string.lesson_th5);
        lesson_th6.setText(R.string.lesson_th6);
        lesson_th7.setText(R.string.lesson_th7);

        lesson_wed1.setText(R.string.lesson_wed1);
        lesson_wed2.setText(R.string.lesson_wed2);
        lesson_wed3.setText(R.string.lesson_wed3);
        lesson_wed4.setText(R.string.lesson_wed4);
        lesson_wed5.setText(R.string.lesson_wed5);
        lesson_wed6.setText(R.string.lesson_wed6);
        lesson_wed7.setText(R.string.lesson_wed7);

        lesson_thur1.setText(R.string.lesson_thur1);
        lesson_thur2.setText(R.string.lesson_thur2);
        lesson_thur3.setText(R.string.lesson_thur3);
        lesson_thur4.setText(R.string.lesson_thur4);
        lesson_thur5.setText(R.string.lesson_thur5);
        lesson_thur6.setText(R.string.lesson_thur6);
        lesson_thur7.setText(R.string.lesson_thur7);

        lesson_fri1.setText(R.string.lesson_fri1);
        lesson_fri2.setText(R.string.lesson_fri2);
        lesson_fri3.setText(R.string.lesson_fri3);
        lesson_fri4.setText(R.string.lesson_fri4);
        lesson_fri5.setText(R.string.lesson_fri5);
        lesson_fri6.setText(R.string.lesson_fri6);
        lesson_fri7.setText(R.string.lesson_fri7);

        lesson_sat1.setText(R.string.lesson_sat1);
        lesson_sat2.setText(R.string.lesson_sat2);
        lesson_sat3.setText(R.string.lesson_sat3);
        lesson_sat4.setText(R.string.lesson_sat4);
        lesson_sat5.setText(R.string.lesson_sat5);
        lesson_sat6.setText(R.string.lesson_sat6);
        lesson_sat7.setText(R.string.lesson_sat7);

        changingSchedule =  rootView.findViewById(R.id.buttonChangeSchedule);
        changingSchedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Нажата кнопка", Toast.LENGTH_SHORT).show();

                //сохранение расписания
                lesson_mon1 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon1);
                lesson_mon2 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon2);
                lesson_mon3 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon3);
                lesson_mon4 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon4);
                lesson_mon5 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon5);
                lesson_mon6 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon6);
                lesson_mon7 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_mon7);

                lesson_th1 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th1);
                lesson_th2 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th2);
                lesson_th3 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th3);
                lesson_th4 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th4);
                lesson_th5 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th5);
                lesson_th6 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th6);
                lesson_th7 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_th7);

                lesson_wed1 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed1);
                lesson_wed2 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed2);
                lesson_wed3 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed3);
                lesson_wed4 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed4);
                lesson_wed5 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed5);
                lesson_wed6 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed6);
                lesson_wed7 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_wed7);

                lesson_thur1 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur1);
                lesson_thur2 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur2);
                lesson_thur3 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur3);
                lesson_thur4 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur4);
                lesson_thur5 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur5);
                lesson_thur6 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur6);
                lesson_thur7 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_thur7);

                lesson_fri1 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri1);
                lesson_fri2 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri2);
                lesson_fri3 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri3);
                lesson_fri4 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri4);
                lesson_fri5 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri5);
                lesson_fri6 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri6);
                lesson_fri7 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_fri7);

                lesson_sat1 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat1);
                lesson_sat2 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat2);
                lesson_sat3 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat3);
                lesson_sat4 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat4);
                lesson_sat5 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat5);
                lesson_sat6 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat6);
                lesson_sat7 = Objects.requireNonNull(getActivity()).findViewById(R.id.lesson_sat7);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lesson_mon1", valueOf(lesson_mon1));
                editor.putString("lesson_mon2", valueOf(lesson_mon2));
                editor.putString("lesson_mon3", valueOf(lesson_mon3));
                editor.putString("lesson_mon4", valueOf(lesson_mon4));
                editor.putString("lesson_mon5", valueOf(lesson_mon5));
                editor.putString("lesson_mon6", valueOf(lesson_mon6));
                editor.putString("lesson_mon7", valueOf(lesson_mon7));

                editor.putString("lesson_th1", valueOf(lesson_th1));
                editor.putString("lesson_th2", valueOf(lesson_th2));
                editor.putString("lesson_th3", valueOf(lesson_th3));
                editor.putString("lesson_th4", valueOf(lesson_th4));
                editor.putString("lesson_th5", valueOf(lesson_th5));
                editor.putString("lesson_th6", valueOf(lesson_th6));
                editor.putString("lesson_th7", valueOf(lesson_th7));

                editor.putString("lesson_wed1", valueOf(lesson_wed1));
                editor.putString("lesson_wed2", valueOf(lesson_wed2));
                editor.putString("lesson_wed3", valueOf(lesson_wed3));
                editor.putString("lesson_wed4", valueOf(lesson_wed4));
                editor.putString("lesson_wed5", valueOf(lesson_wed5));
                editor.putString("lesson_wed6", valueOf(lesson_wed6));
                editor.putString("lesson_wed7", valueOf(lesson_wed7));

                editor.putString("lesson_thur1", valueOf(lesson_thur1));
                editor.putString("lesson_thur2", valueOf(lesson_thur2));
                editor.putString("lesson_thur3", valueOf(lesson_thur3));
                editor.putString("lesson_thur4", valueOf(lesson_thur4));
                editor.putString("lesson_thur5", valueOf(lesson_thur5));
                editor.putString("lesson_thur6", valueOf(lesson_thur6));
                editor.putString("lesson_thur7", valueOf(lesson_thur7));

                editor.putString("lesson_fri1", valueOf(lesson_fri1));
                editor.putString("lesson_fri2", valueOf(lesson_fri2));
                editor.putString("lesson_fri3", valueOf(lesson_fri3));
                editor.putString("lesson_fri4", valueOf(lesson_fri4));
                editor.putString("lesson_fri5", valueOf(lesson_fri5));
                editor.putString("lesson_fri6", valueOf(lesson_fri6));
                editor.putString("lesson_fri7", valueOf(lesson_fri7));

                editor.putString("lesson_sat1", valueOf(lesson_sat1));
                editor.putString("lesson_sat2", valueOf(lesson_sat2));
                editor.putString("lesson_sat3", valueOf(lesson_sat3));
                editor.putString("lesson_sat4", valueOf(lesson_sat4));
                editor.putString("lesson_sat5", valueOf(lesson_sat5));
                editor.putString("lesson_sat6", valueOf(lesson_sat6));
                editor.putString("lesson_sat7", valueOf(lesson_sat7));

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
