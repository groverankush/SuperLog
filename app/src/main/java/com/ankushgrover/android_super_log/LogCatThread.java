package com.ankushgrover.android_super_log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 31/10/17.
 */

public class LogCatThread extends Thread {

    private static final String processId = Integer.toString(android.os.Process.myPid());
    private final StringBuilder logBuilder;


    public LogCatThread() {
        logBuilder = new StringBuilder("");
    }


    @Override
    public void run() {
        super.run();

        try {

            String commands [] = {"logcat", "-v", "tag"};
            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));


            while (!isInterrupted()) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (!line.contains(processId))
                        continue;


                    logBuilder.append(line);

                }
                System.gc();
            }
        } catch (Exception e) {
        }
    }

    private void processLog(String log){
        //D/Something( 7754): 7754asdadasdasdasdasdasdasdasdasdasdsadasdasdasdasdasdasdasdasdasdasdsdfasdfasdfasdfargaergaerfvasHELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD HELLO WORLD

        StringBuilder builder = new StringBuilder(log);
        builder.replace(1,2, "");

        String arr[] = log.split("/", 2);
        String type = arr[0];
        log = arr[1];
    }

}


