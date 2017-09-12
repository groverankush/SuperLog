package com.ankushgrover.android_super_log;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        new SuperLog.Builder().init(getApplication());

        SuperLog.init(getApplication());

        button = (Button) findViewById(R.id.start);

        findViewById(R.id.debug).setOnClickListener(this);
        findViewById(R.id.error).setOnClickListener(this);
        findViewById(R.id.warn).setOnClickListener(this);
        findViewById(R.id.verbose).setOnClickListener(this);
        findViewById(R.id.info).setOnClickListener(this);
        button.setOnClickListener(this);


        random = new Random();
    }

    private void scheduleTimer() {
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                String tag = ((EditText) findViewById(R.id.tag)).getText().toString();
                String message = ((EditText) findViewById(R.id.message)).getText().toString();

                if (tag.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Tag", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (message.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please Enter Message", Toast.LENGTH_SHORT).show();
                    return;
                }

                int type = random.nextInt(6);

                switch (type) {
                    case SuperLog.DEBUG:
                        SuperLog.d(tag, message);
                        break;

                    case SuperLog.WARNING:
                        SuperLog.w(tag, message);
                        break;

                    case SuperLog.ERROR:
                        SuperLog.e(tag, message);
                        break;

                    case SuperLog.VERBOSE:
                        SuperLog.v(tag, message);
                        break;

                    case SuperLog.INFO:
                        SuperLog.i(tag, message);
                        break;

                    case SuperLog.NORMAL:
                        SuperLog.log(message);
                        break;

                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 5000);
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
                SuperLog.d(tag, message);
                break;

            case R.id.error:
                SuperLog.e(tag, message);
                break;

            case R.id.warn:
                SuperLog.w(tag, message);
                break;

            case R.id.verbose:
                SuperLog.v(tag, message);
                break;

            case R.id.info:
                SuperLog.i(tag, message);
                break;

            case R.id.start:
                if (!isPressed) {
                    button.setText("Stop");
                    scheduleTimer();
                } else {
                    button.setText("Start");
                    timer.cancel();
                    task.cancel();
                }


                isPressed = !isPressed;
                break;

            default:
                SuperLog.log(message);
                break;

        }

    }
}
