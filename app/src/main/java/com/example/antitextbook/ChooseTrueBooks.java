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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.antitextbook.Constants.*;
import static com.example.antitextbook.MainActivity.fragmentIs;

/**
 * Создаем RecycleView для админки (с последующими действиями)
 */

/**
 * ░░░░░░░░░░░░░░░░░░░██████████████
 * ░░░░░░░░░░░░░░░░████════════════████
 * ░░░░░░░░░░░░░████══════════════════███
 * ░░░░░░░░░░░███════════════════════════██
 * ░░░░░░░░░███════════════════════════════██
 * ░░░░░░░░██═══════════════════════════════██
 * ░░░░░░░██══════════════════════════════════█
 * ░░░░░░█══════════════════════████████═══════█
 * ░░░░░█══════════════════════██═══════════════█
 * ░░░░█════════════════════════════════███══════█
 * ░░░██══════════════███════════██████══════════██
 * ░░██═══════════████═════════██████████═════════██
 * ░░█════════════════████════████══════███════════█
 * ░██═══════════██████═══════██═══████══███═══════█
 * ░█══════════════════════█════████══███══════════██
 * ██════════════════███████═══██════════█══════════█
 * █══════════█████████████═══█═══════════█═════════█
 * █═════════███████═══════█═█═══██═══════█═════════█
 * █═════════██══════███══██═█═══██═══════█═════════█
 * █══════════██══════█══██══██══════════█══════════█
 * █═══════════███══════██════██═══════██═══════════█
 * █═════════════███████════════███████═════════════█
 * █══════════════════════█═════════════════════════█
 * █══════════════════════███═══════════════════════█
 * █══════════════════█████═██═══███════════════════█
 * █════════════════██═██══════════██═══════════════█
 * ██═══════════════██═█════════════██═════════════██
 * ░██═════════════█══██════════████═█═════════════█
 * ░░█════════════█════██══════██═════█═══════════██
 * ░░██═════════════════████████══════════════════█
 * ░░░█═════════════════════█████═══█════════════██
 * ░░░░█═══════════█═════████════██══█══════════██
 * ░░░░██═════════█═█████═════█═══█══█═════════██
 * ░░░░░██════════█═════════███════█═█════════██
 * ░░░░░░██═══════█════█████═════════════════██
 * ░░░░░░░░██═══════════════════════════════██
 * ░░░░░░░░░██════════════════════════════██
 * ░░░░░░░░░░░██════════════════════════██
 * ░░░░░░░░░░░░░███══════════════════███
 * ░░░░░░░░░░░░░░░████████████████████
 */

public class ChooseTrueBooks extends Fragment {
    private List<BookForRecycle> books = new ArrayList<>();

    private FrameLayout frameLayout;
    private String counter = "-1";

    private ProgressBar progressBar;

    private ArrayList<Integer> realIdOfBook= new ArrayList<>(); // тут под id в новом списке хранится настоящий id книги
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_true_books, container, false);

        progressBar = rootView.findViewById(R.id.progressBarInChoose);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        ImageView back = rootView.findViewById(R.id.back3ForAdmin);
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
                fragmentIs = a7;
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
                                    fragmentIs = a7;
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            frameLayout = rootView.findViewById(R.id.chooseTrueBooks);
            recyclerView = rootView.findViewById(R.id.listForAdmin);
            setTheme();

            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    counter = dataSnapshot.child("forChecking").child("counter").getValue(String.class);
                    if ("-1".equals(counter)) {
                        Toast.makeText(getContext(), "Нет книг", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(ProgressBar.INVISIBLE); // убираем прогресс бар
                    } else {
                        assert counter != null;
                        for (int a = 0; a <= Integer.parseInt(counter); a++){
                            if(dataSnapshot.child("forChecking").child("Books").child(Integer.toString(a)).child("Author").getValue(String.class) != null) {
                                books.add(new BookForRecycle(dataSnapshot.child("forChecking").child("Books").child(Integer.toString(a)).child("Author").getValue(String.class),
                                        dataSnapshot.child("forChecking").child("Books").child(Integer.toString(a)).child("Subject").getValue(String.class),
                                        dataSnapshot.child("forChecking").child("Books").child(Integer.toString(a)).child("Describing").getValue(String.class),
                                        dataSnapshot.child("forChecking").child("Books").child(Integer.toString(a)).child("Class").getValue(String.class),
                                        Integer.toString(a),
                                        realIdOfBook,
                                        getContext(),
                                        getActivity(),
                                        true, true));
                                realIdOfBook.add(a);
                            }
                        }
                        progressBar.setVisibility(ProgressBar.INVISIBLE); // убираем прогресс бар
                        if(books.size() == 0){
                            books.add(new BookForRecycle (
                                    "Ничего не найдено, поменяйте условия сортировки",
                                    "",
                                    "",
                                    "",
                                    Integer.toString(0), realIdOfBook, getContext(),
                                    getActivity(), false, true));
                            realIdOfBook.add(0);
                        }
                        updateUI();
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

        return rootView;
    }

    // выводим книги с сервера на экран
    public void updateUI() {
        if (getActivity() != null) {
            DataAdapter adapter = new DataAdapter(getContext(), books);
            recyclerView.setAdapter(adapter);
        }
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            frameLayout.setBackgroundResource(R.drawable.dark_bg);

            //chooseText.setTextColor(R.color.colorDarkBlue);
            //downloadText.setTextColor(R.color.colorDarkText);
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
