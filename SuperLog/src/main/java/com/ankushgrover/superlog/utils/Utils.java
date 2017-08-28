package com.ankushgrover.superlog.utils;

import android.text.TextUtils;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class Utils {

    /**
     * To check whether the string is empty or not
     *
     * @param string
     * @return
     */
    public static boolean isEmpty(String string) {
        return TextUtils.isEmpty(string) || TextUtils.isEmpty(string.trim()) || "null".contentEquals(string.trim());
    }

}
