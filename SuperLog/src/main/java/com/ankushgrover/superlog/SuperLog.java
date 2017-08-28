package com.ankushgrover.superlog;

import android.os.Bundle;

import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.db.helpers.SuperLogDbHelper;
import com.ankushgrover.superlog.db.listener.DataLoadListener;
import com.ankushgrover.superlog.model.SuperLogModel;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 24/8/17.
 */

public class SuperLog implements SuperLogConstants {

    private static final int DEBUG = 0;
    private static final int ERROR = 1;
    private static final int WARNING = 2;
    private static final int NORMAL = 3;

    /**
     * Debug log
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        log(DEBUG, tag, msg);
    }


    /**
     * Error log
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        log(ERROR, tag, msg);
    }

    /**
     * Warning log
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        log(WARNING, tag, msg);
    }


    /**
     * Log without tag.
     *
     * @param msg
     */
    public static void log(String msg) {
        log(NORMAL, "", msg);
    }


    private static void log(int type, String tag, String msg) {

        Bundle bundle = new Bundle();
        bundle.putParcelable(LOG, new SuperLogModel(tag,msg, type));

        SuperLogDbHelper.getInstance().perform(SuperLogDbHelper.INSERT_LOG, bundle, new DataLoadListener<Object>() {
            @Override
            public void onDataLoaded(Object obj, int key) {

            }
        });

    }

}
