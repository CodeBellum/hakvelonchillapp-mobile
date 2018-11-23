package com.example.chillapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class StartOnClick extends AppCompatActivity implements Animation.AnimationListener {

    private ImageView animateImgView;
    private Context context = this;
    private RotateAnimation animationRight, animationLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_on_click);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        animateImgView = (ImageView) findViewById(R.id.startImg);


    }

    public void disableAirplaneMode(){
        if (!isAirplaneMode()) {
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
}
