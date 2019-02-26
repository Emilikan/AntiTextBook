package com.example.antitextbook;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.TreeSet;

import static com.example.antitextbook.Constants.a0;
import static com.example.antitextbook.Constants.a10;
import static com.example.antitextbook.MainActivity.fragmentIs;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
                editor.putString("openBook", mLastBook.get(position));
                editor.apply();

                Fragment fragment = new Home();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentIs = a10;
            }
        });

        favoriteBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                pdfUri = arrPdfUri.get(position);


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("URI", "content://com.android.providers.downloads.documents/document/1328");
                editor.putString("openBook", mBooks.get(position));

                editor.apply();

                Fragment fragment = new Home();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentIs = a10;
            }
        });


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        arrPdfUri.addAll(Objects.requireNonNull(preferences.getStringSet("FavoriteBookUri", new LinkedHashSet<String>())));
        for (String i:arrPdfUri){
            mBooks.add(preferences.getString(i,""));
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
                //Toast.makeText(getContext(), "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mLastBook.add(preferences.getString("lastBook",""));
            arrPdfUriLB.add(preferences.getString("lastBookPDFUri",""));
            ArrayAdapter adapter1 = new ArrayAdapter<>(getActivity(), R.layout.list_text_view,mLastBook);
            lastBook.setAdapter(adapter1);
        }
    }

}
