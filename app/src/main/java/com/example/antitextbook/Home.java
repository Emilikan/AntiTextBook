package com.example.antitextbook;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Home extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView1 = inflater.inflate(R.layout.fragment_home, container, false);

        int numberOfPictures = 5, numberOfPicturesMin = 5, numberOfPicturesMax = 6;
        ImageView mImageView;
        ScrollView scrollViewSwipe;
        String folderName = "temp/ATB", fileName = "numberOfPictures.txt";// название файла, где хранится номер данной картинки

        scrollViewSwipe = (ScrollView) rootView1.findViewById(R.id.scrollView1);// получаем ScrollView для свайпов
        mImageView = (ImageView) rootView1.findViewById(R.id.imageView1);// получаем ImageView для установки и смены картинки

        // проверяем наличие файла
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;
        if(isExternalStorageWritable()){
            saveFile(fullPath, String.valueOf(numberOfPicturesMin));
        }
        else{
            Toast.makeText(getActivity(), "Error: " + "не установлена cd-card. Для корректной работы приложения необходима cd-card", Toast.LENGTH_SHORT).show();
            // поменять потом все на внутренний накопитель, ща чет не получилось и пошло оно все нахер
            // а, и еще. Я ваще хз, куда созраняется файл (у меня на телефоне он, как мне показалось, сохраняет на внутреннюю)
        }

        swipeImage(mImageView, scrollViewSwipe);

        return rootView1;
    }
    public void swipeImage(ImageView imageView, ScrollView scrollViewSwipe){

        //обработка свайпа (там же сменяем картинку)
        scrollViewSwipe.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                public void onSwipeRight() {
                //обработка свайпа вправо
                    String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/ATB/numberOfPictures.txt";
                    String numberString = readFile(fullPath);;
                    try {
                        int myInt = Integer.parseInt(numberString);
                        if(myInt != 0) {
                            myInt--;
                        }
                        saveFile(fullPath, String.valueOf(myInt));
                        numberString = readFile(fullPath);
                        Toast.makeText(getActivity(), numberString, Toast.LENGTH_SHORT).show();

                        ImageView imageView = (ImageView) getView().findViewById(R.id.imageView1);

                        String numberStringRes = readFile(fullPath);
                        int number = Integer.parseInt(numberStringRes);

                        if(number == 5){
                            imageView.setImageResource(R.drawable.geography10_5);
                        }
                        else {
                            imageView.setImageResource(R.drawable.geography10_6);
                        }
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onSwipeLeft() {
                //обработка свайпа влево
                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/ATB/numberOfPictures.txt";
                String numberString = readFile(fullPath);
                try {
                    int myInt = Integer.parseInt(numberString);
                    myInt++;
                    saveFile(fullPath, String.valueOf(myInt));
                    numberString = readFile(fullPath);
                    Toast.makeText(getActivity(), numberString, Toast.LENGTH_SHORT).show();

                    ImageView imageView = (ImageView) getView().findViewById(R.id.imageView1);

                    String numberStringRes = readFile(fullPath);
                    int number = Integer.parseInt(numberStringRes);

                    if(number == 5){
                        imageView.setImageResource(R.drawable.geography10_5);
                    }
                    else {
                        imageView.setImageResource(R.drawable.geography10_6);
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        } );

        // устанавливаем картинку в самом начале только один раз (при свайпах к этому уже не обращаемся)
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/ATB/numberOfPictures.txt";
        String numberString = readFile(fullPath);
        try {
           int number = Integer.parseInt(numberString);

           if(number == 5){
               imageView.setImageResource(R.drawable.geography10_5);
           }
           else {
               imageView.setImageResource(R.drawable.geography10_6);
           }
        }
        catch (NumberFormatException e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            imageView.setImageResource(R.drawable.geography10_5);
        }
    }

    // Функция, которая проверяет, доступно ли external storage(cd-card) для чтения и записи
    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            return true;
        }
        return false;
    }

    //Функция, которая сохраняет файл, принимая полный путь до файла filePath и сохраняемый текст FileContent (так же используется для создания файла)
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

    // Функция, которая читает файл по определенному адресу, возвращает то, что в файле (Если там более одного слова, то вернет все "слипшееся")
    // Переписать ее для того, чтобы можно было использовать свой адресс
    public String readFile (String path){
        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file = new File(sdcard,"/temp/ATB/numberOfPictures.txt");
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String resultText = "";

            while ((line = br.readLine()) != null) {
                resultText += line;
                return resultText;
            }
            br.close();
        }
        catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}
