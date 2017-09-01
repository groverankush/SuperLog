package com.ankushgrover.superlog.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ankushgrover.superlog.R;
import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.constants.SuperLogConstants;
import com.ankushgrover.superlog.constants.Type;

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
        return String.format("%s %s: %s", timeStamp, tag, message);
    }

    public static String getLogStringForMail(@NonNull String tag, @NonNull String message, @NonNull String timeStamp, int type) {

        return String.format("%s %s/%s: %s \n", timeStamp, Utils.getLogType(type).getText(), tag, message);
    }

    public static Type getLogType(int type) {


        switch (type) {

            case DEBUG:
            case NORMAL:
                return new Type(R.color.debug, "DEBUG");


            case ERROR:
                return new Type(R.color.error, "ERROR");

            case VERBOSE:
                return new Type(R.color.verbose, "VERBOSE");

            case WARNING:
                return new Type(R.color.warn, "WARN");

            case INFO:
                return new Type(R.color.info, "INFO");
        }
        return new Type(R.color.verbose, "VERBOSE");
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
