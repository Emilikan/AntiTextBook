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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.net.HttpURLConnection.HTTP_OK;

public class Send extends Fragment {
    private EditText nameOfFeedback;
    private EditText describingOfFeedback;
    private FrameLayout frameLayout;

    private String mNameOfFeedback;
    private String mDescribingOfFeedback;

    private DatabaseReference mRef;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send, container, false);

        nameOfFeedback = rootView.findViewById(R.id.nameOfFeedback);
        describingOfFeedback = rootView.findViewById(R.id.describingOfFeedback);
        frameLayout = rootView.findViewById(R.id.send);
        setTheme();

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
                else if(!isOnline(Objects.requireNonNull(getContext()))){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Warning")
                            .setMessage("Сообщение не отправлено. Нет доступа в интернет. Проверьте наличие связи")
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
                    mRef = FirebaseDatabase.getInstance().getReference();
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String url = dataSnapshot.child("mailgun").child("url").getValue(String.class);
                            String pass = dataSnapshot.child("mailgun").child("pass").getValue(String.class);
                            sendEmail(url, pass);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        return rootView;
    }

    // проверяем налисие интернета
    private static boolean isOnline (Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    // метод отправки письма через Mailgun
    private void sendEmail(String url, String pass) {
        String to = "atbusersmail@gmail.com";
        String from = "myFeedback@gmail.com";
        String subject = nameOfFeedback.getText().toString().trim();
        String message = describingOfFeedback.getText().toString().trim();

        if (subject.isEmpty()) {
            nameOfFeedback.setError("Поле 'Имя' не заполненно. Пожалуйста, заполните все поля");
            nameOfFeedback.requestFocus();
            return;
        }

        if (message.isEmpty()) {
            describingOfFeedback.setError("Message required");
            describingOfFeedback.requestFocus();
            return;
        }

        RetrofitClient.getInstance(url, pass)
                .getApi()
                .sendEmail(from, to, subject, message)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.code() == HTTP_OK) {
                            try {
                                assert response.body() != null;
                                JSONObject obj = new JSONObject(response.body().string());
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                nameOfFeedback.setText("");
                                describingOfFeedback.setText("");
                            } catch (JSONException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        //send email if validation passes
    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
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
