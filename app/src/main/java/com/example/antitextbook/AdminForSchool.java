package com.example.antitextbook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static java.lang.String.valueOf;

public class AdminForSchool extends Fragment {

    private String kod;
    private String school;
    private String dbSchool;

    private DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_for_school, container, false);

        ImageView back = rootView.findViewById(R.id.back334); // кнопка назад
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = Server.class;
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

        Button singIn = rootView.findViewById(R.id.singInSchoolOfCode);
        singIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                kod = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.kod)).getText().toString();
                school = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.singInHS)).getText().toString();
                if("".equals(school) || "".equals(kod)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Warning")
                            .setMessage("Не все поля заполненны, пожалуйста, заполните все поля")
                            .setCancelable(false)
                            .setNegativeButton("Ок, закрыть",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            Fragment fragment = new Server();
                                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                            Toast.makeText(getActivity(), "Нет книг", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    mRef = FirebaseDatabase.getInstance().getReference();
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dbSchool = dataSnapshot.child("SchoolKod").child(kod).getValue(String.class);
                            if(dbSchool == null){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                                builder.setTitle("Warning")
                                        .setMessage("Неверный код или вашего учебного заведения нет в базе данных. Обратитесь в службу поддержки")
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
                            else if(dbSchool.equals(school)){
                                Fragment fragment = new Subscribe();
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("dbSchool", valueOf(dbSchool));
                                editor.apply();

                                Toast.makeText(getContext(), "Авторизация успешна", Toast.LENGTH_LONG).show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                                builder.setTitle("Warning")
                                        .setMessage("Неверный код или название учебного завеждения. Попробуйте еще раз или напишите в службу поддержки")
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
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                            builder.setTitle("Error")
                                    .setMessage(databaseError.getMessage())
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
                    });
                }
            }
        });
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
