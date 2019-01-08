package com.example.antitextbook;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.FrameLayout;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;
import static java.net.HttpURLConnection.HTTP_OK;


public class ViewBooksForChecking extends Fragment {
    public String counterOfFragment;

    private String bookAuthor;
    private String bookClass;
    private String bookDescribing;
    private String bookIcon;
    private String bookPart;
    private String bookPdf;
    private String bookSchool;
    private String bookSchoolCounter;
    private String bookSubject;
    private String bookYear;
    private String bookForWho;

    private TextView mPart2;
    private TextView mAuthor2;
    private TextView mProject2;
    private TextView mClass2;
    private TextView mYear2;
    private TextView mDescribing;
    private TextView mSchool;
    private ImageView imageView;
    private FrameLayout frameLayout;

    private Uri pdfFilePath = null;

    private DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_books_for_checking, container, false);

        ImageView back = rootView.findViewById(R.id.back3ForAdmin1); // кнопка назад
        back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = ChooseTrueBooks.class;
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

                                    Fragment fragment = new AdminOfApp();
                                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                    Toast.makeText(getActivity(), "Нет книг", Toast.LENGTH_SHORT).show();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            Bundle bundle = getArguments();
            counterOfFragment = "0";
            if (bundle != null) {
                counterOfFragment = bundle.getString("Value", "0");
            }
            frameLayout = rootView.findViewById(R.id.viewBooksForAdmin);
            imageView = rootView.findViewById(R.id.imageView6);
            mPart2 = rootView.findViewById(R.id.Part2ForAdmin);
            mAuthor2 = rootView.findViewById(R.id.Author2ForAdmin);
            mProject2 = rootView.findViewById(R.id.Project2ForAdmin);
            mDescribing = rootView.findViewById(R.id.describingAboutBookForAdmin);
            mClass2 = rootView.findViewById(R.id.Class2ForAdmin);
            mYear2 = rootView.findViewById(R.id.Year2ForAdmin);
            mSchool = rootView.findViewById(R.id.SchoolForAdmin);

            setTheme();
            changeText();

            Button confirmed = rootView.findViewById(R.id.addBook); // кнопка добавления книги в основную базу
            confirmed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBook();
                }
            });

            Button didntConfirm = rootView.findViewById(R.id.deleteBook); // кнопка удаления книги
            didntConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBook();
                }
            });

            Button download = rootView.findViewById(R.id.downloadForChecking); // кнопка скачивания книги
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    downloadBook();
                }
            });
        }
        return rootView;
    }

    // изменяем текст на тот, который получили из бд
    @SuppressLint("WrongViewCast")
    public void changeText() {
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Icon").getValue(String.class)));
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getContext()).load(uri).into(imageView);
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Toast toast = Toast.makeText(getContext(), "Ошибка!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                mPart2.setText("Часть: " + dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Part").getValue(String.class));
                mAuthor2.setText("Автор: " +dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Author").getValue(String.class));
                mProject2.setText("Предмет: " +dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Subject").getValue(String.class));
                mClass2.setText("Класс: " + dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Class").getValue(String.class));
                mYear2.setText("Год: " + dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Year").getValue(String.class));
                mDescribing.setText("Описание: " + dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Describing").getValue(String.class));
                mSchool.setText("Школа/человек, выложивший эту книгу: " + dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("School").child("0").getValue(String.class));
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

    // метод переноса записей из ветки forChecking в основную ветку бд
    private void addBook(){

        // сохраняем все значения локально в SharedPreference
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String email = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("schoolEmail").getValue(String.class);
                String school = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("School").child("0").getValue(String.class);
                String message = "   Команда разработчиков приняла вашу заявку на добавление книги на сервер." +
                        " Теперь вашу книгу смогут увидеть учащиеся вашего (и не только) учебного заведения. По всем вопросам обращайтесь в службу поддержки через мобильное приложение";

                sendEmail(email, school, message, getContext());

                bookAuthor = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Author").getValue(String.class);
                bookClass = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Class").getValue(String.class);
                bookDescribing = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Describing").getValue(String.class);
                bookIcon = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Icon").getValue(String.class);
                bookPart = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Part").getValue(String.class);
                bookPdf = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Pdf").getValue(String.class);
                bookSchool = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("School").child("0").getValue(String.class);
                bookSchoolCounter = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("SchoolCounter").getValue(String.class);
                bookSubject = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Subject").getValue(String.class);
                bookYear = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("Year").getValue(String.class);
                bookForWho = dataSnapshot.child("forChecking").child("Books").child(counterOfFragment).child("ForWho").getValue(String.class);

                String dbCountForCheck = dataSnapshot.child("forChecking").child("counter").getValue(String.class);
                assert dbCountForCheck != null;
                int intCounterForCheck = Integer.parseInt(dbCountForCheck);
                int newCountForCheck = intCounterForCheck - 1;
                String newCounterFor = Integer.toString(newCountForCheck);

                String dbCounter = dataSnapshot.child("counter").getValue(String.class);
                assert dbCounter != null;
                int intCounter = Integer.parseInt(dbCounter);
                intCounter++;
                String stringCounter = Integer.toString(intCounter);

                // перемещаем в главную бд все записи
                mRef.child("Books").child(stringCounter).child("Author").setValue(bookAuthor);
                mRef.child("Books").child(stringCounter).child("Class").setValue(bookClass);
                mRef.child("Books").child(stringCounter).child("Describing").setValue(bookDescribing);
                mRef.child("Books").child(stringCounter).child("Icon").setValue(bookIcon);
                mRef.child("Books").child(stringCounter).child("Part").setValue(bookPart);
                mRef.child("Books").child(stringCounter).child("Pdf").setValue(bookPdf);
                mRef.child("Books").child(stringCounter).child("School").child("0").setValue(bookSchool);
                mRef.child("Books").child(stringCounter).child("SchoolCounter").setValue(bookSchoolCounter);
                mRef.child("Books").child(stringCounter).child("Subject").setValue(bookSubject);
                mRef.child("Books").child(stringCounter).child("Year").setValue(bookYear);
                mRef.child("Books").child(stringCounter).child("ForWho").setValue(bookForWho);
                mRef.child("Books").child(stringCounter).child("ThisCounter").setValue(stringCounter);
                mRef.child("Books").child(stringCounter).child("TopDownloads").setValue("0");
                mRef.child("Books").child(stringCounter).child("UserTop").setValue("0");
                mRef.child("counter").setValue(stringCounter);

                // удаляем запись
                mRef.child("forChecking").child("Books").child(counterOfFragment).setValue(null);
                mRef.child("forChecking").child("AllBooks").child(counterOfFragment).setValue(null);
                mRef.child("forChecking").child("counter").setValue(newCounterFor);

                Fragment fragment = null;
                Class fragmentClass;
                fragmentClass = ChooseTrueBooks.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                assert fragment != null;
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
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

    private void sendEmail (String email, String school, String message1, Context context){
        String subject = "Решение по заявке о добавлении книги на сервер";
        String message = "   Добого времени суток, представитель школы/вуза: " + school + ".\n" + message1 + "\n\n\nС уважением команда разработчиков приложения ATB.\nСпасибо, команда BHB.";

        SendMail sm = new SendMail(context, email, subject, message);
        sm.execute();
    }

    // метод удаления записей о книге из бд и файла с сервака
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    Thread deleteFile = new Thread(new Runnable() {
        @Override
        public void run() {
            final String flowCounterOfFragment = counterOfFragment;
            final Context context = getContext();

            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // получаем ссылки на файлы
                    String bookRefFromDb = dataSnapshot.child("forChecking").child("Books").child(flowCounterOfFragment).child("Pdf").getValue(String.class);
                    String imgRef = dataSnapshot.child("forChecking").child("Books").child(flowCounterOfFragment).child("Icon").getValue(String.class);

                    // получаем ссылки на хранилище
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference bookRefForDelete = storage.getReferenceFromUrl(Objects.requireNonNull(bookRefFromDb));
                    StorageReference imgRefForDelete = storage.getReferenceFromUrl(Objects.requireNonNull(imgRef));

                    // удалям pdf
                    bookRefForDelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "PDF файл удален", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
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
                    });

                    // удаляем img
                    imgRefForDelete.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "img файл удален", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
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
                    });

                    String email = dataSnapshot.child("forChecking").child("Books").child(flowCounterOfFragment).child("schoolEmail").getValue(String.class);
                    String school = dataSnapshot.child("forChecking").child("Books").child(flowCounterOfFragment).child("School").child("0").getValue(String.class);
                    String message = "   К сожалению, мы отклонили заявку на добавление вашей книги в наше приложение." +
                            " О причине отклонения вы можете узнать написав в службу поддержки в приложении, указав название предлагаемой вами книги в течении 5 дней с момента отправки этого письма";

                    sendEmail(email, school, message, context);

                    mRef.child("forChecking").child("Books").child(flowCounterOfFragment).setValue(null);

                    String dbCounter = dataSnapshot.child("forChecking").child("counter").getValue(String.class);
                    assert dbCounter != null;
                    int intCounter = Integer.parseInt(dbCounter);
                    intCounter--;
                    String stringCounter = Integer.toString(intCounter);

                    mRef.child("forChecking").child("counter").setValue(stringCounter);
                }

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
    });

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteBook(){
        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        ad.setTitle("Предупреждение");  // заголовок
        ad.setMessage("Вы уверены, что хотите удалить этот файл. После удаления его уже не востановить?"); // сообщение
        ad.setPositiveButton("Да, все равно удалить", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(DialogInterface dialog, int arg1) {
                deleteFile.start();
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        ad.setCancelable(true);
        ad.show();

    }

    // метод скачивания книги для проверки ее содержания админом
    private void downloadBook(){
        Thread download = new Thread(new Runnable() {
            @Override
            public void run() {
                final Context context = getContext();
                final String contFrag = counterOfFragment;

                final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

                // устанавливаем большее значение в топе загрузок
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String dbCounter = dataSnapshot.child("Books").child(contFrag).child("TopDownloads").getValue(String.class);
                        assert dbCounter != null;
                        int intCounter = Integer.parseInt(dbCounter);
                        intCounter++;
                        String stringCounter = Integer.toString(intCounter);
                        mRef.child("Books").child(contFrag).child("TopDownloads").setValue(stringCounter);
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

                // получаем данные только 1 раз (не следит за изменениями)
                // это сделано, чтобы не вылетало, когда в бд добавляются книги
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        StorageReference islandRef = FirebaseStorage.getInstance().getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("Books").child(contFrag).child("Pdf").getValue(String.class)));

                        String nameOfFileInTelephone = dataSnapshot.child("Books").child(contFrag).child("Author").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(contFrag).child("Describing").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(contFrag).child("Class").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(contFrag).child("Subject").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(contFrag).child("Part").getValue(String.class)
                                + " " + dataSnapshot.child("Books").child(contFrag).child("Year").getValue(String.class);

                        final File localFile = saveFile(Objects.requireNonNull(getContext()).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + nameOfFileInTelephone + ".pdf");

                        assert localFile != null;
                        final ProgressDialog progressDialog = new ProgressDialog(context);
                        progressDialog.setTitle("Downloading");
                        progressDialog.show();
                        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();

                                // в переменной pdfFilePath хранится Uri скаченного файла. Передавать его в Home.class, записывать его в файл настроек или SharedPreferences
                                if (localFile.toURI() != null) {
                                    pdfFilePath = Uri.parse(localFile.toURI() + "");

                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("URI", valueOf(pdfFilePath));
                                    editor.apply();
                                }

                                // тут у нас проверка на то, ушел ли пользователь с активити. Если не ушел, то мы сразу открываем книгу, если ушел, то оповещаем его
                                if(getActivity() != null) {
                                    Fragment fragment = new Home();
                                    FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                                    Toast.makeText(context, "Файл скачан", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(context));
                                    builder.setTitle("Информация")
                                            .setMessage("Файл скачан")
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
                        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // получаем проценты загрузки
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                // отображаем диалог с процентами
                                progressDialog.setMessage("Downloaded " + ((int) progress) + "%...");
                            }
                        });

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
        });
        download.start();
    }

    // метод создания пустого файла
    public File saveFile (String filePath)
    {
        //Создание объекта файла.
        File fileHandle = new File(filePath);
        try
        {
            //Если нет директорий в пути, то они будут созданы:
            if (!fileHandle.getParentFile().exists()) {
                fileHandle.getParentFile().mkdirs();
            }
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

    // проверка, есть ли инет
    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        // получаем значение из SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
