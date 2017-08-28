package com.ankushgrover.superlog.db.table;

import android.provider.BaseColumns;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class SuperLogTable implements BaseColumns {

    public static final String TABLE_NAME = "SuperLogTable";
    public static final String TIMESTAMP = "timeStamp";
    public static final String TAG = "tag";
    public static final String MESSAGE = "message";
    public static final String TYPE = "type";

    public static final String CREATE_TABLE = "create table " +
            TABLE_NAME + "(_ID integer primary key autoincrement," +
            TAG + " text, " +
            MESSAGE + " text, " +
            TYPE + " integer , " +
            TIMESTAMP + " text );";

}
