package com.example.chillapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartOnClick extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView animateImgView;
    private Context context = this;
    private RotateAnimation animationLeft;
    private DatabaseHelper dbHelper;
    private TextView textView;
    private Timer timer;
    private MyTimerTask myTimerTask;
    SharedPreferences preferences;
    List<String> soundNames;
    private final static String DB_VERSION = "dbversion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_on_click);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        animateImgView = (ImageView) findViewById(R.id.startImg);

        preferences = getSharedPreferences("config", MODE_PRIVATE);
        dbHelper = new DatabaseHelper(context);
        textView = (TextView) findViewById(R.id.yourPlace);
        timer = new Timer();
        myTimerTask = new MyTimerTask();

    }

    public void disableAirplaneMode(){
        if (isAirplaneMode()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Магия");
            dialog.setMessage("Пожалуйста, выключите режим полета, и мы создадим для вас ваше уникальное тихое место, это не надолго. Обещаю");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(
                            new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        else {
            //TODO: сюда пихнуть скачку.
            getDBVersion();
        }
    }

    public boolean isAirplaneMode() {
        return Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {


        animationLeft = new RotateAnimation(-35, 15, animateImgView.getMeasuredWidth()/2, animateImgView.getMeasuredHeight()/2);
        animationLeft.setRepeatMode(Animation.REVERSE);
        animationLeft.setRepeatCount(Animation.INFINITE);
        animationLeft.setInterpolator(new LinearInterpolator());
        animationLeft.setDuration(2000L);
        animationLeft.setAnimationListener(this);
        animateImgView.startAnimation(animationLeft);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onResume() {
        disableAirplaneMode();
        super.onResume();
    }

    private void getDBVersion(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hakvelonchillapp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitApi = retrofit.create(RetrofitInterface.class);

        Call<VersionResp> callback = retrofitApi.getVersion();

        callback.enqueue(new Callback<VersionResp>() {
            @Override
            public void onResponse(Call<VersionResp> call, Response<VersionResp> response) {
                if (response.isSuccessful()){
                    if (!response.body().version.equals(preferences.getString(DB_VERSION, "")))
                    {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(DB_VERSION, response.body().version);
                        editor.apply();
                        getPhrases();
                    }
                    else {
                        lastSecondsInUI();
                    }
                } else {
                    Log.e("getVersion", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<VersionResp> call, Throwable t) {
                Log.e("getVersion", t.toString());
            }
        });
    }

    private void getPhrases(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hakvelonchillapp.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitApi = retrofit.create(RetrofitInterface.class);

        Call<List<CustomItem>> callback = retrofitApi.getPhrases();

        callback.enqueue(new Callback<List<CustomItem>>() {
            @Override
            public void onResponse(Call<List<CustomItem>> call, Response<List<CustomItem>> response) {
                if (response.isSuccessful()){
                    soundNames = new ArrayList<>();

                    for (int i = 0; i < response.body().size(); i++){
                        dbHelper.save(response.body().get(i));
                        if (!soundNames.contains(response.body().get(i).theme))
                            soundNames.add(response.body().get(i).theme);
                    }
                    lastSecondsInUI();
                } else {
                    Log.e("Phrases", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<CustomItem>> call, Throwable t) {
                Log.e("Phrases", t.toString());
            }
        });
    }
    public void lastSecondsInUI(){
        textView.setText("Вжух! Добро пожаловать.");
        timer.schedule(myTimerTask,2000);
    }
    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timer.cancel();
                    Intent intent = new Intent(context,StartActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}
