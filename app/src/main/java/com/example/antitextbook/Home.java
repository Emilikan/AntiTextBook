package com.example.antitextbook;

import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

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

        int numberOfPicturesMin = 0;
        ImageView mImageView;
        ScrollView scrollViewSwipe;
        String folderName = "temp/ATB", fileName = "numberOfPictures.txt";// название файла, где хранится номер данной картинки

        //scrollViewSwipe = (ScrollView) rootView1.findViewById(R.id.scrollView1);// получаем ScrollView для свайпов
        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;
        File file = new File(fullPath);
        String folderName1 = "temp/ATB/settings", fileName1 = "darkBox.txt";
        String fullPath1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName1 + "/" + fileName1;
        Toast.makeText(getActivity(),Environment.getExternalStorageDirectory().getAbsolutePath(), Toast.LENGTH_SHORT).show();
        String dark = readFile(fullPath1);
        // открытие pdf файла
        PDFView pdfView = rootView1.findViewById(R.id.pdfView);
        if(dark == "TRUE") {
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
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {


                        }
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
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {


                        }
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
                Toast.makeText(getActivity(), "Error: " + "не установлена cd-card. Для корректной работы приложения необходима cd-card", Toast.LENGTH_SHORT).show();
                //* поменять потом все на внутренний накопитель, ща чет не получилось и пошло оно все нахер
                //* а, и еще. Я ваще хз, куда сохраняется файл (у меня на телефоне он, как мне показалось, сохраняет на внутреннюю)
            }
        }
        String numberString = readFile(fullPath);
        try {
            int number = Integer.parseInt(numberString);
            String lesson = "geography", grage = "10";
            String name = lesson + grage + "_"+ number;
            int holderInt = getResources().getIdentifier(name, "drawable", getActivity().getPackageName()); // для поиска id по названию. Крутаю вещь. Если ты не понимаешь, что это, то спроси у меня (Эмиля)
            //mImageView.setImageResource(holderInt); // используем это для изменения КАРТИНКИ
        }
        catch (NumberFormatException e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            //mImageView.setImageResource(R.drawable.geography10_5);
        }
        //swipeImage(mImageView, scrollViewSwipe);
        return rootView1;
    }


    private void swipeImage(ImageView imageView, ScrollView scrollViewSwipe){

        //обработка свайпа (там же сменяем картинку)
        scrollViewSwipe.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
                public void onSwipeRight() {
                //обработка свайпа вправо
                    String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/ATB/numberOfPictures.txt";
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
                String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp/ATB/numberOfPictures.txt";
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
    //* написать функцию подобно этой, но без перезаписывания файла
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

    // Функция, которая читает файл по определенному адресу, возвращает то, что в файле (Если там более одного слова, то вернет все "слипшееся". В идеале заменить строку на массив (список),
    // а то функцию почти нигде не получится использовать)
    //* Переписать ее для того, чтобы можно было использовать свой адресс (а не встроенный)
    public String readFile (String path){
        File sdcard = Environment.getExternalStorageDirectory();
        //получает текстовый файл
        File file = new File(sdcard,path);
        //читаем текстовый файл
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            String resultText = "";

            while ((line = br.readLine()) != null) {
                resultText += line;
            }
            br.close();
            return resultText;
        }
        catch (IOException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}
