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
import android.widget.ImageView;

import java.util.Objects;

public class CheckEmail extends Fragment {
    private String emailOfSchool;
    private int kod = 0;
    private int newKod;

    private SharedPreferences preferences;

    private EditText kodET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_email, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        emailOfSchool = preferences.getString("schoolEmail", "Ошибка");
        String isAgain = preferences.getString("kodOfEmail", null);

        if(isAgain == null) {
            Thread send = new Thread(new Runnable() {
                @Override
                public void run() {
                    sendEmail();
                }
            });
            send.start();
        } else {
            AlertDialog.Builder ad;
            ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            ad.setTitle("Информация");  // заголовок
            ad.setMessage("Код уже был отправлен. Введите код или отправте его еще раз"); // сообщение
            ad.setPositiveButton("Отправить еще раз", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Thread send = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sendEmail();
                        }
                    });
                    send.start();
                }
            });
            ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });
            ad.setCancelable(true);
            ad.show();
        }

        kodET = rootView.findViewById(R.id.kodForEmail);

        // кнопка входа
        ImageView singIn = rootView.findViewById(R.id.singInNew);
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringKod = kodET.getText().toString();
                if("".equals(stringKod)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Error")
                            .setMessage("Поле ввода не заполненно")
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
                    try {
                       newKod = Integer.parseInt(stringKod);
                    } catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        builder.setTitle("Error")
                                .setMessage("Ошибка при вводе кода:" + e.getMessage())
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
                    String wasKod = preferences.getString("kodOfEmail", null);
                    int oldKod = 0;
                    if(wasKod != null) {
                        oldKod = Integer.parseInt(wasKod);
                    }
                    if(newKod != 0 && oldKod == newKod && oldKod != 0){
                        Fragment fragment = new SchoolProfile();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("isOkOfKodFromEmail", "true");
                        editor.apply();
                    }
                    else {
                        AlertDialog.Builder ad;
                        ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        ad.setTitle("Информация");  // заголовок
                        ad.setMessage("Неверный код"); // сообщение
                        ad.setPositiveButton("Отправить еще раз", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                Thread send = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendEmail();
                                    }
                                });
                                send.start();
                            }
                        });
                        ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        });
                        ad.setCancelable(true);
                        ad.show();
                    }
                }
            }
        });

        // кнопка смены адреса
        Button newEmailAdres = rootView.findViewById(R.id.back5);
        newEmailAdres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("kodOfEmail", null);
                editor.putString("dbSchool", null);
                editor.putString("schoolEmail", null);
                editor.putString("isOkOfKodFromEmail", "false");
                editor.putString("login", null);
                editor.apply();

                Fragment fragment = new MainSettings();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        // кнопка повторного отправления
        Button sendAgain = rootView.findViewById(R.id.sendEmail);
        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread send = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendEmail();
                    }
                });
                send.start();
            }
        });

        return rootView;
    }

    private void sendEmail() {
        // генерим код
        kod = 1000 + (int) (Math.random() * 9999);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("kodOfEmail", Integer.toString(kod));
        editor.apply();

        String email = emailOfSchool;
        String subject = "Подтвердите E-mail адрес";
        String message = "Уважаемый представитель учебного заведения\n\n" +
                "Ваш E-mail адрес был использован для входа в аккаунт представителя учебного заведения в приложении AntiTextBook.\n\n\n" +
                "Код подтверждения:\n\n" + kod + "\n\nВ случае, если вы не входили в учетную запись с данным E-mail адресом, пожалуйста, проигнорируйте письмо.";

        SendMail sm = new SendMail(getContext(), email, subject, message);
        sm.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
