package com.example.chillapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SecretTextView txt = findViewById(R.id.textview);

//        txt.show();    // показывает
//        txt.hide();    // скрывает
//        txt.toggle();  // показывает или скрывает в зависимости от текущего состояния видимости
    }
}