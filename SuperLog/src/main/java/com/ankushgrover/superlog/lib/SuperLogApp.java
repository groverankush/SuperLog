package com.ankushgrover.superlog.lib;

import android.app.Application;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class SuperLogApp extends Application {

    private static SuperLogApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static SuperLogApp getInstance() {
        return mInstance;
    }
}
