package com.ankushgrover.superlog.bg;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 31/10/17.
 */

public class LogCatThread extends Thread {


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

                    String[] parts = line.split(" ", 6);

                        /*if (parts.length > 5 && parts[4].length() == 1 && parts[4].matches("[EWVID]")) {

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
}


