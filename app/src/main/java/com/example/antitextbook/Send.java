package com.example.antitextbook;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Send extends Fragment {
    private EditText nameOfFeedback;
    private EditText describingOfFeedback;

    String mNameOfFeedback;
    String mDescribingOfFeedback;
    private String dbCounter;
    private int counterFor = 0;

    private DatabaseReference mRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send, container, false);

        nameOfFeedback = rootView.findViewById(R.id.nameOfFeedback);
        describingOfFeedback = rootView.findViewById(R.id.describingOfFeedback);

        Button sendFeedback = rootView.findViewById(R.id.sendFeedback);
        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mNameOfFeedback = nameOfFeedback.getText().toString();
                mDescribingOfFeedback = describingOfFeedback.getText().toString();

                if("".equals(mDescribingOfFeedback) || "".equals(mNameOfFeedback)){
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
                else{
                    saveDataToDatabase();
                    counterFor = 1;
                }

            }
        });
        return rootView;
    }

    private void saveDataToDatabase(){
        mRef = FirebaseDatabase.getInstance().getReference();

        mRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(counterFor == 1) {
                    dbCounter = dataSnapshot.child("counterOfFeedback").getValue(String.class);
                    Toast.makeText(getActivity(), dbCounter, Toast.LENGTH_SHORT).show();
                    int intCounter = Integer.parseInt(dbCounter);
                    intCounter++;
                    String stringCounter = Integer.toString(intCounter);

                    mRef.child("Feedback").child(stringCounter).child("Describing").setValue(mDescribingOfFeedback);
                    mRef.child("Feedback").child(stringCounter).child("NameOf").setValue(mNameOfFeedback);
                    mRef.child("Feedback").child(stringCounter).child("ThisCounter").setValue(stringCounter);
                    mRef.child("counterOfFeedback").setValue(stringCounter);


                    ((EditText) Objects.requireNonNull(getActivity()).findViewById(R.id.nameOfFeedback)).setText("");
                    ((EditText) getActivity().findViewById(R.id.describingOfFeedback)).setText("");

                    counterFor = 0;
                    Toast.makeText(getActivity(), "Оправлено", Toast.LENGTH_SHORT).show();
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
}
