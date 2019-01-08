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
import android.widget.Toast;

import java.util.Objects;


public class CheckNewEmail extends Fragment {
    private String mOldKod;
    private String mNewKod;
    private String SpOldKod;
    private String SpNewKod;
    private String newEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_new_email, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SpOldKod = preferences.getString("kodOfEmailInChange", null);
        SpNewKod = preferences.getString("oldKodOfEmailInChange", null);
        newEmail = preferences.getString("newEmailInChange", null);

        Button singIn = rootView.findViewById(R.id.checkNewEmail3);
        singIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mOldKod = ((EditText) Objects.requireNonNull(getView()).findViewById(R.id.checkNewEmail1)).getText().toString().trim();
                mNewKod = ((EditText) getView().findViewById(R.id.checkNewEmail2)).getText().toString().trim();
                int TTOldKod = 0;
                int TTNewKod = -2;
                int XXOldKod = -3;
                int XXNewKod = -6;

                try {
                    TTOldKod = Integer.parseInt(SpOldKod);
                    TTNewKod = Integer.parseInt(SpNewKod);
                    XXOldKod = Integer.parseInt(mOldKod);
                    XXNewKod = Integer.parseInt(mNewKod);
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Ошибка в фотмате кода: " + e.getMessage())
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

                if(mOldKod.equals("") || mNewKod.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
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
                } else if(SpNewKod == null || SpOldKod == null || SpOldKod.equals("") || SpNewKod.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Ошибка в коде программыб ругайте разрабов")
                            .setCancelable(false)
                            .setNegativeButton("Ок, закрыть",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if(newEmail == null || newEmail.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Ошибка в email адресе")
                            .setCancelable(false)
                            .setNegativeButton("Ок, закрыть",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if((TTNewKod == XXNewKod && TTOldKod == XXOldKod) || (TTNewKod == XXOldKod && TTOldKod == XXNewKod)){
                    Fragment fragment = new SchoolProfile();
                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putString("isOkOfKodFromEmail", "true");
                    editor.putString("schoolEmail", newEmail);
                    editor.putString("newEmailInChange", null);
                    editor.putString("kodOfEmailInChange", null);
                    editor.putString("oldKodOfEmailInChange", null);
                    editor.apply();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Неверный код")
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

        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
