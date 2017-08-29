package com.ankushgrover.superlog.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.constants.SuperLogConstants;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class Utils implements SuperLogConstants {

    /**
     * To check whether the string is empty or not
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string) || TextUtils.isEmpty(string.trim()) || "null".contentEquals(string.trim());
    }

    public static String getLogString(@NonNull String tag, @NonNull String message, @NonNull String timeStamp) {
        return String.format("%s %s: %s", timeStamp, tag, message);
    }

    public static int getColor(int type) {
        switch (type) {

            case DEBUG:
            case NORMAL:
                return R.color.debug;

            case ERROR:
                return R.color.error;

            case VERBOSE:
                return R.color.verbose;

            case WARNING:
                return R.color.warn;


        }
        return R.color.verbose;
    }

}
