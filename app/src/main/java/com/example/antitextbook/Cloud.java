package com.example.antitextbook;

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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
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

    private String studentOrSchooler = "Ничего не выбранно";

    private String mAuthor;
    private String mClass;
    private String mYear;
    private String mSubject;
    private String mPart;
    private String mDescribing;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);

        // подписываем пользователя на тему (для получаения push уведомлений)
        FirebaseMessaging.getInstance().subscribeToTopic("ForAllUsers1");

        ImageView back = rootView.findViewById(R.id.back2444);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = AdminOfApp.class;
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

        if(!isOnline(Objects.requireNonNull(getContext()))){
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
            builder.setTitle("Warning")
                    .setMessage("Нет доступа в интернет. Проверьте наличие связи")
                    .setCancelable(false)
                    .setNegativeButton("Ок, закрыть",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    Fragment fragment = new Server();
                                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                    Toast.makeText(getActivity(), "Нет книг", Toast.LENGTH_SHORT).show();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {

            frameLayout = rootView.findViewById(R.id.cloud);
            choosePdf = rootView.findViewById(R.id.buttonChoosePDF);
            chooseImg = rootView.findViewById(R.id.buttonDownloadImage);
            imageView = rootView.findViewById(R.id.checkImage);
            help = rootView.findViewById(R.id.buttonHelp);
            sendOnCloud = rootView.findViewById(R.id.buttonSendToCloud);
            setTheme();

            RadioGroup radioGroup = rootView.findViewById(R.id.radioGroup2);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case -1:
                            studentOrSchooler = "";
                            Toast.makeText(getActivity(), "Ничего не выбранно", Toast.LENGTH_LONG).show();
                            break;
                        case R.id.instituteInCloud:
                            studentOrSchooler = "ForStudent";
                            break;
                        case R.id.schoolInCloud:
                            studentOrSchooler = "ForSchoolBoy";
                            break;
                    }
                }
            });

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

                    if ("".equals(mAuthor) || "".equals(mClass) || "".equals(mYear) || "".equals(mSubject) || "".equals(mPart) || "".equals(mDescribing)) {
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
                    } else if (filePath == null) {
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
                    } else if (Integer.parseInt(mClass) > 12) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        builder.setTitle("Предупреждение")
                                .setMessage("Класс не может быть установлен больше 12. Если у вас в школе больше 12 классов, то напишите в службу поддержки")
                                .setCancelable(false)
                                .setNegativeButton("Ок, закрыть",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (Integer.parseInt(mYear) > 2020 || Integer.parseInt(mYear) < 1980) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        builder.setTitle("Предупреждение")
                                .setMessage("Ошибка при установке даты. Дата слишком большая или слишком маленькая. Пожалуйста, установите правильную дату")
                                .setCancelable(false)
                                .setNegativeButton("Ок, закрыть",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else if (studentOrSchooler.equals("Ничего не выбранно")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                        builder.setTitle("Предупреждение")
                                .setMessage("Выберете, для кого эта книга: для студентов или для школьников")
                                .setCancelable(false)
                                .setNegativeButton("Ок, закрыть",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        sendOnCloud.setClickable(false);
                        // код отправки на сервер
                        pdfUri = "gs://antitextbook.appspot.com/" + mSubject + "_" + mClass + "_" + mAuthor + "_" + mDescribing + "_" + mPart + "_" + mYear + "/" + mSubject +
                                "_" + mClass + "_" + mAuthor + "_" + mDescribing + "_" + mPart + "_" + mYear + "_pdf"; // путь до учебника
                        imgUri = "gs://antitextbook.appspot.com/images/" + mSubject + "_" + mClass + "_" + mDescribing + "_" + mAuthor + "_" + mPart + "_" + mYear + "_img"; // путь до обложки

                        // изменяем в отдельном потоке бд
                        Thread changeDataInDataBase = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

                                final String mClassFlow = mClass;
                                final String mAuthorFlow = mAuthor;
                                final String mYearFlow = mYear;
                                final String mSubjectFlow = mSubject;
                                final String mPartFlow = mPart;
                                final String pdfUriFlow = pdfUri;
                                final String imgUriFlow = imgUri;
                                final String mDescribingFlow = mDescribing;
                                final Context context = getContext();
                                final String forWho = studentOrSchooler;

                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String dbCounter = dataSnapshot.child("counter").getValue(String.class);
                                            assert dbCounter != null;
                                            int intCounter = Integer.parseInt(dbCounter);
                                            intCounter++;
                                            String stringCounter = Integer.toString(intCounter);

                                            mRef.child("Books").child(stringCounter).child("Class").setValue(mClassFlow);
                                            mRef.child("Books").child(stringCounter).child("Author").setValue(mAuthorFlow);
                                            mRef.child("Books").child(stringCounter).child("Year").setValue(mYearFlow);
                                            mRef.child("Books").child(stringCounter).child("Subject").setValue(mSubjectFlow);
                                            mRef.child("Books").child(stringCounter).child("Part").setValue(mPartFlow);
                                            mRef.child("Books").child(stringCounter).child("Pdf").setValue(pdfUriFlow);
                                            mRef.child("Books").child(stringCounter).child("Icon").setValue(imgUriFlow);
                                            mRef.child("Books").child(stringCounter).child("Describing").setValue(mDescribingFlow);
                                            mRef.child("Books").child(stringCounter).child("TopDownloads").setValue("0");
                                            mRef.child("Books").child(stringCounter).child("UserTop").setValue("0");
                                            mRef.child("Books").child(stringCounter).child("SchoolCounter").setValue("-1");
                                            mRef.child("Books").child(stringCounter).child("School").setValue("0");
                                            mRef.child("Books").child(stringCounter).child("ForWho").setValue(forWho);
                                            mRef.child("Books").child(stringCounter).child("ThisCounter").setValue(stringCounter);

                                            mRef.child("counter").setValue(stringCounter);

                                            Toast.makeText(context, "Загружено в базу данных", Toast.LENGTH_SHORT).show();

                                    }
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
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
                        });
                        changeDataInDataBase.start();

                        // загружаем в отдельном потоке pdf файл
                        Thread uploadPdf = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Uri filePdfPathInFlow = filePdfPath;
                                String pdfUriInFlow = pdfUri;
                                final Context context = getContext();

                                if (filePdfPathInFlow != null) {
                                    //final ProgressDialog progressDialog = new ProgressDialog(context);
                                    //progressDialog.setTitle("Загрузка");
                                    //progressDialog.show();


                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference riversRef = storage.getReferenceFromUrl(pdfUriInFlow);// путь на облаке, куда загружается файл, im - название файла на облаке
                                    // в переменной типа Uri filePath хранится путь на устройстве до загружаемого файла
                                    riversRef.putFile(filePdfPathInFlow)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    //*progressDialog.dismiss();
                                                    //*filePdfPath = null;

                                                    // проверяем, вышел ли пользователь из активити. Если пользователь вышел, то все обнулится в onDetach
                                                    if(getActivity() != null) {
                                                        sendOnCloud.setClickable(true);
                                                        Toast.makeText(context, "PDF файл загружен ", Toast.LENGTH_SHORT).show();
                                                        // обнуляем все EditText
                                                        ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.textAuthorCloud)).setText("");
                                                        ((EditText) getActivity().findViewById(R.id.textClassCloud)).setText("");
                                                        ((EditText) getActivity().findViewById(R.id.textYearCloud)).setText("");
                                                        ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).setText("");
                                                        ((EditText) getActivity().findViewById(R.id.textPartCloud)).setText("");
                                                        ((EditText) getActivity().findViewById(R.id.describingBook)).setText("");

                                                        filePath = null;
                                                        filePdfPath = null;
                                                    }
                                                    else{
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
                                                        builder.setTitle("Информация")
                                                                .setMessage("PDF файл загружен")
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
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    //*progressDialog.dismiss();
                                                    if(getActivity() != null){
                                                        sendOnCloud.setClickable(true);
                                                    }

                                                    AlertDialog.Builder ad;
                                                    ad = new AlertDialog.Builder(Objects.requireNonNull(context));
                                                    ad.setTitle("Error");  // заголовок
                                                    ad.setMessage("Ошибка: " + exception.getMessage() + ""); // сообщение
                                                    ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int arg1) {
                                                            Fragment fragment = new Send();
                                                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                                        }
                                                    });
                                                    ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int arg1) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    ad.setCancelable(true);
                                                    ad.show();
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // вычисление процентов скачивания
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                    // отображение progressDialog c процентами

                                                    //*progressDialog.setMessage("Загрузка " + ((int) progress) + "%...");
                                                }
                                            });
                                }
                                // если нет файлов
                                else {
                                    if(getActivity() != null){
                                        sendOnCloud.setClickable(true);
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
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
                        });
                        uploadPdf.start();

                        // загружаем в отдельном потоке img файл
                        Thread uploadImg = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Uri filePathInFlow = filePath;
                                String imgUriInFlow = imgUri;

                                final Context context = getContext();

                                if (filePathInFlow != null) {
                                    //*final ProgressDialog progressDialog = new ProgressDialog(getContext());
                                    //*progressDialog.setTitle("Загрузка");
                                    //*progressDialog.show();

                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference riversRef = storage.getReferenceFromUrl(imgUriInFlow);// путь на облаке, куда загружается файл, im - название файла на облаке
                                    // в переменной типа Uri filePath хранится путь на устройстве до загружаемого файла
                                    riversRef.putFile(filePathInFlow)
                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    //*progressDialog.dismiss();

                                                    // проверяем, вышел ли пользователь из активити
                                                    if(getActivity()!=null){
                                                        imageView.setImageDrawable(null);
                                                    }
                                                    Toast.makeText(context, "Файл изображния загружен ", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    //*progressDialog.dismiss();

                                                    AlertDialog.Builder ad;
                                                    ad = new AlertDialog.Builder(Objects.requireNonNull(context));
                                                    ad.setTitle("Error");  // заголовок
                                                    ad.setMessage("Ошибка: " + exception.getMessage() + ""); // сообщение
                                                    ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int arg1) {
                                                            Fragment fragment = new Send();
                                                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                                        }
                                                    });
                                                    ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int arg1) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    ad.setCancelable(true);
                                                    ad.show();
                                                }
                                            })
                                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                    // получаем проценты загрузки
                                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                    // отображаем диалог с процентами

                                                    //*progressDialog.setMessage("Загрузка " + ((int) progress) + "%...");
                                                }
                                            });
                                }
                                // если нет файлов
                                else {
                                    AlertDialog.Builder ad;
                                    ad = new AlertDialog.Builder(Objects.requireNonNull(context));
                                    ad.setTitle("Error");  // заголовок
                                    ad.setMessage("Ошибка: нет файлов \nнапишите в службу поддержки"); // сообщение
                                    ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            Fragment fragment = new Send();
                                            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                        }
                                    });
                                    ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int arg1) {
                                            dialog.cancel();
                                        }
                                    });
                                    ad.setCancelable(true);
                                    ad.show();
                                }
                            }
                        });
                        uploadImg.start();
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
                            .setMessage("Для загрузки:\n1) выберите файл книги в pdf формате;\n2) выберите изображение обложки (скриншот);\n" +
                                    "3) заполните все поля, начиная каждое поле со слова с большой буквы;\n5) описание заполнять не обязательно." +
                                    " В описание пишем продолжение названия учебника (например, у нас есть книга: 'М.М. Астахов Конспект ленций по электричеству'." +
                                    " В описание мы пишем 'Конспект лекций по электричеству')\n4) нажмите кнопку 'отправить'.\n\n" +
                                    "При возникновении вопросов: напишите в службу поддержки, указав данные, по которым админ сможет с вами связаться (почта).")
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

        return rootView;
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

                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                ad.setTitle("Error");  // заголовок
                ad.setMessage("Ошибка: " + e.getMessage()); // сообщение
                ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        Fragment fragment = new Send();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    }
                });
                ad.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                ad.setCancelable(true);
                ad.show();
            }
        }
        else if(requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePdfPath = data.getData();
            Toast.makeText(getContext(), "Pdf файл выбран", Toast.LENGTH_LONG).show();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDetach() {
        super.onDetach();

        /*((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.textAuthorCloud)).setText("");
        ((EditText) getActivity().findViewById(R.id.textClassCloud)).setText("");
        ((EditText) getActivity().findViewById(R.id.textYearCloud)).setText("");
        ((EditText) getActivity().findViewById(R.id.textSubjectCloud)).setText("");
        ((EditText) getActivity().findViewById(R.id.textPartCloud)).setText("");
        ((EditText) getActivity().findViewById(R.id.describingBook)).setText("");

        filePath = null;
        filePdfPath = null;
        */
    }

}
