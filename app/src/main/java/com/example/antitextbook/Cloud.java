package com.example.antitextbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Cloud extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int PICK_PDF_REQUEST = 345;
    private Uri filePath = null;
    private Uri filePdfPath = null;
    private ImageView imageView;

    private String mAuthor;
    private String mClass;
    private String mYear;
    private String mSubject;
    private String mPart;

    private DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.checkImage);

        Button sendOnCloud = (Button) rootView.findViewById(R.id.buttonSendToCloud);//Кнопка отправить
        //* изначально заблокировать эту кнопку. Зfтем, после выгрузки изображения - разблокировать
        sendOnCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuthor = ((EditText) getActivity().findViewById(R.id.textAuthorCloud)).getText().toString();
                mClass = ((EditText) getActivity().findViewById(R.id.textClassCloud)).getText().toString();
                mYear = ((EditText) getActivity().findViewById(R.id.textYearCloud)).getText().toString();
                mSubject = ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).getText().toString();
                mPart = ((EditText) getActivity().findViewById(R.id.textPartCloud)).getText().toString();

                if("".equals(mAuthor)|| "".equals(mClass)|| "".equals(mYear) || "".equals(mSubject) || "".equals(mPart)) {
                    //* заменить на активити
                    Toast.makeText(getActivity(), "Одно из полей не заполненно. Пожалуйста, заполните все поля и повторите отправку", Toast.LENGTH_LONG).show();
                }
                else if(filePath == null){
                    //* заменить на активити
                    Toast.makeText(getActivity(), "Не выбран файл загрузки", Toast.LENGTH_LONG).show();
                }
                else{
                    // код отправки на сервер
                    String path = "gs://antitextbook.appspot.com/" + mSubject + "_" + mClass + "_" + mAuthor + "_" + mPart + "_" + mYear + "/"  + mSubject + "_" + mClass + "_" + mAuthor + "_" + mPart + "_" + mYear+"_pdf"; // путь до учебника
                    String name = mSubject + "_" + mClass + "_" + mAuthor + "_" + mPart + "_" + mYear;// так называется каталог в базе данных
                    String imagePath = "gs://antitextbook.appspot.com/images/" + mSubject + "_" + mClass + "_" + mAuthor + "_" + mPart + "_" + mYear +"_img"; // путь до обложки
                    saveDataToDatabase(name, path, imagePath);
                    uploadFile(path, filePdfPath);
                    uploadFile(imagePath, filePath);
                }
            }
        });

        Button choosePdf = (Button) rootView.findViewById(R.id.buttonChoosePDF);//Кнопка загрузки pdf
        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfChooser();
            }
        });

        Button choiceImages = (Button) rootView.findViewById(R.id.buttonDownloadImage);//Кнопка загрузки Изображения
        choiceImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        return rootView;
    }

    private void saveDataToDatabase(String name, String pdfUri, String imgUri){

        mRef = FirebaseDatabase.getInstance().getReference(); // получаем ссылку на базу данных
        // устанавливаем значение
        mRef.child(name).child("Class").setValue(mClass);
        mRef.child(name).child("Author").setValue(mAuthor);
        mRef.child(name).child("Year").setValue(mYear);
        mRef.child(name).child("Subject").setValue(mSubject);
        mRef.child(name).child("Part").setValue(mPart);
        mRef.child(name).child("Pdf").setValue(pdfUri);
        mRef.child(name).child("Icon").setValue(imgUri);
    }

    private void pdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), filePath);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                //* написать обработку ошибок
            }
        }
        else if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePdfPath = data.getData();
        }
    }

    private void uploadFile(String path, Uri pathOfFile) {
        //if there is a file to upload
        if (pathOfFile != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference riversRef = storage.getReferenceFromUrl(path);// путь на облаке, куда загружается файл, im - название файла на облаке
            // в переменной типа Uri filePath хранится путь на устройстве до загружаемого файла
            riversRef.putFile(pathOfFile)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            filePath = null;
                            filePdfPath = null;
                            ((EditText) getActivity().findViewById(R.id.textAuthorCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textClassCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textYearCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textPartCloud)).setText("");
                            imageView.setImageDrawable(null);
                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message

                            //* заменить на активити
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }



}
