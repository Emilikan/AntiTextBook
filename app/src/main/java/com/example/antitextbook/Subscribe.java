package com.example.antitextbook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Subscribe extends Fragment {

    private String nameOfSchool;

    private String counter = "-1";

    private Spinner spinnerSubject;
    private Spinner spinnerClass;
    private Spinner spinnerForWho;

    private ProgressBar progressBar;

    private List<BookForRecycle> books = new ArrayList<>();

    private String schoolOrInst = "School";
    private String forWho;
    private String subj;
    private String classOf;

    private ArrayList<String> nameOfSubj= new ArrayList<>(); // тут хронятся все виды предметов (для вывода их)
    private ArrayList<Integer> realIdOfBook= new ArrayList<>(); // тут под id в новом списке хранится настоящий id книги

    private RecyclerView recyclerView;

    private DatabaseReference mRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subscribe, container, false);

        progressBar = rootView.findViewById(R.id.progressBarInSubscribe);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // подписываем пользователя на тему (для получаения push уведомлений)
        FirebaseMessaging.getInstance().subscribeToTopic("ForAllUsers1");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        nameOfSchool = preferences.getString("dbSchool", "Ошибка");

        ImageView back = rootView.findViewById(R.id.back2348);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SchoolProfile();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
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
            spinnerSubject = rootView.findViewById(R.id.spinnerSubjectForSchool);
            spinnerClass = rootView.findViewById(R.id.spinnerClassForSchool);
            spinnerForWho = rootView.findViewById(R.id.forWhoForSchool);
            recyclerView = rootView.findViewById(R.id.listForSchool);

            mRef = FirebaseDatabase.getInstance().getReference();
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    counter = dataSnapshot.child("counter").getValue(String.class);
                    if ("-1".equals(counter)) {
                        Fragment fragment = new Server();
                        FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        Toast.makeText(getActivity(), "Нет книг", Toast.LENGTH_SHORT).show();
                    } else {
                        assert counter != null;
                        nameOfSubj.add("Все предметы");
                        for(int a = 0; a <= Integer.parseInt(counter); a++){
                            String school = dataSnapshot.child("Books").child(Integer.toString(a)).child("Subject").getValue(String.class);
                            if(!nameOfSubj.contains(school) && school != null){
                                nameOfSubj.add(school);
                            }
                        }
                        String[] arrayNameOfSubj = nameOfSubj.toArray(new String[0]); // массив со всеми предметами

                        progressBar.setVisibility(ProgressBar.INVISIBLE); // убираем прогресс бар

                        if(getActivity()!=null) {
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayNameOfSubj);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSubject.setAdapter(adapter1);
                        }

                        final String[] arrayForClass1 = {"Все классы", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
                        final String[] arrayForClass2 = {"Все курсы", "1", "2", "3", "4", "5"};

                        String[] arrayForWho = {"Школьник", "Студент"};

                        // какя-то шняга с тем, что вылетает, если не успевает подргузить бд (из-за плохого инета), а пользователь уходит с активити
                        if(getActivity()!=null) {
                            ArrayAdapter<String> adapter5 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayNameOfSubj);
                            adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSubject.setAdapter(adapter5);

                            if("School".equals(schoolOrInst)) {
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayForClass1);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerClass.setAdapter(adapter1);
                            } else {
                                ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayForClass2);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerClass.setAdapter(adapter1);
                            }

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayForWho);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerForWho.setAdapter(adapter2);
                        }

                        // обработчики спинеров
                        AdapterView.OnItemSelectedListener itemSelectedListenerForClass = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                classOf = (String)parent.getItemAtPosition(position);
                                sortingBook1();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        };
                        spinnerClass.setOnItemSelectedListener(itemSelectedListenerForClass);

                        AdapterView.OnItemSelectedListener itemSelectedListenerForWho = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String item = (String)parent.getItemAtPosition(position);

                                if(item.equals("Школьник")){
                                    schoolOrInst = "School";
                                    forWho = "ForSchoolBoy";
                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayForClass1);
                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerClass.setAdapter(adapter1);
                                }
                                else{
                                    schoolOrInst = "Inst";
                                    forWho = "ForStudent";
                                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, arrayForClass2);
                                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerClass.setAdapter(adapter1);
                                }
                                sortingBook1();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        };
                        spinnerForWho.setOnItemSelectedListener(itemSelectedListenerForWho);

                        AdapterView.OnItemSelectedListener itemSelectedListenerForSubj = new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                subj = (String)parent.getItemAtPosition(position);
                                sortingBook1();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        };
                        spinnerSubject.setOnItemSelectedListener(itemSelectedListenerForSubj);
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    AlertDialog.Builder ad;
                    ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    ad.setTitle("Error");  // заголовок
                    ad.setMessage("Ошибка: " + databaseError.getMessage() + "\n Проблемы на серверной части. Можете сообщить в службу поддержки"); // сообщение
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
            });
        }


        return rootView;
    }

    private void sortingBook1(){
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                books = new ArrayList<>();
                realIdOfBook = new ArrayList<>();
                final String counterOfBook = dataSnapshot.child("counter").getValue(String.class);
                assert counterOfBook != null;

                for(int a = 0; a <= Integer.parseInt(counterOfBook); a++){
                    String mSubj = dataSnapshot.child("Books").child(Integer.toString(a)).child("Subject").getValue(String.class);
                    String classOfBook = dataSnapshot.child("Books").child(Integer.toString(a)).child("Class").getValue(String.class);
                    String forWhoThisBook = dataSnapshot.child("Books").child(Integer.toString(a)).child("ForWho").getValue(String.class);

                    if(mSubj != null) {
                        if (subj.equals("Все предметы") && forWhoThisBook.equals(forWho) && (classOf.equals("Все классы") || classOf.equals("Все курсы"))) {
                            books.add(new BookForRecycle(dataSnapshot.child("Books").child(Integer.toString(a)).child("Author").getValue(String.class),
                                    mSubj, dataSnapshot.child("Books").child(Integer.toString(a)).child("Describing").getValue(String.class),
                                    classOfBook, Integer.toString(a), realIdOfBook, getContext(), getActivity(), nameOfSchool, true));
                            realIdOfBook.add(a);
                        } else if ((classOf.equals("Все классы")||classOf.equals("Все курсы")) && forWhoThisBook.equals(forWho) && subj.equals(mSubj)) {
                            books.add(new BookForRecycle(dataSnapshot.child("Books").child(Integer.toString(a)).child("Author").getValue(String.class),
                                    mSubj, dataSnapshot.child("Books").child(Integer.toString(a)).child("Describing").getValue(String.class),
                                    classOfBook, Integer.toString(a), realIdOfBook, getContext(), getActivity(), nameOfSchool, true));
                            realIdOfBook.add(a);
                        } else if (subj.equals("Все предметы") && forWhoThisBook.equals(forWho) && classOfBook.equals(classOf)) {
                            books.add(new BookForRecycle(dataSnapshot.child("Books").child(Integer.toString(a)).child("Author").getValue(String.class),
                                    mSubj, dataSnapshot.child("Books").child(Integer.toString(a)).child("Describing").getValue(String.class),
                                    classOfBook, Integer.toString(a), realIdOfBook, getContext(), getActivity(), nameOfSchool, true));
                            realIdOfBook.add(a);
                        } else if (mSubj.equals(subj) && classOfBook.equals(classOf) && forWhoThisBook.equals(forWho)) {
                            books.add(new BookForRecycle(dataSnapshot.child("Books").child(Integer.toString(a)).child("Author").getValue(String.class),
                                    mSubj, dataSnapshot.child("Books").child(Integer.toString(a)).child("Describing").getValue(String.class),
                                    classOfBook, Integer.toString(a), realIdOfBook, getContext(), getActivity(), nameOfSchool, true));
                            realIdOfBook.add(a);
                        }
                    }
                }
                if(books.size() == 0){
                    books.add(new BookForRecycle (
                            "Ничего не найдено, поменяйте условия сортировки",
                            "",
                            "",
                            "",
                            Integer.toString(0), realIdOfBook, getContext(), getActivity(), nameOfSchool, false));
                    realIdOfBook.add(0);
                }
                updateUI();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                ad.setTitle("Error");  // заголовок
                ad.setMessage("Ошибка: " + databaseError.getMessage() + "\n Проблемы на серверной части. Можете сообщить в службу поддержки"); // сообщение
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
        });
    }

    // выводим книги с сервера на экран
    public void updateUI() {
        if(getActivity() != null) {
            DataAdapterForSchool adapter = new DataAdapterForSchool(getContext(), books);
            recyclerView.setAdapter(adapter);
        }
    }

    // проверка, есть ли инет
    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
