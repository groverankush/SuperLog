package com.ankushgrover.superlog.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class TimeUtils {

    public static String getTimeStamp(){
        Calendar calendar =  Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        Date date = calendar.getTime();
        return df.format(date);
    }

}
