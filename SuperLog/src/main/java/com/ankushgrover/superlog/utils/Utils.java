package com.ankushgrover.superlog.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;

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

        return String.format("%s %s/%s: %s \n", timeStamp, Utils.getLogType(type).getText().charAt(0), tag, message);
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
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("/temp.properties");


/*        Properties properties = new Properties();
        InputStream inputStream =
                context.getClass().getClassLoader().getResourceAsStream("local.properties");*/
        try {
            properties.load(stream);
            String email = properties.getProperty("emailId");
            String pass = properties.getProperty("password");
            return new String[]{email, pass};

        } catch (Exception e) {
            e.printStackTrace();
            SuperLog.e("Error Fetching properties", "FAILED");
        }

        return new String[]{"hello", "world"};


    }

    /**
     * Manage view animation on visibility change.
     *
     * @param v
     * @param makeVisible
     */
    public static void manageViewVisibility(final View v, final boolean makeVisible) {
        if (makeVisible)
            v.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", (makeVisible ? 0f : 1f), (makeVisible ? 1f : 0f));
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!makeVisible)
                    v.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public static void manageViewVisibilityReveal(final View v, boolean makeVisible) {

        int cx = v.getWidth() / 2;
        int cy = v.getHeight() / 2;

        float radius = (float) Math.hypot(cx, cy);


        Animator anim = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            if (makeVisible) {
                anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                v.setVisibility(View.VISIBLE);
            } else {
                anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        v.setVisibility(View.GONE);
                    }
                });
            }
            anim.start();
        } else manageViewVisibility(v, makeVisible);


    }

}
