package com.example.chillapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    SecretTextView txt1, txt2;
    boolean show1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);

        setListener();
        txt1.show();    // показывает
//        txt.hide();    // скрывает
//        txt.toggle();  // показывает или скрывает в зависимости от текущего состояния видимости
    }

    private void setListener(){
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (show1) {
                    txt1.setIsVisible(false);
                    txt2.show();
                    show1 = !show1;
                } else {
                    txt2.setIsVisible(false);
                    txt1.show();
                    show1 = !show1;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show1)
                    txt1.startAnimation(animation);
                else  txt2.startAnimation(animation);
            }
        });
    }
}