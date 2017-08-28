package com.ankushgrover.superlog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public  class Database extends SQLiteOpenHelper {

    private static Database mInstance;

    private static final String NAME = "superLog.db";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
