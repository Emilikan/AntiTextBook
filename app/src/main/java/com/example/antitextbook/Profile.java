package com.example.antitextbook;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;


public class Profile extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView profName = rootView.findViewById(R.id.profName);
        TextView profClass = rootView.findViewById(R.id.profClass);
        TextView profSchool = rootView.findViewById(R.id.profSchool);
        TextView profStOrSch = rootView.findViewById(R.id.profStOrSch);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // устанавливаем значения из SharedPreference

        profName.setText(preferences.getString("UserName", "Поле не заполнено"));
        profClass.setText(preferences.getString("UserClass", "Поле не заполнено"));
        profSchool.setText(preferences.getString("UserSchool", "Поле не заполнено"));
        String uStOrSch = preferences.getString("UserStudentOrSchoolBoy", "0");

        if(!"0".equals(uStOrSch) && "Student".equals(uStOrSch)){
            profStOrSch.setText("Вы студент");
        }
        else if (!"0".equals(uStOrSch) && "SchoolBoy".equals(uStOrSch)){
            profStOrSch.setText("Вы школьник");
        }
        else {
            profStOrSch.setText("Поле не заполненно");
        }

        Button changeProfile = rootView.findViewById(R.id.changeProfile);
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = Regestration.class;
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
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
