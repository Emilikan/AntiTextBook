package com.example.antitextbook;

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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.antitextbook.Constants.*;
import static com.example.antitextbook.MainActivity.fragmentIs;
import static java.lang.String.valueOf;

public class AdminForSchool extends Fragment {

    private String kod;
    private String school;
    private String dbSchool;
    private String email;

    private Boolean maySave = false;

    private ProgressBar progressBar;

    private DatabaseReference mRef;

    /**
     * Класс входа в админку школы пльзователя. Получаем код и названия школы, отправляем на сервер, смотрим, все ли ок и выдаем результат пользователю
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_for_school, container, false);
        fragmentIs = a0;
        progressBar = rootView.findViewById(R.id.progressBarInAdminForSchool);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String oldKod = preferences.getString("login", null);
        String oldNameSchool = preferences.getString("dbSchool", null);
        String oldEmail = preferences.getString("schoolEmail", null);
        String toFinish = preferences.getString("isOkOfKodFromEmail", "false");

        ImageView help = rootView.findViewById(R.id.helpAdmin);
        help.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Информация")
                        .setMessage("Данный раздел доступен только для представителей учебных заведений. Получить доступ вы можете написав в службу поддержки, указав почту для обратной связи." +
                                " Дополнительно вы можете указать в письме id в вк, ваш Telegram или Skype, где мы сможем с вами связаться\n\n" +
                                "Для входа укажите в точности те данные, которые вы получили без лишних пробелов. Почту укажите свою, для того, чтобы в дальнейшем мы могли с вами связаться\n" +
                                "Если вы забыли код, обратитесь в службу поддержки")
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

        if(!isOnline(Objects.requireNonNull(getContext()))){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Warning")
                    .setMessage("Нет доступа в интернет. Проверьте наличие связи")
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

            // это сделано, т.к. если мы зареганы и как админ и как шк представитель, то приложение вылетает
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert toFinish != null;
            if (oldKod != null && oldNameSchool != null && oldEmail != null && toFinish.equals("false") && user == null) {
                checkedLogin(oldKod, oldNameSchool, false, true);
            } else if (oldKod != null && oldNameSchool != null && oldEmail != null && toFinish.equals("true") && user == null) {
                checkedLogin(oldKod, oldNameSchool, true, true);
            } else {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("kodOfEmail", null);
                editor.putString("dbSchool", null);
                editor.putString("schoolEmail", null);
                editor.apply();
            }

            Switch saveMe = rootView.findViewById(R.id.checkBoxSaveMe);
            saveMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    maySave = isChecked;
                }
            });

            // кнопка входа
            Button singIn = rootView.findViewById(R.id.singInSchoolOfCode);
            singIn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    kod = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.kod)).getText().toString();
                    school = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.singInHS)).getText().toString();
                    email = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.schoolEmail)).getText().toString();
                    if ("".equals(school) || "".equals(kod)) {
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
                    } else if (email.equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        builder.setTitle("Warning")
                                .setMessage("Пожалуйста, заполните поле email. На этот email придет ответ при загрузке книг")
                                .setCancelable(false)
                                .setNegativeButton("Ок, закрыть",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        checkedLogin(kod, school, false, maySave);
                    }
                }
            });
        }
        return rootView;
    }

    // проверка кода
    private void checkedLogin (final String cod, final String nameOfSchool, final Boolean toFinish, final Boolean maySave1){

        progressBar.setVisibility(ProgressBar.VISIBLE);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbSchool = dataSnapshot.child("SchoolKod").child(cod).getValue(String.class);
                if(dbSchool == null){
                    AlertDialog.Builder ad;
                    ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    ad.setTitle("Предупреждение");  // заголовок
                    ad.setMessage("Неверный код или вашего учебного заведения нет в базе данных. Обратитесь в службу поддержки"); // сообщение
                    ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Fragment fragment = new Send();
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            fragmentIs = a2;
                        }
                    });
                    ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    });
                    ad.setCancelable(true);
                    ad.show();
                }
                else if(dbSchool.equals(nameOfSchool)){
                    if(getActivity() != null && getContext() != null) {
                        if (maySave1) {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("login", cod);
                            editor.apply();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        } else {
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("login", null);
                            editor.apply();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }

                        if (!toFinish) {
                            Fragment fragment = new CheckEmail();
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                            fragmentIs = a2;

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("dbSchool", valueOf(dbSchool));
                            editor.putString("schoolEmail", email);
                            editor.putString("isOkOfKodFromEmail", "false");
                            editor.apply();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        } else {
                            Fragment fragment = new SchoolProfile();
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.apply();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            fragmentIs = a2;
                        }
                    }
                }
                else {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    AlertDialog.Builder ad;
                    ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    ad.setTitle("Предупреждение");  // заголовок
                    ad.setMessage("Неверный код или название учебного завеждения. Попробуйте еще раз или напишите в службу поддержки"); // сообщение
                    ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Fragment fragment = new Send();
                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                            fragmentIs = a2;
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

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                ad.setTitle("Error");  // заголовок
                ad.setMessage("Ошибка: " + databaseError.getMessage() + "\n Проблемы в создании файла. Можете сообщить в службу поддержки"); // сообщение
                ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Fragment fragment = new Send();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        fragmentIs = a2;
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
        });
    }

    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mRef = null;
    }

}
