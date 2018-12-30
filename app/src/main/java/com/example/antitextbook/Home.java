package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Home extends Fragment {

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView1 = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String uriOfPdf = preferences.getString("URI", "");
        assert uriOfPdf != null;
        if(!uriOfPdf.equalsIgnoreCase(""))
        {
            uriOfPdf = uriOfPdf + "";
        }

        String dark = preferences.getString("Theme", "0");

        // открытие pdf файла
        PDFView pdfView = rootView1.findViewById(R.id.pdfView);
        if("TRUE".equals(dark)) {
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

        //setTitleName();
        return rootView1;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void setTitleName(){
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
            TextView mainTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.title_name);
            mainTitle.setText("Admin");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
