package com.ankushgrover.superlog.constants;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 1/9/17.
 */

public enum LogType {

    DEBUG (0),
    ERROR (1),
    WARNING (2),
    VERBOSE (3),
    NORMAL (4),
    INFO (5);



    int value;

    LogType(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }

}
