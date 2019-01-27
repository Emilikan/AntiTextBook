package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * Вход в админку-разработчик
 */

public class Server extends Fragment {
    private FrameLayout frameLayout;

    private String mLogin;
    private String mPassword;

    private Boolean maySave;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_server, container, false);

        frameLayout = rootView.findViewById(R.id.server);
        setTheme();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String isToNext = preferences.getString("saveMeAdmin", "false");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert isToNext != null;
        if (user != null && isOnline(Objects.requireNonNull(getContext())) && isToNext.equals("true")){
            Fragment fragment = new AdminOfApp();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        else {

            Switch saveMe = rootView.findViewById(R.id.checkBoxSaveMeInAdm);
            saveMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    maySave = isChecked;
                }
            });

            ImageView help = rootView.findViewById(R.id.helpAdmin2);
            help.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Информация")
                            .setMessage("  В настоящее время авторизация доступна только для админов и представителей учебных заведений\n  По всем вопросам обращайтесь в службу поддержки")
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

            Button singIn = rootView.findViewById(R.id.singIn); // кнопка авторизации
            singIn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    mLogin = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.login)).getText().toString();
                    mPassword = ((EditText) getActivity().findViewById(R.id.password)).getText().toString();
                    if ("".equals(mLogin) || "".equals(mPassword)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        builder.setTitle("Error")
                                .setMessage("Одно из полей не заполненно. Пожалуйста, заполните все поля и повторите отправку")
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
                        singInAdmin();
                    }
                }
            });
        }
        return rootView;
    }

    // авторизация админки
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void singInAdmin(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(mLogin, mPassword).addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    if(getContext() != null && getActivity() != null) {
                        if(maySave){
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("saveMeAdmin", "true");
                            editor.apply();
                        } else {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("saveMeAdmin", "false");
                            editor.apply();
                        }

                        Toast.makeText(getContext(), "Авторизация успешна", Toast.LENGTH_LONG).show();
                        Fragment fragment = new AdminOfApp();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                    }

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Авторизация провалена")
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
        });
    }

    // проверка на доступ к интернету
    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
