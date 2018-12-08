package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Storage extends Fragment {
    private ListView listBooks;
    private static final int PICK_PDF_REQUEST = 345;
    private ArrayList<String> mBooks = new ArrayList<String>();
    private ArrayList<String> arrPdfUri = new ArrayList<String>();
    private String pdfUri;

    private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_storage, container, false);

        linearLayout = rootView.findViewById(R.id.storage);
        setTheme();

        listBooks = (ListView) rootView.findViewById(R.id.booksListView2);
        listBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                pdfUri = arrPdfUri.get(position);

                //Toast.makeText(getContext(), pdfUri, Toast.LENGTH_LONG).show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("URI", pdfUri);
                editor.apply();

                Fragment fragment = new Home();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });

        File rootFolder = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        assert rootFolder != null;
        File[] filesArray = rootFolder.listFiles();

        int count = 0;
        for (File f: filesArray) {
            // возможно нужен count
            mBooks.add(f.getName());
            arrPdfUri.add(f.toURI() + "");

            //count++;
        }

        ImageView back = rootView.findViewById(R.id.back4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = Library.class;
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

        updateUI();
        Button choosePdf = rootView.findViewById(R.id.buttonChooseBook);//Кнопка загрузки pdf
        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfChooser();

                //Toast.makeText(getContext(), pdfUri, Toast.LENGTH_LONG).show();

            }
        });

        return rootView;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePdfPath = data.getData();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("URI", String.valueOf(filePdfPath));
            editor.apply();

            Fragment fragment = new Home();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
    }
    private void pdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }
    public void updateUI() {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_text_view, mBooks);
            listBooks.setAdapter(adapter);
        }
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            linearLayout.setBackgroundResource(R.drawable.dark_bg);

            //chooseText.setTextColor(R.color.colorDarkBlue);
            //downloadText.setTextColor(R.color.colorDarkText);
        }
    }

}