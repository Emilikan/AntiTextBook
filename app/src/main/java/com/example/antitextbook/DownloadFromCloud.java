package com.example.antitextbook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DownloadFromCloud extends Fragment {
    private int STORAGE_PERMISSION_CODE = 23;

    private DatabaseReference mRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download_from_cloud, container, false);
        Button button4 = (Button) rootView.findViewById(R.id.search);

        button4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = Search.class;
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

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // проверка на доступ к памяти
                if(!isReadStorageAllowed()){
                    requestStoragePermission();
                }

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference gsReference = storage.getReferenceFromUrl("gs://antitextbook.appspot.com/Информатика. 10кл. Баз. уровень_Семакин и др_2015 -264с.pdf");
                String name = gsReference.getName();
                // проверка для себя. Чтобы убедиться, что нашел файл
                Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
                StorageReference islandRef = storageRef.child("images/island.jpg");

                /*final long ONE_MEGABYTE = 49739700;
                gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Toast.makeText(getActivity(), "Удачная загрузка", Toast.LENGTH_SHORT).show();
                    }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getActivity(), "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                        }
                    }); */
                File localFile = null;
                try {
                    localFile = File.createTempFile("Informat", "pdf");
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Ошибка", Toast.LENGTH_SHORT).show();
                }

                    gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getActivity(), "Удачная загрузка", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getActivity(), "Ошибка загрузки: " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            }


        });
        return rootView;
    }

    private void saveDataToDatabase(String book){
        mRef = FirebaseDatabase.getInstance().getReference(); // получаем ссылку на базу данных
        // прикрепляем слушателя
        /*mRef.child().addChildEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

            }

            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });*/
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                Toast.makeText(getContext(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

}