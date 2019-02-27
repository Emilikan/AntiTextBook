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

public class Schedule extends Fragment {
    private Button changingSchedule;
    private FrameLayout frameLayout;
    private EditText lesson_mon1,lesson_mon2,lesson_mon3,lesson_mon4,lesson_mon5,lesson_mon6,lesson_mon7, lesson_th1,lesson_th2,lesson_th3,lesson_th4,lesson_th5,lesson_th6,lesson_th7,lesson_wed1,lesson_wed2,lesson_wed3,lesson_wed4,lesson_wed5,lesson_wed6,lesson_wed7,lesson_thur1,lesson_thur2,lesson_thur3,lesson_thur4,lesson_thur5,lesson_thur6,lesson_thur7,lesson_fri1,lesson_fri2,lesson_fri3,lesson_fri4,lesson_fri5,lesson_fri6,lesson_fri7,lesson_sat1,lesson_sat2,lesson_sat3,lesson_sat4,lesson_sat5,lesson_sat6,lesson_sat7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String[] lesson_mon1 = {preferences.getString("lesson_mon1", "Первый урок")};
        final String[] lesson_mon2 = {preferences.getString("lesson_mon2", "Второй урок")};
        final String[] lesson_mon3 = {preferences.getString("lesson_mon3", "Третий урок")};
        final String[] lesson_mon4 = {preferences.getString("lesson_mon4", "Четвертый урок")};
        final String[] lesson_mon5 = {preferences.getString("lesson_mon5", "Пятый урок")};
        final String[] lesson_mon6 = {preferences.getString("lesson_mon6", "Шестой урок")};
        final String[] lesson_mon7 = {preferences.getString("lesson_mon7", "Седьмой урок")};

        final String[] lesson_th1 = {preferences.getString("lesson_th1", "")};
        final String[] lesson_th2 = {preferences.getString("lesson_th2", "")};
        final String[] lesson_th3 = {preferences.getString("lesson_th3", "")};
        final String[] lesson_th4 = {preferences.getString("lesson_th4", "")};
        final String[] lesson_th5 = {preferences.getString("lesson_th5", "")};
        final String[] lesson_th6 = {preferences.getString("lesson_th6", "")};
        final String[] lesson_th7 = {preferences.getString("lesson_th7", "")};

        final String[] lesson_wed1 = {preferences.getString("lesson_wed1", "")};
        final String[] lesson_wed2 = {preferences.getString("lesson_wed2", "")};
        final String[] lesson_wed3 = {preferences.getString("lesson_wed3", "")};
        final String[] lesson_wed4 = {preferences.getString("lesson_wed4", "")};
        final String[] lesson_wed5 = {preferences.getString("lesson_wed5", "")};
        final String[] lesson_wed6 = {preferences.getString("lesson_wed6", "")};
        final String[] lesson_wed7 = {preferences.getString("lesson_wed7", "")};

        final String[] lesson_thur1 = {preferences.getString("lesson_thur1", "")};
        final String[] lesson_thur2 = {preferences.getString("lesson_thur2", "")};
        final String[] lesson_thur3 = {preferences.getString("lesson_thur3", "")};
        final String[] lesson_thur4 = {preferences.getString("lesson_thur4", "")};
        final String[] lesson_thur5 = {preferences.getString("lesson_thur5", "")};
        final String[] lesson_thur6 = {preferences.getString("lesson_thur6", "")};
        final String[] lesson_thur7 = {preferences.getString("lesson_thur7", "")};

        final String[] lesson_fri1 = {preferences.getString("lesson_fri1", "")};
        final String[] lesson_fri2 = {preferences.getString("lesson_fri2", "")};
        final String[] lesson_fri3 = {preferences.getString("lesson_fri3", "")};
        final String[] lesson_fri4 = {preferences.getString("lesson_fri4", "")};
        final String[] lesson_fri5 = {preferences.getString("lesson_fri5", "")};
        final String[] lesson_fri6 = {preferences.getString("lesson_fri6", "")};
        final String[] lesson_fri7 = {preferences.getString("lesson_fri7", "")};

        final String[] lesson_sat1 = {preferences.getString("lesson_sat1", "")};
        final String[] lesson_sat2 = {preferences.getString("lesson_sat2", "")};
        final String[] lesson_sat3 = {preferences.getString("lesson_sat3", "")};
        final String[] lesson_sat4 = {preferences.getString("lesson_sat4", "")};
        final String[] lesson_sat5 = {preferences.getString("lesson_sat5", "")};
        final String[] lesson_sat6 = {preferences.getString("lesson_sat6", "")};
        final String[] lesson_sat7 = {preferences.getString("lesson_sat7", "")};

        frameLayout = rootView.findViewById(R.id.schedule);

        EditText les_mon_1 = rootView.findViewById(R.id.lesson_mon1);
        EditText les_mon_2 = rootView.findViewById(R.id.lesson_mon2);
        EditText les_mon_3 = rootView.findViewById(R.id.lesson_mon3);
        EditText les_mon_4 = rootView.findViewById(R.id.lesson_mon4);
        EditText les_mon_5 = rootView.findViewById(R.id.lesson_mon5);
        EditText les_mon_6 = rootView.findViewById(R.id.lesson_mon6);
        EditText les_mon_7 = rootView.findViewById(R.id.lesson_mon7);

        EditText les_th_1 = rootView.findViewById(R.id.lesson_th1);
        EditText les_th_2 = rootView.findViewById(R.id.lesson_th2);
        EditText les_th_3 = rootView.findViewById(R.id.lesson_th3);
        EditText les_th_4 = rootView.findViewById(R.id.lesson_th4);
        EditText les_th_5 = rootView.findViewById(R.id.lesson_th5);
        EditText les_th_6 = rootView.findViewById(R.id.lesson_th6);
        EditText les_th_7 = rootView.findViewById(R.id.lesson_th7);

        EditText les_wed_1 = rootView.findViewById(R.id.lesson_wed1);
        EditText les_wed_2 = rootView.findViewById(R.id.lesson_wed2);
        EditText les_wed_3 = rootView.findViewById(R.id.lesson_wed3);
        EditText les_wed_4 = rootView.findViewById(R.id.lesson_wed4);
        EditText les_wed_5 = rootView.findViewById(R.id.lesson_wed5);
        EditText les_wed_6 = rootView.findViewById(R.id.lesson_wed6);
        EditText les_wed_7 = rootView.findViewById(R.id.lesson_wed7);

        EditText les_thur_1 = rootView.findViewById(R.id.lesson_thur1);
        EditText les_thur_2 = rootView.findViewById(R.id.lesson_thur2);
        EditText les_thur_3 = rootView.findViewById(R.id.lesson_thur3);
        EditText les_thur_4 = rootView.findViewById(R.id.lesson_thur4);
        EditText les_thur_5 = rootView.findViewById(R.id.lesson_thur5);
        EditText les_thur_6 = rootView.findViewById(R.id.lesson_thur6);
        EditText les_thur_7 = rootView.findViewById(R.id.lesson_thur7);

        EditText les_fri_1 = rootView.findViewById(R.id.lesson_fri1);
        EditText les_fri_2 = rootView.findViewById(R.id.lesson_fri2);
        EditText les_fri_3 = rootView.findViewById(R.id.lesson_fri3);
        EditText les_fri_4 = rootView.findViewById(R.id.lesson_fri4);
        EditText les_fri_5 = rootView.findViewById(R.id.lesson_fri5);
        EditText les_fri_6 = rootView.findViewById(R.id.lesson_fri6);
        EditText les_fri_7 = rootView.findViewById(R.id.lesson_fri7);

        EditText les_sat_1 = rootView.findViewById(R.id.lesson_sat1);
        EditText les_sat_2 = rootView.findViewById(R.id.lesson_sat2);
        EditText les_sat_3 = rootView.findViewById(R.id.lesson_sat3);
        EditText les_sat_4 = rootView.findViewById(R.id.lesson_sat4);
        EditText les_sat_5 = rootView.findViewById(R.id.lesson_sat5);
        EditText les_sat_6 = rootView.findViewById(R.id.lesson_sat6);
        EditText les_sat_7 = rootView.findViewById(R.id.lesson_sat7);


        les_mon_1.setText(lesson_mon1[0]);
        les_mon_2.setText(lesson_mon2[0]);
        les_mon_3.setText(lesson_mon3[0]);
        les_mon_4.setText(lesson_mon4[0]);
        les_mon_5.setText(lesson_mon5[0]);
        les_mon_6.setText(lesson_mon6[0]);
        les_mon_7.setText(lesson_mon7[0]);

        les_th_1.setText(lesson_th1[0]);
        les_th_2.setText(lesson_th2[0]);
        les_th_3.setText(lesson_th3[0]);
        les_th_4.setText(lesson_th4[0]);
        les_th_5.setText(lesson_th5[0]);
        les_th_6.setText(lesson_th6[0]);
        les_th_7.setText(lesson_th7[0]);

        les_wed_1.setText(lesson_wed1[0]);
        les_wed_2.setText(lesson_wed2[0]);
        les_wed_3.setText(lesson_wed3[0]);
        les_wed_4.setText(lesson_wed4[0]);
        les_wed_5.setText(lesson_wed5[0]);
        les_wed_6.setText(lesson_wed6[0]);
        les_wed_7.setText(lesson_wed7[0]);

        les_thur_1.setText(lesson_thur1[0]);
        les_thur_2.setText(lesson_thur2[0]);
        les_thur_3.setText(lesson_thur3[0]);
        les_thur_4.setText(lesson_thur4[0]);
        les_thur_5.setText(lesson_thur5[0]);
        les_thur_6.setText(lesson_thur6[0]);
        les_thur_7.setText(lesson_thur7[0]);

        les_fri_1.setText(lesson_fri1[0]);
        les_fri_2.setText(lesson_fri2[0]);
        les_fri_3.setText(lesson_fri3[0]);
        les_fri_4.setText(lesson_fri4[0]);
        les_fri_5.setText(lesson_fri5[0]);
        les_fri_6.setText(lesson_fri6[0]);
        les_fri_7.setText(lesson_fri7[0]);

        les_sat_1.setText(lesson_sat1[0]);
        les_sat_2.setText(lesson_sat2[0]);
        les_sat_3.setText(lesson_sat3[0]);
        les_sat_4.setText(lesson_sat4[0]);
        les_sat_5.setText(lesson_sat5[0]);
        les_sat_6.setText(lesson_sat6[0]);
        les_sat_7.setText(lesson_sat7[0]);

        changingSchedule =  rootView.findViewById(R.id.buttonChangeSchedule);
        changingSchedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();

                EditText les_mon_1 = rootView.findViewById(R.id.lesson_mon1);
                EditText les_mon_2 = rootView.findViewById(R.id.lesson_mon2);
                EditText les_mon_3 = rootView.findViewById(R.id.lesson_mon3);
                EditText les_mon_4 = rootView.findViewById(R.id.lesson_mon4);
                EditText les_mon_5 = rootView.findViewById(R.id.lesson_mon5);
                EditText les_mon_6 = rootView.findViewById(R.id.lesson_mon6);
                EditText les_mon_7 = rootView.findViewById(R.id.lesson_mon7);

                EditText les_th_1 = rootView.findViewById(R.id.lesson_th1);
                EditText les_th_2 = rootView.findViewById(R.id.lesson_th2);
                EditText les_th_3 = rootView.findViewById(R.id.lesson_th3);
                EditText les_th_4 = rootView.findViewById(R.id.lesson_th4);
                EditText les_th_5 = rootView.findViewById(R.id.lesson_th5);
                EditText les_th_6 = rootView.findViewById(R.id.lesson_th6);
                EditText les_th_7 = rootView.findViewById(R.id.lesson_th7);

                EditText les_wed_1 = rootView.findViewById(R.id.lesson_wed1);
                EditText les_wed_2 = rootView.findViewById(R.id.lesson_wed2);
                EditText les_wed_3 = rootView.findViewById(R.id.lesson_wed3);
                EditText les_wed_4 = rootView.findViewById(R.id.lesson_wed4);
                EditText les_wed_5 = rootView.findViewById(R.id.lesson_wed5);
                EditText les_wed_6 = rootView.findViewById(R.id.lesson_wed6);
                EditText les_wed_7 = rootView.findViewById(R.id.lesson_wed7);

                EditText les_thur_1 = rootView.findViewById(R.id.lesson_thur1);
                EditText les_thur_2 = rootView.findViewById(R.id.lesson_thur2);
                EditText les_thur_3 = rootView.findViewById(R.id.lesson_thur3);
                EditText les_thur_4 = rootView.findViewById(R.id.lesson_thur4);
                EditText les_thur_5 = rootView.findViewById(R.id.lesson_thur5);
                EditText les_thur_6 = rootView.findViewById(R.id.lesson_thur6);
                EditText les_thur_7 = rootView.findViewById(R.id.lesson_thur7);

                EditText les_fri_1 = rootView.findViewById(R.id.lesson_fri1);
                EditText les_fri_2 = rootView.findViewById(R.id.lesson_fri2);
                EditText les_fri_3 = rootView.findViewById(R.id.lesson_fri3);
                EditText les_fri_4 = rootView.findViewById(R.id.lesson_fri4);
                EditText les_fri_5 = rootView.findViewById(R.id.lesson_fri5);
                EditText les_fri_6 = rootView.findViewById(R.id.lesson_fri6);
                EditText les_fri_7 = rootView.findViewById(R.id.lesson_fri7);

                EditText les_sat_1 = rootView.findViewById(R.id.lesson_sat1);
                EditText les_sat_2 = rootView.findViewById(R.id.lesson_sat2);
                EditText les_sat_3 = rootView.findViewById(R.id.lesson_sat3);
                EditText les_sat_4 = rootView.findViewById(R.id.lesson_sat4);
                EditText les_sat_5 = rootView.findViewById(R.id.lesson_sat5);
                EditText les_sat_6 = rootView.findViewById(R.id.lesson_sat6);
                EditText les_sat_7 = rootView.findViewById(R.id.lesson_sat7);

                //сохранение расписания


                lesson_mon1[0] = les_mon_1.getText().toString();
                lesson_mon2[0] = les_mon_2.getText().toString();
                lesson_mon3[0] = les_mon_3.getText().toString();
                lesson_mon4[0] = les_mon_4.getText().toString();
                lesson_mon5[0] = les_mon_5.getText().toString();
                lesson_mon6[0] = les_mon_6.getText().toString();
                lesson_mon7[0] = les_mon_7.getText().toString();

                lesson_th1[0] = les_th_1.getText().toString();
                lesson_th2[0] = les_th_2.getText().toString();
                lesson_th3[0] = les_th_3.getText().toString();
                lesson_th4[0] = les_th_4.getText().toString();
                lesson_th5[0] = les_th_5.getText().toString();
                lesson_th6[0] = les_th_6.getText().toString();
                lesson_th7[0] = les_th_7.getText().toString();

                lesson_wed1[0] = les_wed_1.getText().toString();
                lesson_wed2[0] = les_wed_2.getText().toString();
                lesson_wed3[0] = les_wed_3.getText().toString();
                lesson_wed4[0] = les_wed_4.getText().toString();
                lesson_wed5[0] = les_wed_5.getText().toString();
                lesson_wed6[0] = les_wed_6.getText().toString();
                lesson_wed7[0] = les_wed_7.getText().toString();

                lesson_thur1[0] = les_thur_1.getText().toString();
                lesson_thur2[0] = les_thur_2.getText().toString();
                lesson_thur3[0] = les_thur_3.getText().toString();
                lesson_thur4[0] = les_thur_4.getText().toString();
                lesson_thur5[0] = les_thur_5.getText().toString();
                lesson_thur6[0] = les_thur_6.getText().toString();
                lesson_thur7[0] = les_thur_7.getText().toString();

                lesson_fri1[0] = les_fri_1.getText().toString();
                lesson_fri2[0] = les_fri_2.getText().toString();
                lesson_fri3[0] = les_fri_3.getText().toString();
                lesson_fri4[0] = les_fri_4.getText().toString();
                lesson_fri5[0] = les_fri_5.getText().toString();
                lesson_fri6[0] = les_fri_6.getText().toString();
                lesson_fri7[0] = les_fri_7.getText().toString();

                lesson_sat1[0] = les_sat_1.getText().toString();
                lesson_sat2[0] = les_sat_2.getText().toString();
                lesson_sat3[0] = les_sat_3.getText().toString();
                lesson_sat4[0] = les_sat_4.getText().toString();
                lesson_sat5[0] = les_sat_5.getText().toString();
                lesson_sat6[0] = les_sat_6.getText().toString();
                lesson_sat7[0] = les_sat_7.getText().toString();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lesson_mon1", valueOf(lesson_mon1[0]));
                editor.putString("lesson_mon2", valueOf(lesson_mon2[0]));
                editor.putString("lesson_mon3", valueOf(lesson_mon3[0]));
                editor.putString("lesson_mon4", valueOf(lesson_mon4[0]));
                editor.putString("lesson_mon5", valueOf(lesson_mon5[0]));
                editor.putString("lesson_mon6", valueOf(lesson_mon6[0]));
                editor.putString("lesson_mon7", valueOf(lesson_mon7[0]));

                editor.putString("lesson_th1", valueOf(lesson_th1[0]));
                editor.putString("lesson_th2", valueOf(lesson_th2[0]));
                editor.putString("lesson_th3", valueOf(lesson_th3[0]));
                editor.putString("lesson_th4", valueOf(lesson_th4[0]));
                editor.putString("lesson_th5", valueOf(lesson_th5[0]));
                editor.putString("lesson_th6", valueOf(lesson_th6[0]));
                editor.putString("lesson_th7", valueOf(lesson_th7[0]));

                editor.putString("lesson_wed1", valueOf(lesson_wed1[0]));
                editor.putString("lesson_wed2", valueOf(lesson_wed2[0]));
                editor.putString("lesson_wed3", valueOf(lesson_wed3[0]));
                editor.putString("lesson_wed4", valueOf(lesson_wed4[0]));
                editor.putString("lesson_wed5", valueOf(lesson_wed5[0]));
                editor.putString("lesson_wed6", valueOf(lesson_wed6[0]));
                editor.putString("lesson_wed7", valueOf(lesson_wed7[0]));

                editor.putString("lesson_thur1", valueOf(lesson_thur1[0]));
                editor.putString("lesson_thur2", valueOf(lesson_thur2[0]));
                editor.putString("lesson_thur3", valueOf(lesson_thur3[0]));
                editor.putString("lesson_thur4", valueOf(lesson_thur4[0]));
                editor.putString("lesson_thur5", valueOf(lesson_thur5[0]));
                editor.putString("lesson_thur6", valueOf(lesson_thur6[0]));
                editor.putString("lesson_thur7", valueOf(lesson_thur7[0]));

                editor.putString("lesson_fri1", valueOf(lesson_fri1[0]));
                editor.putString("lesson_fri2", valueOf(lesson_fri2[0]));
                editor.putString("lesson_fri3", valueOf(lesson_fri3[0]));
                editor.putString("lesson_fri4", valueOf(lesson_fri4[0]));
                editor.putString("lesson_fri5", valueOf(lesson_fri5[0]));
                editor.putString("lesson_fri6", valueOf(lesson_fri6[0]));
                editor.putString("lesson_fri7", valueOf(lesson_fri7[0]));

                editor.putString("lesson_sat1", valueOf(lesson_sat1[0]));
                editor.putString("lesson_sat2", valueOf(lesson_sat2[0]));
                editor.putString("lesson_sat3", valueOf(lesson_sat3[0]));
                editor.putString("lesson_sat4", valueOf(lesson_sat4[0]));
                editor.putString("lesson_sat5", valueOf(lesson_sat5[0]));
                editor.putString("lesson_sat6", valueOf(lesson_sat6[0]));
                editor.putString("lesson_sat7", valueOf(lesson_sat7[0]));

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
