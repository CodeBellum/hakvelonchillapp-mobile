package com.example.chillapp;

import android.content.res.AssetFileDescriptor;
import android.icu.util.MeasureUnit;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    SecretTextView txt1, txt2, subTxt1, subTxt2;
    boolean showFirst = true;
    LinearLayout first, second;
    private int i = 0;
    List<CustomItem> phrases;
    DatabaseHelper dbHelper;
    CountDownTimer skipTimer;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dbHelper = new DatabaseHelper(this);
        phrases = dbHelper.getAll();

        initViews();
        setListener();

        txt1.setText(phrases.get(0).primaryText);
        subTxt1.setText(phrases.get(0).secondaryText);
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, phrases.get(0).firstTextSize);
        if (phrases.get(0).secondTextSize>0)
            subTxt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, phrases.get(0).secondTextSize);
        txt1.show();
        subTxt1.show();
        findViewById(R.id.content).setClickable(false);
        setTimer(phrases.get(0).minShowTime, phrases.get(0).maxShowTime);
        try{
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_1);
            mediaPlayer.start();
        } catch (Exception e){
            Log.e("playMusic", e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void initViews(){
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        subTxt1 = findViewById(R.id.subTxt1);
        subTxt2 = findViewById(R.id.subTxt2);

        first = findViewById(R.id.firstLayout);
        second = findViewById(R.id.secondLayout);
    }

    private void setChillText(CustomItem item){
        if(showFirst){
            txt1.setText(item.primaryText);
            subTxt1.setText(item.secondaryText);
            txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.firstTextSize);
            if (item.secondTextSize>0)
                subTxt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.secondTextSize);
        } else {
            txt2.setText(item.primaryText);
            subTxt2.setText(item.secondaryText);
            txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.firstTextSize);
            if (item.secondTextSize>0)
                subTxt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.secondTextSize);
        }
        setTimer(item.minShowTime, item.maxShowTime);
    }

    private void setTimer(long timeToTap, long timeToSkip) {
        timeToSkip = timeToSkip - timeToTap;
        skipTimer = new CountDownTimer(timeToSkip, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                showNext();
            }
        };

        new CountDownTimer(timeToTap, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                skipTimer.start();
                findViewById(R.id.content).setClickable(true);
            }
        }.start();
    }

    private void showNext(){
        findViewById(R.id.content).setClickable(false);
        final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.fadeup);
        animation1.setAnimationListener(this);
        animation2.setAnimationListener(this);

        if (i < phrases.size()-1) {
            i++;
            Random rnd = new Random(System.currentTimeMillis());
            int number = rnd.nextInt(2);

            switch (number) {
                case 0:
                    if (showFirst)
                        first.startAnimation(animation1);
                    else second.startAnimation(animation1);
                    break;
                case 1:
                    if (showFirst)
                        first.startAnimation(animation2);
                    else second.startAnimation(animation2);
                    break;
            }
        }
    }

    private void setListener(){
        findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.content).setClickable(false);
                skipTimer.cancel();
                showNext();
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (showFirst) {
            txt1.setIsVisible(false);
            subTxt1.setIsVisible(false);
            showFirst = !showFirst;
            setChillText(phrases.get(i));
            txt2.show();
            subTxt2.show();
        } else {
            txt2.setIsVisible(false);
            subTxt2.setIsVisible(false);
            showFirst = !showFirst;
            setChillText(phrases.get(i));
            txt1.show();
            subTxt1.show();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

