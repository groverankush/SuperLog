package com.ankushgrover.android_super_log;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ankushgrover.superlog.SuperLog;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TimerTask task;
    private Random random;
    private Timer timer;
    private boolean isPressed;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = findViewById(R.id.start);

        findViewById(R.id.debug).setOnClickListener(this);
        findViewById(R.id.error).setOnClickListener(this);
        findViewById(R.id.warn).setOnClickListener(this);
        findViewById(R.id.verbose).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);
        button.setOnClickListener(this);


        random = new Random();
    }

    private void scheduleTimer(final String tag, final String message) {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                int type = random.nextInt(6);

                switch (type) {
                    case SuperLog.DEBUG:
                        Log.d(tag, message);
                        break;

                    case SuperLog.WARNING:
                        Log.w(tag, message);
                        break;

                    case SuperLog.ERROR:
                        Log.e(tag, message);
                        break;

                    case SuperLog.VERBOSE:
                        Log.v(tag, message);
                        break;

                    case SuperLog.INFO:
                        Log.i(tag, message);
                        break;

                    case SuperLog.NORMAL:
                        Log.d(MainActivity.class.getSimpleName(), message);
                        break;

                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onClick(View v) {

        String tag = ((EditText) findViewById(R.id.tag)).getText().toString();
        String message = ((EditText) findViewById(R.id.message)).getText().toString();

        if (tag.isEmpty()) {
            Toast.makeText(this, "Please Enter Tag", Toast.LENGTH_SHORT).show();
            return;
        }

        if (message.isEmpty()) {
            Toast.makeText(this, "Please Enter Message", Toast.LENGTH_SHORT).show();
            return;
        }


        switch (v.getId()) {
            case R.id.debug:
                Log.d(tag, message);
                break;

            case R.id.error:
                Log.e(tag, message);
                break;

            case R.id.warn:
                Log.w(tag, message);
                break;

            case R.id.verbose:
                Log.v(tag, message);
                break;

            case R.id.info:
                Log.i(tag, message);
                break;

            case R.id.start:
                if (!isPressed) {
                    button.setText("Stop");
                    scheduleTimer(tag, message);
                } else {
                    button.setText("Start");
                    timer.cancel();
                    task.cancel();
                }


                isPressed = !isPressed;
                break;

            default:
                Log.d("Main Activity", message);
                break;

        }

    }
}
