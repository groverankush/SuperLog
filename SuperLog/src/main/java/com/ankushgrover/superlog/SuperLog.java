package com.ankushgrover.superlog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.model.SuperLogModel;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 24/8/17.
 */

public class SuperLog implements SuperLogConstants {


    /**
     * Debug log
     *
     * @param tag
     * @param msg
     */
    public static void d(@NonNull String tag, @NonNull String msg) {
        Log.d(tag, msg);
        log(DEBUG, tag, msg);
    }


    /**
     * Error log
     *
     * @param tag
     * @param msg
     */
    public static void e(@NonNull String tag, @NonNull String msg) {
        Log.e(tag, msg);
        log(ERROR, tag, msg);
    }

    /**
     * Warning log
     *
     * @param tag
     * @param msg
     */
    public static void w(@NonNull String tag, @NonNull String msg) {
        Log.w(tag, msg);
        log(WARNING, tag, msg);
    }

    /**
     * Verbose log
     *
     * @param tag
     * @param msg
     */
    public static void v(@NonNull String tag, @NonNull String msg) {
        Log.v(tag, msg);
        log(VERBOSE, tag, msg);
    }


    /**
     * Log without tag.
     *
     * @param msg
     */
    public static void log(String msg) {
        Log.v(SuperLog.class.getSimpleName(), msg);
        log(NORMAL, SuperLog.class.getSimpleName(), msg);
    }


    private static void log(int type, String tag, String msg) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(LOG, new SuperLogModel(tag, msg, type));

        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.INSERT_LOG, bundle, new DataLoadListener<Object>() {
            @Override
            public void onDataLoaded(Object obj, int key) {

            }
        });

    }

}
