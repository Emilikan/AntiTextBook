package com.example.antitextbook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

import static java.lang.String.valueOf;


public class Regestration extends Fragment {

    private String userName;
    private String userSchool;
    private String userClass;
    private String studentOrSchooler = "";

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
        userClass = ((EditText) rootView.findViewById(R.id.userClass)).getText().toString();

        RadioGroup radioGroup = rootView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        studentOrSchooler = "";
                        Toast.makeText(getActivity(), "Ничего не выбранно", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.heightSchoolBoy:
                        studentOrSchooler = "Student";
                        Toast.makeText(getActivity(), "Студент", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.schoolBoy:
                        studentOrSchooler = "SchoolBoy";
                        Toast.makeText(getActivity(), "Школьник", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });


        Button save = rootView.findViewById(R.id.saveInfoAboutUser);
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(userClass) > 8 && studentOrSchooler.equals("Student")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Warning")
                            .setMessage("Вы не можете быть больше 8 курса. Выберите 'школьник' или напишите в службу поддержки.")
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
                else if(Integer.parseInt(userClass) > 12 && studentOrSchooler.equals("SchoolBoy")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Warning")
                            .setMessage("Вы не можете быть больше 12 класса. Выберите 'студент' или напишите в службу поддержки.")
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
                else if("".equals(userClass) || "".equals(userName) || "".equals(userSchool) || "".equals(studentOrSchooler)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Warning")
                            .setMessage("Не все поля заполненны, пожалуйста, заполните все поля")
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
                else {
                    // сохранение данных о пользователе в SharedPreference
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("UserName", userName);
                    editor.putString("UserClass", userClass);
                    editor.putString("UserSchool", userSchool);
                    editor.putString("UserStudentOrSchoolBoy", studentOrSchooler);
                    editor.apply();
                    Toast.makeText(getActivity(), "Сохраннено", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

}
