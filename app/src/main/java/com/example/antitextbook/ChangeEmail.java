package com.example.antitextbook;

import android.content.Context;
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

import java.util.Objects;

/**
 * Класс для тзменения почты. Отправляет два письма: на старую и новую почты
 */

public class ChangeEmail extends Fragment {
    private EditText email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_email, container, false);
        email = rootView.findViewById(R.id.changeEmail);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String oldEmail = preferences.getString("schoolEmail", null);

        Button next = rootView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final String emailString = email.getText().toString().trim();
                if(!emailString.equals("")){
                    final Context context = getContext();

                    Thread send = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int kod1 = 1000 + (int) (Math.random() * 9999);
                            int kod2 = 1000 + (int) (Math.random() * 9999);

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("kodOfEmailInChange", Integer.toString(kod1));
                            editor.putString("newEmailInChange", emailString);
                            editor.putString("oldKodOfEmailInChange", Integer.toString(kod2));
                            editor.apply();

                            sendEmail(emailString, kod1, context);
                            sendEmail(oldEmail, kod2, context);
                        }
                    });
                    send.start();
                    Fragment fragment = new CheckNewEmail();
                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
                else {
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
            }
        });

        return rootView;
    }

    private void sendEmail(String emailOfSchool, int kod, Context context) {

        String subject = "Подтвердите E-mail адрес";
        String message = "Уважаемый представитель учебного заведения\n\n" +
                "Ваш E-mail адрес был использован для входа в аккаунт представителя учебного заведения в приложении AntiTextBook.\n\n\n" +
                "Код подтверждения:\n\n" + kod + "\n\nВ случае, если вы не входили в учетную запись с данным E-mail адресом, пожалуйста, проигнорируйте письмо.";

        SendMail sm = new SendMail(context, emailOfSchool, subject, message);
        sm.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
