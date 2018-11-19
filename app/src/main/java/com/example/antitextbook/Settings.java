package com.example.antitextbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class Settings extends Fragment {

    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonInfoApp = (Button) rootView.findViewById(R.id.infoApp);
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
                saveFile(fullPath, String.valueOf("false"));
            } else {
                Toast.makeText(getActivity(), "Error: " + "не установлена cd-card. Для корректной работы приложения необходима cd-card", Toast.LENGTH_SHORT).show();
                //* поменять потом все на внутренний накопитель, ща чет не получилось и пошло оно все нахер
                //* а, и еще. Я ваще хз, куда созраняется файл (у меня на телефоне он, как мне показалось, сохраняет на внутреннюю)
            }
        }

        // сохраняет значение checkBox
        CheckBox checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // проверка на доступ к памяти
                if(isReadStorageAllowed()){
                    requestStoragePermission();
                }
                CheckBox checkBox = (CheckBox) Objects.requireNonNull(getView()).findViewById(R.id.checkBox);
                String folderName = "AntiTextBook/ATB/settings", fileName = "checkedBox.txt";
                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;
                if(checkBox.isChecked()){
                    int i = 1;
                    saveFile(fullPath,String.valueOf(i));
                }
                else {
                    int i = 2;
                    saveFile(fullPath, String.valueOf(i));
                }
            }
        });

        CheckBox darkBox = (CheckBox) rootView.findViewById(R.id.darkBox);
        darkBox.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                // проверка на доступ к памяти
                if(isReadStorageAllowed()){
                    requestStoragePermission();
                }

                CheckBox checkBox = (CheckBox) Objects.requireNonNull(getView()).findViewById(R.id.darkBox);
                String folderName1 = "AntiTextBook/ATB/settings", fileName = "darkBox.txt";
                String fullPathForDarkTheme = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName1 + "/" + fileName;
                Toast.makeText(getContext(), fullPathForDarkTheme, Toast.LENGTH_LONG).show();
                if(checkBox.isChecked()){
                    String i = "TRUE";
                    saveFile(fullPathForDarkTheme, i);
                }
                else {
                   String i = "FALSE";
                    saveFile(fullPathForDarkTheme, i);
                }
            }
        });
        return rootView;
    }
    public void saveFile (String filePath, String FileContent)
    {
        //Создание объекта файла.
        File fileHandle = new File(filePath);
        try
        {
            //Если нет директорий в пути, то они будут созданы:
            if (!fileHandle.getParentFile().exists())
                fileHandle.getParentFile().mkdirs();
            //Если файл существует, то он будет перезаписан:
            fileHandle.createNewFile();
            FileOutputStream fOut = new FileOutputStream(fileHandle);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.write(FileContent);
            myOutWriter.close();
            fOut.close();
        }
        catch (IOException e)
        {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return false;

        //If permission is not granted returning false
        return true;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

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




}
