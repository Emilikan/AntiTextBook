package com.example.antitextbook;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
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

    private FrameLayout frameLayout;
    private ImageView imageView;
    private Button help;
    private Button choosePdf;
    private Button chooseImg;
    private Button sendOnCloud;

    private Uri filePath = null;
    private Uri filePdfPath = null;

    private String pdfUri;
    private String imgUri;

    private String mAuthor;
    private String mClass;
    private String mYear;
    private String mSubject;
    private String mPart;
    private String mDescribing;

    private int counterFor = 0;
    private String dbCounter;

    private DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);

        // подписываем пользователя на тему (для получаения push уведомлений)
        FirebaseMessaging.getInstance().subscribeToTopic("ForAllUsers1");

        if(!isOnline(Objects.requireNonNull(getContext()))){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Warning")
                    .setMessage("Нет доступа в интернет. Проверьте наличие связи")
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

        frameLayout = rootView.findViewById(R.id.cloud);
        choosePdf = rootView.findViewById(R.id.buttonChoosePDF);
        chooseImg = rootView.findViewById(R.id.buttonDownloadImage);
        imageView = rootView.findViewById(R.id.checkImage);
        help = rootView.findViewById(R.id.buttonHelp);
        sendOnCloud = rootView.findViewById(R.id.buttonSendToCloud);
        setTheme();

        //Кнопка отправить
        sendOnCloud.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mAuthor = ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.textAuthorCloud)).getText().toString();
                mClass = ((EditText) getActivity().findViewById(R.id.textClassCloud)).getText().toString();
                mYear = ((EditText) getActivity().findViewById(R.id.textYearCloud)).getText().toString();
                mSubject = ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).getText().toString();
                mPart = ((EditText) getActivity().findViewById(R.id.textPartCloud)).getText().toString();
                mDescribing = ((EditText) getActivity().findViewById(R.id.describingBook)).getText().toString();

                if("".equals(mAuthor)|| "".equals(mClass)|| "".equals(mYear) || "".equals(mSubject) || "".equals(mPart) || "".equals(mDescribing)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Предупреждение")
                            .setMessage("Одно из полей не заполненно. Пожалуйста, заполните все поля и повторите отправку")
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
                else if(filePath == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Предупреждение")
                            .setMessage("Не выбран файл загрузки")
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
                else{
                    // код отправки на сервер
                    pdfUri = "gs://antitextbook.appspot.com/" + mSubject + "_" + mClass + "_" + mAuthor + "_" + mDescribing + "_" + mPart + "_" + mYear + "/"  + mSubject +
                            "_" + mClass + "_" + mAuthor + "_" + mPart + "_" + mYear+"_pdf"; // путь до учебника
                    imgUri = "gs://antitextbook.appspot.com/images/" + mSubject + "_" + mClass + "_" + mAuthor + "_" + mPart + "_" + mYear +"_img"; // путь до обложки

                    saveDataToDatabase();
                    uploadFile(pdfUri, filePdfPath);
                    uploadFile(imgUri, filePath);
                    counterFor = 1;
                }
            }
        });

        //Кнопка загрузки pdf
        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdfChooser();
            }
        });

        //Кнопка загрузки Изображения
        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        // кнопка помощи
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Info")
                        .setMessage("Для загрузки:\n1) выберите файл книги в pdf формате;\n2) выберите изображение обложки (скриншот);\n3) заполните все поля, начиная каждое поле со слова с большой буквы;\n4) нажмите кнопку 'отправить'.\n\nПри возникновении вопросов: напишите в службу поддержки, указав данные, по которым админ сможет с вами связаться (почта).")
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

        return rootView;
    }

    private void saveDataToDatabase(){
        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(counterFor == 1) {
                    dbCounter = dataSnapshot.child("counter").getValue(String.class);
                    // Toast.makeText(getActivity(), dbCounter, Toast.LENGTH_SHORT).show();
                    assert dbCounter != null;
                    int intCounter = Integer.parseInt(dbCounter);
                    intCounter++;
                    String stringCounter = Integer.toString(intCounter);

                    mRef.child("Books").child(stringCounter).child("Class").setValue(mClass);
                    mRef.child("Books").child(stringCounter).child("Author").setValue(mAuthor);
                    mRef.child("Books").child(stringCounter).child("Year").setValue(mYear);
                    mRef.child("Books").child(stringCounter).child("Subject").setValue(mSubject);
                    mRef.child("Books").child(stringCounter).child("Part").setValue(mPart);
                    mRef.child("Books").child(stringCounter).child("Pdf").setValue(pdfUri);
                    mRef.child("Books").child(stringCounter).child("Icon").setValue(imgUri);
                    mRef.child("Books").child(stringCounter).child("Describing").setValue(mDescribing);
                    mRef.child("Books").child(stringCounter).child("TopDownloads").setValue("0");
                    mRef.child("Books").child(stringCounter).child("TopViews").setValue("0");
                    mRef.child("Books").child(stringCounter).child("UserTop").setValue("0");
                    mRef.child("Books").child(stringCounter).child("School").setValue("0");
                    mRef.child("Books").child(stringCounter).child("ThisCounter").setValue(stringCounter);

                    mRef.child("counter").setValue(stringCounter);
                    mRef.child("AllBooks").child(stringCounter).setValue(mSubject + " " + mAuthor + " " + mDescribing + " " + mClass);

                    counterFor = 0;
                    Toast.makeText(getActivity(), "Загружено", Toast.LENGTH_SHORT).show();
                }
            }
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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

    private void pdfChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    private void imageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

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
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setTitle("Error")
                        .setMessage(e.getMessage())
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
        }
        else if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePdfPath = data.getData();
            Toast.makeText(getContext(), "Pdf файл выбран", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void uploadFile(String path, Uri pathOfFile) {
        if (pathOfFile != null) {
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
                            ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.textAuthorCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textClassCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textYearCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).setText("");
                            ((EditText) getActivity().findViewById(R.id.textPartCloud)).setText("");
                            imageView.setImageDrawable(null);
                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                            builder.setTitle("Error")
                                    .setMessage(exception.getMessage())
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
        // если нет файлов
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Error")
                    .setMessage("Нет файлов")
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
    }

    // проверка на доступ интернета
    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);
            chooseImg.setBackgroundResource(R.drawable.dark_cards);
            choosePdf.setBackgroundResource(R.drawable.dark_cards);
            help.setBackgroundResource(R.drawable.dark_cards);
        }
    }

}
