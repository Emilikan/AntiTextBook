package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class AboutBook extends Fragment {
    private TextView mPart2;
    private TextView mAuthor2;
    private TextView mProject2;
    private TextView mClass2;
    private TextView mYear2;
    private ImageView imageView;

    public String conterOfFragment;

    private DatabaseReference mRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            @Override
            public void onClick(View v) {
            // скачивание книги
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
                //dbCounter = dataSnapshot.child("counter").getValue(String.class);
                //Toast.makeText(getActivity(), dbCounter, Toast.LENGTH_SHORT).show();

                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageRef = storage.getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("Books").child(conterOfFragment).child("Icon").getValue(String.class)));

                // создаем ссылку на файл по адресу scoin.png
                // вызываем getDownloadUrl() и устанавливаем слушатель успеха,
                // который срабатывает в случае успеха процесса скачивания
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getContext()).load(uri).into(imageView);  //ВАШ IMAGEVIEW,без id работать не будет
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
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error" + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });



    }

}
