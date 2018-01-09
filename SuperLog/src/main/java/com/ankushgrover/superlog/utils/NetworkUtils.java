package com.ankushgrover.superlog.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.ankushgrover.superlog.R;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 13/9/17.
 */

public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context, boolean showLog) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        if (showLog)
            Log.e(context.getString(R.string.network_error), context.getString(R.string.device_not_connected));
        return false;
    }

}
