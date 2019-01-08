package com.example.antitextbook;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private Boolean isHome;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTheme();

        // push уведомления
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
        // подписываем всех новых пользователей на одну тему. Через нее будем осуществлять отправку всех push уведомлений
        FirebaseMessaging.getInstance().subscribeToTopic("ForAllUsers1");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // устанавливаем начальный фрагмент - Home
        Fragment fragment = null;
        Class fragmentClass = Home.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // проверяем, первый ли раз зашел человек (заполненя ли информация о человеке)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String isFirst = preferences.getString("isFirst", "false");
        assert isFirst != null;
        if(isFirst.equals("false")){
            fragment = new Settings();
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

            setTitle("Настройки");

            Toast.makeText(this, "Пожалуйста, заполните информацию о себе", Toast.LENGTH_LONG).show();
        }

    }

    // метод изменения темы
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String dark = preferences.getString("Theme", "0");

        if("TRUE".equals(dark)) {
            toolbar.setBackgroundResource(R.drawable.dark_bg);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // создаем новый фрагмент
        Fragment fragment = null;
        Class fragmentClass;

        int id = item.getItemId();

        if (id == R.id.library) {
            fragmentClass = Library.class;
        }
        else if (id == R.id.settings) {
            fragmentClass = Settings.class;
        }
        else if (id == R.id.server) {
            fragmentClass = MainSettings.class;
        }
        else if (id == R.id.nav_send) {
            fragmentClass = Send.class;
        }
        else if (id == R.id.home) {
            fragmentClass = Home.class;
        }
        else if (id == R.id.schedule) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String pair = preferences.getString("Checked", "0");

            if("pair".equals(pair)) {
                fragmentClass = Schedule.class;
            }
            else{
                fragmentClass = Schedule.class;
            }
        }
        else {
            fragmentClass = Home.class;
        }

        // ловим ошибки
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Вставляем фрагмент, заменяя текущий фрагмент
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // устанавливаем имя
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView textView = findViewById(R.id.title_name);
        textView.setText(preferences.getString("UserName", "User"));

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Settings();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

                setTitle("Настройки");
            }
        });

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
