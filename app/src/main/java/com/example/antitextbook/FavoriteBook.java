package com.example.antitextbook;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class FavoriteBook extends Fragment {
    private ListView lastBook;


    private ListView favoriteBooks;
    private String pdfUri;
    private ArrayList<String> mLastBook = new ArrayList<>();
    private ArrayList<String> arrPdfUriLB = new ArrayList<>();
    private ArrayList<String> mBooks = new ArrayList<>();
    private ArrayList<String> arrPdfUri = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_book, container, false);
        favoriteBooks = rootView.findViewById(R.id.favoriteBooks);
        lastBook = rootView.findViewById(R.id.lastBook);

        updateUI();
        lastBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                pdfUri = arrPdfUriLB.get(position);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("URI", pdfUri);
                editor.apply();

                Fragment fragment = new Home();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        });
        favoriteBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                pdfUri = arrPdfUri.get(position);

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

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        for (File f: filesArray) {
            if ((f.getName()).equals(preferences.getString(f.getName(), ""))){
                mBooks.add(f.getName());
                arrPdfUri.add(f.toURI() + "");
            }
        }
        return rootView;
    }
    public void updateUI() {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_text_view, mBooks);
            favoriteBooks.setAdapter(adapter);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            try {
                mLastBook.remove(0);
            }
            catch (Exception e){

            }
            mLastBook.add(preferences.getString("lastBook",""));
            arrPdfUriLB.add(preferences.getString("lastBookPDFUri",""));
            ArrayAdapter adapter1 = new ArrayAdapter<>(getActivity(), R.layout.list_text_view,mLastBook);
            lastBook.setAdapter(adapter1);
        }
    }

}
