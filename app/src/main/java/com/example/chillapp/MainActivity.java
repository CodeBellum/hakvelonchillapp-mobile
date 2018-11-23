package com.example.chillapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    SecretTextView txt1, txt2, subTxt1, subTxt2;
    boolean showFirst = true;
    LinearLayout first, second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViews();
        setListener();
        txt1.show();// показывает
        subTxt1.show();
//        txt.hide();    // скрывает
//        txt.toggle();  // показывает или скрывает в зависимости от текущего состояния видимости
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
        } else {
            txt2.setText(item.primaryText);
            subTxt2.setText(item.secondaryText);
        }
    }

    private void setListener(){
        final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.fadeup);
        animation1.setAnimationListener(this);
        animation2.setAnimationListener(this);
        findViewById(R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rnd = new Random(System.currentTimeMillis());
                int number = rnd.nextInt(2);
                switch (number){
                    case 0:
                        if (showFirst)
                            first.startAnimation(animation1);
                        else  second.startAnimation(animation1);
                        break;
                    case 1:
                        if (showFirst)
                            first.startAnimation(animation2);
                        else  second.startAnimation(animation2);
                        break;
                }
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
            //TODO поместить на это место функцию setChillText
            txt2.show();
            subTxt2.show();
        } else {
            txt2.setIsVisible(false);
            subTxt2.setIsVisible(false);
            showFirst = !showFirst;
            //TODO поместить на это место функцию setChillText
            txt1.show();
            subTxt1.show();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

