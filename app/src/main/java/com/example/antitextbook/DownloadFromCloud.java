package com.example.antitextbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class DownloadFromCloud extends Fragment {
    private final int STORAGE_PERMISSION_CODE = 23;

    private DatabaseReference mRef;
    private List<String> mTasks;
    private String counter = "-1";

    private ListView listTasks;

    public ImageView imageView;
    public String conterOfFragment;

    private static class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView mTitleBooks;
        ImageView mBooksImage;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleBooks = (TextView) itemView.findViewById(R.id.tv_title_books);
            mBooksImage = (ImageView) itemView.findViewById(R.id.image_books_id);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download_from_cloud, container, false);

        Button search = (Button) rootView.findViewById(R.id.search); // кнопка поиска
        search.setOnClickListener(new View.OnClickListener() {
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

        /*RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_books);
        Toast.makeText(getContext(), "2", Toast.LENGTH_LONG).show();
        FirebaseRecyclerOptions<String> options = new FirebaseRecyclerOptions.Builder<String>().setQuery(mRef, String.class).build();

        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<String, TaskViewHolder>(options) {

            @Override
            public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_fragment, parent, false);
                Toast.makeText(getContext(), "gh", Toast.LENGTH_LONG).show();

                return new TaskViewHolder(view);
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onBindViewHolder(TaskViewHolder viewHolder, int position, @NonNull String model) {

                Toast.makeText(getContext(), "ghdf", Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), model, Toast.LENGTH_LONG).show();
                viewHolder.mTitleBooks.setText(model);
            }

        };

        recyclerView.setAdapter(adapter);
        */
        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.addValueEventListener(new ValueEventListener() {
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
                    checked();
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
        listTasks = (ListView) rootView.findViewById(R.id.booksListView);


        /*ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.imageButton);
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

                final long ONE_MEGABYTE = 49739700;
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
                    });
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


        });*/

        return rootView;
    }


    private void checked() {
        mRef = FirebaseDatabase.getInstance().getReference();
        // слушатель ListView
        listTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Fragment fragment = new AboutBook();
                FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                Bundle bundle = new Bundle();
                String valueOfReplace = position + "";
                bundle.putString("Value", valueOfReplace);
                fragment.setArguments(bundle);
            }
        });

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                mTasks = dataSnapshot.child("AllBooks").getValue(t);
                updateUI();
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

    public void updateUI() {
        if (getActivity() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mTasks);
            listTasks.setAdapter(adapter);
        }
    }




    //We are calling this method to check the permission status
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),Manifest.permission.READ_EXTERNAL_STORAGE)){
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