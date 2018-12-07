package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static java.lang.String.valueOf;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AboutBook extends Fragment {

    private TextView mPart2;
    private TextView mAuthor2;
    private TextView mProject2;
    private TextView mClass2;
    private TextView mYear2;
    private ImageView imageView;

    //private int i = 0;
    //private String dbCounter;

    public String conterOfFragment;
    private File localFile = null;
    private Uri pdfFilePath = null;

    private DatabaseReference mRef;

    private StorageReference islandRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_book, container, false);

        Bundle bundle = getArguments();
        conterOfFragment = "0";
        if(bundle != null){
            conterOfFragment = bundle.getString("Value", "0");
        }

        FirebaseMessaging.getInstance().subscribeToTopic("ForAllUsers1");

        imageView = rootView.findViewById(R.id.imageView3);
        mPart2 = rootView.findViewById(R.id.Part2);
        mAuthor2 = rootView.findViewById(R.id.Author2);
        mProject2 = rootView.findViewById(R.id.Project2);
        mClass2 = rootView.findViewById(R.id.Class2);
        mYear2 = rootView.findViewById(R.id.Year2);

        Button upload2 = rootView.findViewById(R.id.upload2); // кнопка скачать книгу
        upload2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
            // скачивание книги
                //i = 1;
                //changeTop();

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("Books").child(conterOfFragment).child("Pdf").getValue(String.class)));
                        //localFile = getAlbumStorageDir(getApplicationContext(), "uploads1");
                        String nameOfFileInTelephone = dataSnapshot.child("Books").child(conterOfFragment).child("Author").getValue(String.class) + " " +  dataSnapshot.child("Books").child(conterOfFragment).child("Class").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(conterOfFragment).child("Subject").getValue(String.class) + " " + dataSnapshot.child("Books").child(conterOfFragment).child("Part").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(conterOfFragment).child("Year").getValue(String.class);

                        localFile = saveFile(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + nameOfFileInTelephone + ".pdf");

                        assert localFile != null;
                        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                if(getActivity() != null && getContext() != null) {
                                    Toast.makeText(getActivity(), "Файл скачан", Toast.LENGTH_LONG).show();
                                    // в переменной pdfFilePath хранится Uri скаченного файла. Передавать его в Home.class, записывать его в файл настроек или SharedPreferences
                                    if(localFile.toURI() != null) {
                                        pdfFilePath = Uri.parse(localFile.toURI() + "");

                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("URI", valueOf(pdfFilePath));
                                        editor.apply();
                                    }
                                    localFile = null;
                                    pdfFilePath = null;
                                    islandRef = null;
                                    mRef = null;

                                    Fragment fragment = null;
                                    Class fragmentClass;
                                    fragmentClass = Home.class;
                                    try {
                                        fragment = (Fragment) fragmentClass.newInstance();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                    assert fragment != null;
                                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                }
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "ERROR" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ImageView back = rootView.findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = DownloadFromCloud.class;
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

        changeText();

        return rootView;
    }

    /*private void changeTop(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(i == 1){
                    dbCounter = dataSnapshot.child("Books").child(conterOfFragment).child("TopDownloads").getValue(String.class);
                    // Toast.makeText(getActivity(), dbCounter, Toast.LENGTH_SHORT).show();
                    assert dbCounter != null;
                    int intCounter = Integer.parseInt(dbCounter);
                    intCounter++;
                    String stringCounter = Integer.toString(intCounter);
                    mRef.child("Books").child(conterOfFragment).child("TopDownloads").setValue(stringCounter);
                    i = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    */


    @SuppressLint("WrongViewCast")
    public void changeText() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("Books").child(conterOfFragment).child("Icon").getValue(String.class)));
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getContext()).load(uri).into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast toast = Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

                mPart2.setText("Часть: " + dataSnapshot.child("Books").child(conterOfFragment).child("Part").getValue(String.class));
                mAuthor2.setText("Автор: " +dataSnapshot.child("Books").child(conterOfFragment).child("Author").getValue(String.class));
                mProject2.setText("Предмет: " +dataSnapshot.child("Books").child(conterOfFragment).child("Subject").getValue(String.class));
                mClass2.setText("Класс: " + dataSnapshot.child("Books").child(conterOfFragment).child("Class").getValue(String.class));
                mYear2.setText("Год: " + dataSnapshot.child("Books").child(conterOfFragment).child("Year").getValue(String.class));
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // создаем диологовое окно с ошибкой
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Error")
                        .setMessage(databaseError.getMessage())
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
        });

    }

    public File saveFile (String filePath)
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
            return fileHandle;
        }
        catch (IOException e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return fileHandle;
    }



}
