package com.example.antitextbook;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;
import static com.example.antitextbook.Constants.a0;
import static com.example.antitextbook.Constants.a21;
import static com.example.antitextbook.MainActivity.fragmentIs;

/**
 * Получаем книги из определенной папки и обрабатываем нажатие на них
 * + сокачанные книги
 */

public class Storage extends Fragment {
    private static final int PICK_PDF_REQUEST = 345;

    private LinearLayout linearLayout;
    private Button choosePdf;
    private ListView listBooks;
    private String pdfUri;
    private Set<String> arrPdfUriLibraryBooks = new LinkedHashSet<>();



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
        final View rootView = inflater.inflate(R.layout.fragment_storage, container, false);

        // для смены тема
        linearLayout = rootView.findViewById(R.id.storage);
        choosePdf = rootView.findViewById(R.id.buttonChooseBook);
        listBooks = rootView.findViewById(R.id.booksListView2);
        final ImageView comeBack = rootView.findViewById(R.id.back4);






        updateUI();
        setTheme();

         // обработчик на ListView
        listBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                pdfUri = arrPdfUri.get(position);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("URI", pdfUri);
                editor.putString("openBook", mBooks.get(position));
                editor.apply();

                Fragment fragment = new Home();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                fragmentIs = a21;
            }
        });


        // картинка-кнопка назад
        comeBack.setOnClickListener(new View.OnClickListener() {
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
                fragmentIs = a0;
            }
        });

        //кнопка выбора pdf на устройстве
        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfChooser();
            }
        });

        // получаем все файлы, которые есть в папке загрузок
        File rootFolder = Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        assert rootFolder != null;
        final File[] filesArray = rootFolder.listFiles();

        for (File f: filesArray) {
            mBooks.add(f.getName());
            arrPdfUri.add(f.toURI() + "");
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        arrPdfUri.addAll(Objects.requireNonNull(preferences.getStringSet("UriLibraryBook", new LinkedHashSet<String>())));
        for (String i :Objects.requireNonNull(preferences.getStringSet("UriLibraryBook", new LinkedHashSet<String>()))){
            mBooks.add(preferences.getString(i, ""));
        }
        return rootView;
    }
    // метод, который открывает проводник для выбора pdf
    private void pdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    // метод обновления информации в ListView
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

            choosePdf.setBackgroundResource(R.drawable.dark_cards);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            final Uri filePdfPath = data.getData();
            final String nameOfFile = getNameOfFile(filePdfPath);
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            final SharedPreferences.Editor editor = preferences.edit();

            editor.putString("URI", String.valueOf(filePdfPath));
            editor.putString("openBook",nameOfFile);
            arrPdfUriLibraryBooks.addAll(Objects.requireNonNull(preferences.getStringSet("UriLibraryBook", new LinkedHashSet<String>())));
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Warning")
                    .setMessage("Хотите добавить эту книгу в библиотеку?")
                    .setCancelable(false)
                    .setPositiveButton("Да",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    arrPdfUriLibraryBooks.add(filePdfPath+"");
                                    editor.putString(filePdfPath+"",nameOfFile);
                                    editor.putStringSet("UriLibraryBook", arrPdfUriLibraryBooks);
                                    editor.apply();
                                    dialog.cancel();
                                }
                            })
                    .setNegativeButton("Нет",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            if (!Objects.requireNonNull(preferences.getStringSet("UriLibraryBook", new LinkedHashSet<String>())).contains(filePdfPath+"")){
                alert.show();
            }

            editor.apply();


            Fragment fragment = new Home();
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            fragmentIs = a21;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getNameOfFile(Uri uri) {
        String displayName = null;
        Cursor cursor = getActivity().getContentResolver()
                .query(uri, null, null, null, null, null);

        ContentResolver ct = getContext().getContentResolver();
        ct.takePersistableUriPermission(uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            }
        } finally {
            cursor.close();
        }
        return displayName;
    }




    @Override
    public void onDetach() {
        super.onDetach();
    }


}