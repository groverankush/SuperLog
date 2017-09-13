package com.ankushgrover.superlog.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ankushgrover.superlog.SuperLog;
import com.ankushgrover.superlog.db.table.SuperLogTable;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 28/8/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String NAME = "superLog.db";
    private static final int DB_VERSION = 1;
    private static DbHelper mInstance;

    private DbHelper() {
        super(SuperLog.getBuilder().getContext(), NAME, null, DB_VERSION);
    }

    public static synchronized DbHelper getInstance() {
        if (mInstance == null)
            mInstance = new DbHelper();
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SuperLogTable.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists " + SuperLogTable.TABLE_NAME);
        onCreate(db);

    }

    public void clear() {
        getWritableDatabase().delete(SuperLogTable.TABLE_NAME, null, null);
    }

    public void getDb() {
        mInstance.getWritableDatabase();
    }

}
