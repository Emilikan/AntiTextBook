package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AboutBook extends Fragment {

    private TextView mPart2;
    private TextView mAuthor2;
    private TextView mProject2;
    private TextView mClass2;
    private TextView mYear2;
    private ImageView imageView;


    public String conterOfFragment;
    private File localFile = null;
    private Uri pdfFilePath = null;

    private DatabaseReference mRef;


    FirebaseAuth mAuth;
    FirebaseUser mUser;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
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

        imageView = (ImageView) rootView.findViewById(R.id.imageView3);
        mPart2 = (TextView) rootView.findViewById(R.id.Part2);
        mAuthor2 = (TextView) rootView.findViewById(R.id.Author2);
        mProject2 = (TextView) rootView.findViewById(R.id.Project2);
        mClass2 = (TextView) rootView.findViewById(R.id.Class2);
        mYear2 = (TextView) rootView.findViewById(R.id.Year2);

        Button upload2 = (Button) rootView.findViewById(R.id.upload2); // кнопка скачать книгу
        upload2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
            // скачивание книги

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("Books").child(conterOfFragment).child("Pdf").getValue(String.class)));

                        //localFile = getAlbumStorageDir(getApplicationContext(), "uploads1");
                        String nameOfFileInTelephone = dataSnapshot.child("Books").child(conterOfFragment).child("Author").getValue(String.class) + "_" +  dataSnapshot.child("Books").child(conterOfFragment).child("Class").getValue(String.class)
                                + "_" + dataSnapshot.child("Books").child(conterOfFragment).child("Subject").getValue(String.class) + "_" + dataSnapshot.child("Books").child(conterOfFragment).child("Part").getValue(String.class)
                                + "_" + dataSnapshot.child("Books").child(conterOfFragment).child("Year").getValue(String.class);

                        localFile = saveFile(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "download/" + nameOfFileInTelephone + ".pdf");

                        assert localFile != null;
                        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getContext(), "Файл скачан", Toast.LENGTH_LONG).show();
                                // в переменной pdfFilePath хранится Uri скаченного файла. Передавать его в Home.class, записывать его в файл настроек или SharedPreferences
                                pdfFilePath = Uri.parse(localFile.toURI()+"");
                                // showPdf();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Toast.makeText(getContext(), exception.getMessage() , Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        changeText();

        return rootView;
    }

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
            FileOutputStream fOut = new FileOutputStream(fileHandle);
            return fileHandle;
        }
        catch (IOException e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return fileHandle;
    }



}
