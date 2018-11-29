package com.example.antitextbook;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class Home extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView1 = inflater.inflate(R.layout.fragment_home, container, false);

        int numberOfPicturesMin = 0;
        String folderName = "AntiTextBook/ATB", fileName = "numberOfPictures.txt";// название файла, где хранится номер данной картинки

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;
        String folderName1 = "AntiTextBook/ATB/settings", fileName1 = "darkBox.txt";
        String fullPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName1 + "/" + fileName1;
        String dark = readFile(fullPath1);

        File file = new File(fullPath);

        // открытие pdf файла
        PDFView pdfView = rootView1.findViewById(R.id.pdfView);
        if("TRUE".equals(dark)) {
            pdfView.fromAsset("geogr_10_maksakovskiy.pdf")
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .nightMode(true)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .onDrawAll(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {}
                    })
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .load();
        }
        else {
            pdfView.fromAsset("geogr_10_maksakovskiy.pdf")
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .nightMode(false)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .onDrawAll(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {}
                    })
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .load();
        }

        // проверяем наличие cd-card
        if(!file.exists()) {
            if (isExternalStorageWritable()) {
                saveFile(fullPath, String.valueOf(numberOfPicturesMin));
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

        String numberString = readFile(fullPath);

        try {
            int number = Integer.parseInt(numberString);
            String lesson = "geography", grage = "10";
            String name = lesson + grage + "_"+ number;
            int holderInt = getResources().getIdentifier(name, "drawable", Objects.requireNonNull(getActivity()).getPackageName()); // для поиска id по названию. Крутаю вещь. Если ты не понимаешь, что это, то спроси у меня (Эмиля)
            //mImageView.setImageResource(holderInt); // используем это для изменения КАРТИНКИ
        }
        catch (NumberFormatException e){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Error")
                    .setMessage(e.getMessage())
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
        //swipeImage(mImageView, scrollViewSwipe);
        return rootView1;
    }


    private void swipeImage(ImageView imageView, ScrollView scrollViewSwipe){
/*
        //обработка свайпа (там же сменяем картинку)
        scrollViewSwipe.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                public void onSwipeRight() {
                //обработка свайпа вправо
                    String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AntiTextBook/ATB/numberOfPictures.txt";
                    String numberString = readFile(fullPath);
                    try {
                        int myInt = Integer.parseInt(numberString);
                        if(myInt != 0) {
                            myInt--;
                        }
                        saveFile(fullPath, String.valueOf(myInt));
                        numberString = readFile(fullPath);
                        Toast.makeText(getActivity(), numberString, Toast.LENGTH_SHORT).show();

                        //ImageView imageView = (ImageView) getView().findViewById(R.id.imageView1);

                        String numberStringRes = readFile(fullPath);
                        int number = Integer.parseInt(numberStringRes);
                        String lesson = "geography", grage = "10";
                        //* написать метод получение названия и класса (это после реализации библиотеки)
                        String name = lesson + grage + "_"+ number;

                        int holderInt = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());

                        //imageView.setImageResource(holderInt);
                    }
                    catch (NumberFormatException e){
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onSwipeLeft() {
                //обработка свайпа влево
                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AntiTextBook/ATB/numberOfPictures.txt";
                String numberString = readFile(fullPath);
                try {
                    int myInt = Integer.parseInt(numberString);
                    myInt++;
                    saveFile(fullPath, String.valueOf(myInt));
                    numberString = readFile(fullPath);
                    Toast.makeText(getActivity(), numberString, Toast.LENGTH_SHORT).show();

                    //ImageView imageView = (ImageView) getView().findViewById(R.id.imageView1);

                    String numberStringRes = readFile(fullPath);
                    int number = Integer.parseInt(numberStringRes);
                    String lesson = "geography", grage = "10";
                    String name = lesson + grage + "_"+ number;

                    int holderInt = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());

                    //imageView.setImageResource(holderInt);
                }
                catch (NumberFormatException e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        } );


        // устанавливаем картинку в самом начале только один раз (при свайпах к этому уже не обращаемся)
*/
    }

    // Функция, которая проверяет, доступно ли external storage(cd-card) для чтения и записи
    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    //Функция, которая сохраняет файл, принимая полный путь до файла filePath и сохраняемый текст FileContent (так же используется для создания файла)
    //* написать функцию подобно этой, но без перезаписывания файла
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Error")
                    .setMessage(e.getMessage())
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

    // Функция, которая читает файл по определенному адресу, возвращает то, что в файле (Если там более одного слова, то вернет все "слипшееся". В идеале заменить строку на массив (список),
    // а то функцию почти нигде не получится использовать)
    //* Переписать ее для того, чтобы можно было использовать свой адресс (а не встроенный)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String readFile (String path){
        File sdcard = Environment.getExternalStorageDirectory();
        //получает текстовый файл
        File file = new File(sdcard,path);
        //читаем текстовый файл
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder resultText = new StringBuilder();

            while ((line = br.readLine()) != null) {
                resultText.append(line);
            }
            br.close();
            return resultText.toString();
        }
        catch (IOException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Error")
                    .setMessage(e.getMessage())
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
        return null;
    }

}
