package com.example.chillapp;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    private Button airplaneModeBtn, enterBtn;
    private Context context = this;
    private Timer timer;
    private AnySecond anySecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        timer = new Timer();
        anySecond = new AnySecond();
        timer.scheduleAtFixedRate(anySecond,1000,2000);
        airplaneModeBtn = (Button) findViewById(R.id.airplaneMode);
        enterBtn = (Button) findViewById(R.id.enter);
        if (isAirplaneMode()) airplaneModeBtn.setVisibility(View.GONE);
        else airplaneModeBtn.setVisibility(View.VISIBLE);
        airplaneModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(android.provider.Settings.ACTION_AIRPLANE_MODE_SETTINGS));
            }
        });
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
            }
        });
    }
    public boolean isAirplaneMode() {
        return Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    }

    @Override
    protected void onResume() {
        if (isAirplaneMode()) airplaneModeBtn.setVisibility(View.GONE);
        else airplaneModeBtn.setVisibility(View.VISIBLE);
        super.onResume();
    }

    class AnySecond extends TimerTask{
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAirplaneMode()) airplaneModeBtn.setVisibility(View.GONE);
                    else airplaneModeBtn.setVisibility(View.VISIBLE);
                }
            });

        }
    }
}
