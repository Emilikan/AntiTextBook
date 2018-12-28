package com.example.antitextbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

import static java.lang.String.valueOf;

public class Settings extends Fragment {
    private int STORAGE_PERMISSION_CODE = 23;

    private FrameLayout frameLayout;
    private Button aboutApp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("CutPasteId")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        frameLayout = rootView.findViewById(R.id.settings);
        aboutApp = rootView.findViewById(R.id.infoApp);
        setTheme();


        Button buttonInfoApp = rootView.findViewById(R.id.infoApp); // кнопка информации о приложении
        buttonInfoApp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = InfoAboutApp.class;
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

        String folderName = "AntiTextBook/ATB/settings", fileName = "checkedBox.txt";
        final String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;

        File file = new File(fullPath);
        if(!file.exists()) {
            if (isExternalStorageWritable()) {

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Error")
                        .setMessage("не установлена cd-card. Для корректной работы приложения необходима cd-card")
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



        // сохраняет значение checkBox
        CheckBox checkBox = rootView.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                //сохранение значения в shared prefrences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("Checked", "pair");
                editor.apply();

                // проверка на доступ к памяти
                if(isReadStorageAllowed()){
                    requestStoragePermission();
                }

            }
        });

        CheckBox darkBox = rootView.findViewById(R.id.darkBox); // темная тема
        darkBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad;

                ad = new AlertDialog.Builder(getContext());
                ad.setTitle("Смена темы");  // заголовок
                ad.setMessage("Для смены темы приложение будет перезагружено"); // сообщение
                ad.setPositiveButton("Ок, перезагрузить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                        CheckBox checkBox = Objects.requireNonNull(getView()).findViewById(R.id.darkBox);
                        if(checkBox.isChecked()){
                            setThemeDark();
                        }
                        else {
                            setThemeNormal();
                        }
                    }
                });
                ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();

                    }
                });
                ad.setCancelable(true);
                ad.show();

            }
        });
        return rootView;
    }


    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result != PackageManager.PERMISSION_GRANTED;
    }

    //Requesting permission
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requestStoragePermission(){

        ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE);

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(getContext(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);
            aboutApp.setBackgroundResource(R.drawable.dark_cards);
        }
    }

    // метод изменения темы на темную
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setThemeDark(){
        String i = "TRUE";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Theme", i);
        editor.apply();


        doRestart(getActivity());
    }
    // метод изменения темы на светлую
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setThemeNormal(){
        String i = "FALSE";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Theme", i);
        editor.apply();

        doRestart(getActivity());
    }

    public static void doRestart(Context c) {

        // костыль
        // ждем для перезагрузки приложения (чтобы инфа о смене темы добавилась в SharedPreference)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (c != null) {
                PackageManager pm = c.getPackageManager();

                if (pm != null) {

                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(c, mPendingIntentId, mStartActivity,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1, mPendingIntent);
                        
                        System.exit(0);
                    } else {
                        Toast.makeText(c,"Was not able to restart application, mStartActivity null",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(c,"Was not able to restart application, PM null",Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(c,"Was not able to restart application, Context null",Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(c,"Was not able to restart application",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
