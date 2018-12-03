package com.example.antitextbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Schedule extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        Button changingSchedule =  rootView.findViewById(R.id.buttonChangeSchedule);
        changingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Нажата кнопка", Toast.LENGTH_SHORT).show();
                //* дописать обработку кноки "изменить расписание"
            }
        });

        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    public void changeTextView(View view){
        EditText editText = view.findViewById(R.id.lesson_mon1);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setKeyListener(null);
    }

}
