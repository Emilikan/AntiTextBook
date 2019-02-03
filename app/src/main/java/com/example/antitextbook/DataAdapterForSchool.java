package com.example.antitextbook;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.antitextbook.Constants.a0;
import static com.example.antitextbook.MainActivity.fragmentIs;

/**
 * Класс генерирования RecycleView для школьной админки. Отдеьный класс, т.к. сильно другое действие при нажатии, а отслеживание нажатия происходит в этом классе
 */

public class DataAdapterForSchool extends RecyclerView.Adapter<DataAdapter.ViewHolder>  {

    private LayoutInflater inflater;
    private List<BookForRecycle> books;
    private DatabaseReference mRef;

    private ArrayList<String> allSchool;
    private String counterOfSchool;

    private ProgressBar progressBar;

    DataAdapterForSchool(Context context, List<BookForRecycle> books) {
        this.books = books;
        this.inflater = LayoutInflater.from(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @NonNull
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_view, parent, false);
        progressBar = view.findViewById(R.id.progressBarInRecycle);
        return new DataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataAdapter.ViewHolder viewHolder, final int i) {
        final BookForRecycle book = books.get(i);
        if (book.getIsBook()) {
                viewHolder.counterOfFragment = book.getArrayList().get(i) + "";
                viewHolder.authorView.setText(book.getAuthor());
                viewHolder.describingView.setText(book.getDescribing());
                viewHolder.subjectView.setText(book.getSubject());
                viewHolder.classView.setText(book.getClassOfBook());

                mRef = FirebaseDatabase.getInstance().getReference();
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl(Objects.requireNonNull(dataSnapshot.child("Books").child(book.getArrayList().get(i) + "").child("Icon").getValue(String.class)));
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                Picasso.with(book.getContext()).load(uri).into(viewHolder.imageView);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        doing(book, book.getArrayList().get(viewHolder.getAdapterPosition()));
                    }
                });
        } else {
            viewHolder.authorView.setText(book.getAuthor());
            viewHolder.describingView.setText(book.getDescribing());
            viewHolder.subjectView.setText(book.getSubject());
            viewHolder.classView.setText(book.getClassOfBook());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void doing(final BookForRecycle bookForRecycle, final int positionn){
        if (!bookForRecycle.getNameOfSchool().equals("Ошибка")) {
            allSchool = new ArrayList<>();
            AlertDialog.Builder ad;

            ad = new AlertDialog.Builder(Objects.requireNonNull(bookForRecycle.getContext()));
            ad.setTitle("Подписаться на книгу");  // заголовок
            ad.setMessage("Вы уверены, что хотите подписаться под этой книгой? После подписи ученики вашей школы смогут видеть ее у себя"); // сообщение
            ad.setPositiveButton("Ок, подписаться", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    // для получения номера
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            counterOfSchool = dataSnapshot.child("Books").child(Integer.toString(positionn)).child("SchoolCounter").getValue(String.class);

                            // проверка на то, подписаны ли мы уже
                            assert counterOfSchool != null;
                            for(int a = 0; a < Integer.parseInt(counterOfSchool) + 1; a++){

                                String school = dataSnapshot.child("Books").child(Integer.toString(positionn)).child("School").child(Integer.toString(a)).getValue(String.class);
                                allSchool.add(school);
                            }

                            // если не подписаны:
                            if(!allSchool.contains(bookForRecycle.getNameOfSchool())){
                                assert counterOfSchool != null;
                                int intCounter = Integer.parseInt(counterOfSchool);
                                intCounter++;
                                String stringCounter = Integer.toString(intCounter);
                                mRef.child("Books").child(Integer.toString(positionn)).child("School").child(stringCounter).setValue(bookForRecycle.getNameOfSchool());
                                mRef.child("Books").child(Integer.toString(positionn)).child("SchoolCounter").setValue(stringCounter);
                                Toast.makeText(bookForRecycle.getContext(), "Вы успешно отметились", Toast.LENGTH_LONG).show();
                            }
                            // если подписаны:
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(bookForRecycle.getContext()));
                                builder.setTitle("Error")
                                        .setMessage("Вы уже выбрали данную книгу")
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

                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(bookForRecycle.getContext()));
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

            ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });
            ad.setCancelable(true);
            ad.show();
        }

        // если нет названия школы
        else {
            AlertDialog.Builder ad;
            ad = new AlertDialog.Builder(Objects.requireNonNull(bookForRecycle.getContext()));
            ad.setTitle("Error");  // заголовок
            ad.setMessage("WTF??? Крч, я сам хз че за фигня, но пиши в службу поддержки"); // сообщение
            ad.setPositiveButton("Служба поддержки", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    Fragment fragment = new Send();
                    FragmentManager fragmentManager = Objects.requireNonNull(bookForRecycle.getActivity()).getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    fragmentIs = a0;
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

    @Override
    public int getItemCount() {
        return books.size();
    }
}
