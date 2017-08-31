package com.ankushgrover.android_super_log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ankushgrover.superlog.SuperLog;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SuperLog.init(getApplication());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SuperLog.i("Checkl", "message");
            }
        }, 0, 5000);

    }
}
