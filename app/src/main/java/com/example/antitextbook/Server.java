package com.example.antitextbook;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Server extends Fragment {
    private String mLogin;
    private String mPassword;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            Fragment fragment = new Cloud();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            EditText login = (EditText) getActivity().findViewById(R.id.login);
            EditText password = (EditText) getActivity().findViewById(R.id.password);

            Button singIn = (Button) rootView.findViewById(R.id.singIn); // кнопка авторизации
            singIn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    mLogin = ((EditText) getActivity().findViewById(R.id.login)).getText().toString();
                    mPassword = ((EditText) getActivity().findViewById(R.id.password)).getText().toString();
                    if ("".equals(mLogin) || "".equals(mPassword)) {
                        Toast.makeText(getActivity(), "Одно из полей не заполненно. Пожалуйста, заполните все поля и повторите отправку", Toast.LENGTH_LONG).show();
                    } else {
                        singInUser();
                    }

                }
            });
        }
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void singInUser(){
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    // sing in

                }
                else {
                    // sing out
                }
            }
        };
        mAuth.signInWithEmailAndPassword(mLogin, mPassword).addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Авторизация успешна", Toast.LENGTH_LONG).show();
                    Fragment fragment = new Cloud();

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);

                }
                else{
                    Toast.makeText(getContext(), "Авторизация провалена", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
