package com.ankushgrover.superlog.bg;

import com.ankushgrover.superlog.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 31/10/17.
 */

public class LogCatThread extends Thread {

    private StringBuilder logBuilder;

    public LogCatThread() {
        logBuilder = new StringBuilder();
    }


    @Override
    public void run() {
        super.run();

        try {
            Process process = Runtime.getRuntime().exec("logcat");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line = "", log = "", logType = "";
            while (!isInterrupted()) {
                while ((line = bufferedReader.readLine()) != null) {

                    logBuilder.append(line);


                    /*String[] parts = line.split(" ", 6);

                        if (parts.length > 5 && parts[4].length() == 1 && parts[4].matches("[EWVID]")) {

                            //// TODO: 22/9/17 Enter logtype and log in db.

                            logType = parts[4];
                            log = line;

                        } else {
                            log += "\n\t" + line;
                        }*/
                }
                System.gc();
            }
        } catch (Exception e) {
        }
    }

    public String getLogCatString() {
        String log = logBuilder.toString();
        if (Utils.isEmpty(log))
            log = "NO LOGS FOUND";

        return log;
    }
}


