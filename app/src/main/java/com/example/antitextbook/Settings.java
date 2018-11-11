package com.example.antitextbook;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class Settings extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button button1 = (Button) rootView.findViewById(R.id.infoApp);

        button1.setOnClickListener(new View.OnClickListener() {
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        String folderName = "temp/ATB/settings", fileName = "checkedBox.txt";
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;
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
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) getView().findViewById(R.id.checkBox);
                String folderName = "temp/ATB/settings", fileName = "checkedBox.txt";
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


}
