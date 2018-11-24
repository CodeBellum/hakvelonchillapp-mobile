package com.example.chillapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.Comparator;
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
    private Button reenter, airplane;
    private Context context = this;
    private LinearLayout layout;
    private String currentSound;
    private int lastSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dbHelper = new DatabaseHelper(this);
        phrases = dbHelper.getAll();
        initViews();
        setListener();
        reenter = (Button) findViewById(R.id.reenter);
        airplane = (Button) findViewById(R.id.exit_airplaneMode);
        layout = (LinearLayout) findViewById(R.id.button_exit_layout);
        txt1.setText(phrases.get(0).primaryText);
        subTxt1.setText(phrases.get(0).secondaryText);
        txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, phrases.get(0).firstTextSize+2);
        if (phrases.get(0).secondTextSize > 0)
            subTxt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, phrases.get(0).secondTextSize+2);
        txt1.show();
        subTxt1.show();
        findViewById(R.id.content).setClickable(false);
        setTimer(phrases.get(0).minShowTime, phrases.get(0).maxShowTime);
        currentSound = phrases.get(0).theme;
        runSound(getResources().getIdentifier(phrases.get(0).theme, "raw", getPackageName()));
        lastSec = 100;

        reenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartActivity.class);
                startActivity(intent);
                finish();
            }
        });

        airplane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void runSound(int resId) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            mediaPlayer = MediaPlayer.create(getApplicationContext(), resId);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e("playMusic", e.getMessage());
        }
    }

    private void initViews() {
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        subTxt1 = findViewById(R.id.subTxt1);
        subTxt2 = findViewById(R.id.subTxt2);

        first = findViewById(R.id.firstLayout);
        second = findViewById(R.id.secondLayout);
    }

    private void setChillText(CustomItem item) {
        if (showFirst) {
            txt1.setText(item.primaryText);
            subTxt1.setText(item.secondaryText);
            txt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.firstTextSize+2);
            if (item.secondTextSize > 0)
                subTxt1.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.secondTextSize+2);
        } else {
            txt2.setText(item.primaryText);
            subTxt2.setText(item.secondaryText);
            txt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.firstTextSize+2);
            if (item.secondTextSize > 0)
                subTxt2.setTextSize(TypedValue.COMPLEX_UNIT_SP, item.secondTextSize+2);
        }
        setTimer(item.minShowTime, item.maxShowTime);
    }

    private void setTimer(long timeToTap, long timeToSkip) {
        timeToSkip = timeToSkip - timeToTap;
        if (showFirst) {
            if (txt1.getText().toString().contains("@time")) {
                txt1.setText(txt1.getText().toString().replace("@time", "30"));
                lastSec = 30;
            }
        } else {
            if (txt2.getText().toString().contains("@time")) {
                txt2.setText(txt2.getText().toString().replace("@time", "30"));
                lastSec = 30;
            }
        }
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
                if (showFirst) {
                    txt1.setText(txt1.getText().toString().replace(String.valueOf(lastSec),String.valueOf(millisUntilFinished/1000)));
                    lastSec = (int) (millisUntilFinished/1000);
                }
                else {
                    txt2.setText(txt2.getText().toString().replace(String.valueOf(lastSec),String.valueOf(millisUntilFinished/1000)));
                    lastSec = (int) (millisUntilFinished/1000);
                }
            }

            @Override
            public void onFinish() {
                skipTimer.start();
                findViewById(R.id.content).setClickable(true);
            }
        }.start();
    }

    private void showNext() {
        findViewById(R.id.content).setClickable(false);
        final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.fadeup);
        animation1.setAnimationListener(this);
        animation2.setAnimationListener(this);

        if (i < phrases.size() - 1) {
            i++;
            if (!currentSound.equals(phrases.get(i).theme)) {
                runSound(getResources().getIdentifier(phrases.get(i).theme, "raw", getPackageName()));
                currentSound = phrases.get(i).theme;
            }
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
        } else layout.setVisibility(View.VISIBLE);
    }

    private void setListener() {
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

