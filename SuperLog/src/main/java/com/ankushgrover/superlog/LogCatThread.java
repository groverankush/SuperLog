package com.ankushgrover.superlog;

import com.ankushgrover.superlog.ContextWrapper;
import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.constants.SuperLogConstants;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 31/10/17.
 */

public class LogCatThread extends Thread {

    private static final String processId = Integer.toString(android.os.Process.myPid());
    private final ContextWrapper wrapper;


    public LogCatThread(ContextWrapper wrapper) {
        this.wrapper = wrapper;
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
                    SuperLog.log(getType(line), line, wrapper);
                }
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getType(String log) {

        switch (log.charAt(0)) {
            case 'D':
                return SuperLogConstants.DEBUG;

            case 'E':
                return SuperLogConstants.ERROR;

            case 'W':
                return SuperLogConstants.WARNING;

            case 'V':
                return SuperLogConstants.VERBOSE;

            case 'I':
                return SuperLogConstants.INFO;
        }

        return SuperLogConstants.DEBUG;

    }

}


