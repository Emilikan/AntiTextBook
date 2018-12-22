/*
* Класс для отправки письма через Mailgun
 */


package com.example.antitextbook;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private DatabaseReference mRef;



    private static String BASE_URL;

    private static final String API_USERNAME = "api";

    //you need to change the value to your API key
    private static String API_PASSWORD;

    private static String AUTH;

    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(String url, String pass) {
        BASE_URL = url;
        API_PASSWORD = pass;
        AUTH  = "Basic " + Base64.encodeToString((API_USERNAME+":"+API_PASSWORD).getBytes(), Base64.NO_WRAP);

        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(@NonNull Chain chain) throws IOException {
                                Request original = chain.request();
                                //Adding basic auth
                                Request.Builder requestBuilder = original.newBuilder()
                                        .header("Authorization", AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();

    }

    public static synchronized RetrofitClient getInstance(String url, String pass) {
        if (mInstance == null) {
            mInstance = new RetrofitClient(url, pass);
        }
        return mInstance;
    }

    public Retrofit getClient() {
        return retrofit;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
