package com.ankushgrover.superlog.bg;

import com.ankushgrover.superlog.constants.SuperLogConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 31/10/17.
 */

public class LogCatThread extends Thread {

    private static final String processId = Integer.toString(android.os.Process.myPid());


    public LogCatThread() {

    }


    @Override
    public void run() {
        super.run();

        try {

            String commands[] = {"logcat", "-v", "tag"};
            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));


            while (!isInterrupted()) {
                String line;
                while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {

                }
                System.gc();
            }
        } catch (Exception e) {
        }
    }

    private int getType(String log) {

        int DEBUG = 0;
        int ERROR = 1;
        int WARNING = 2;
        int VERBOSE = 3;
        int NORMAL = 4;
        int INFO = 5;

        switch (log.charAt(0)) {
            case 'D':
                return SuperLogConstants.DEBUG;
        }

        return SuperLogConstants.DEBUG;

    }

}


