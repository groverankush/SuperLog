package com.ankushgrover.android_super_log;

import android.app.Application;
import android.content.Context;

import com.ankushgrover.superlog.ContextWrapper;
import com.ankushgrover.superlog.SuperLog;


/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 13/9/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SuperLog.init(new SuperLog.Builder()
                .setSuperLogViewVisibility(true), new ContextWrapper() {
            @Override
            public Context getContext() {
                return getApplicationContext();
            }
        });

    }


}
