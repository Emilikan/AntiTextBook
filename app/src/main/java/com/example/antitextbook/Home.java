package com.example.antitextbook;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        String folderName = "AntiTextBook/ATB", fileName = "numberOfPictures.txt";// название файла, где хранится номер данной картинки

        String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + fileName;
        String folderName1 = "AntiTextBook/ATB/settings", fileName1 = "darkBox.txt";
        //String dark = readTxtFile(fullPath1);

        File file = new File(fullPath);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uriOfPdf = preferences.getString("URI", "");
        assert uriOfPdf != null;
        if(!uriOfPdf.equalsIgnoreCase(""))
        {
            uriOfPdf = uriOfPdf + "";  /* Edit the value here*/
        }

        // открытие pdf файла
        PDFView pdfView = rootView1.findViewById(R.id.pdfView);
        /*if("TRUE".equals(dark)) {
            pdfView.fromUri(Uri.parse(uriOfPdf))
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
        } else {
        */
            pdfView.fromUri(Uri.parse(uriOfPdf))
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
        //}

        // проверяем наличие cd-card
        if(!file.exists()) {
            if (isExternalStorageWritable()) {
                //saveFile(fullPath, String.valueOf(numberOfPicturesMin));
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

        /* String numberString = readTxtFile(fullPath);
        try {
            int number = Integer.parseInt(numberString);
            String lesson = "geography", grage = "10";
            String name = lesson + grage + "_"+ number;
            int holderInt = getResources().getIdentifier(name, "drawable", Objects.requireNonNull(getActivity()).getPackageName()); // для поиска id по названию. Крутаю вещь. Если ты не понимаешь, что это, то спроси у меня (Эмиля)
            //mImageView.setImageResource(holderInt); // используем это для изменения КАРТИНКИ
        }
        catch (NumberFormatException e){
        } */
        return rootView1;

    }

    // Функция, которая проверяет, доступно ли external storage(cd-card) для чтения и записи
    public boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
