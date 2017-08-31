package com.ankushgrover.superlog.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.constants.SuperLogConstants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
        return getLogString(tag, message, timeStamp, false);
    }

    public static String getLogString(@NonNull String tag, @NonNull String message, @NonNull String timeStamp, boolean withNewLine) {

        return withNewLine ? String.format("%s %s: %s \n", timeStamp, tag, message) : String.format("%s %s: %s", timeStamp, tag, message);
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

            case INFO:
                return R.color.info;

        }
        return R.color.verbose;
    }

    public static String[] getCredentials(Context context) {




        Properties properties = new Properties();
        InputStream inputStream =
                context.getClass().getClassLoader().getResourceAsStream("local.properties");
        try {
            properties.load(inputStream);
            String email = properties.getProperty("emailId");
            String pass = properties.getProperty("password");
            return new String[]{email, pass};

        } catch (Exception e) {
            e.printStackTrace();
            SuperLog.e("Error Fetching properties", "FAILED");
        }

        return new String[]{"hello", "world"};


    }

}
